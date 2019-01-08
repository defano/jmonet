package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.layer.ImageLayer;
import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.tools.base.Tool;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A scratch buffer on which to draw ephemeral changes to the canvas.
 * <p>
 * The scratch buffer provides a graphic context on which paint tools can draw without modifying the canvas. For
 * example, the selection tool needs a layer on which to draw marching ants that does not result in the ants becoming
 * part of the underlying graphic. In general, all tools should draw on the scratch buffer until the change is ready to
 * be committed to the canvas; for many tools (like the paintbrush, pencil, line, or rectangle), the change is complete
 * only when the user releases the mouse.
 * <p>
 * The scratch buffer provides two scratch "modes": an "add" scratch for changes that add paint to the canvas, and a
 * "remove" scratch for changes that erase paint from the canvas. The add scratch works by overlaying the scratch
 * graphic to the canvas using composite mode {@link AlphaComposite#SRC_OVER}; the remove scratch uses
 * {@link AlphaComposite#DST_OUT}.
 * <p>
 * The dimension of the scratch buffer always matches the dimension of the canvas, but for performance, the scratch
 * buffer manages clipping regions to paint only the portion of the buffer that the tool has modified.
 */
public class Scratch {

    // Region of the addScratch and removeScratch that have been dirtied by tools
    private Rectangle addClip, removeClip;

    // Dimension of the scratch buffer
    private int width, height;

    // Scratch buffer data
    private BufferedImage addScratch, removeScratch;

    // Graphics context created from the buffers
    private Graphics2D addScratchGraphics, removeScratchGraphics;

    /**
     * Creates a scratch unbound to any tool with a given dimension.
     *
     * @param width  The width of the scratch buffer.
     * @param height The height of the scratch buffer.
     */
    public Scratch(int width, int height) {
        this.width = width;
        this.height = height;
        clear();
    }

    /**
     * Sets the size of the scratch buffer, keeping as much of the existing buffer's image as possible.
     *
     * @param width  The new width of the buffer
     * @param height The new height of the buffer
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        BufferedImage newAddScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newAddScratchGraphics = newAddScratch.getGraphics();
        newAddScratchGraphics.drawImage(addScratch, 0, 0, null);
        setAddScratch(newAddScratch, null);
        newAddScratchGraphics.dispose();

        BufferedImage newRemoveScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newRemoveScratchGraphics = newRemoveScratch.getGraphics();
        newRemoveScratchGraphics.drawImage(removeScratch, 0, 0, null);
        setRemoveScratch(newRemoveScratch, null);
        newRemoveScratchGraphics.dispose();
    }

    /**
     * Gets the dimensions of the scratch buffer.
     *
     * @return The size of the scratch buffer
     */
    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public Rectangle getBounds() {
        return new Rectangle(0, 0, this.width, this.height);
    }

    /**
     * Clears the scratch buffer (both add and remove buffers), restoring it to its original, unmodified (fully
     * transparent) state.
     */
    public void clear() {
        clearAdd();
        clearRemove();
    }

    /**
     * Clears the remove-scratch buffer, restoring it to its original, unmodified (fully transparent) state.
     */
    public void clearRemove() {
        setRemoveScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), null);
    }

    /**
     * Clears the add-scratch buffer, restoring it to its original, unmodified (fully transparent) state.
     */
    public void clearAdd() {
        setAddScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), null);
    }

    /**
     * Gets the remove-scratch graphics context configured and ready to be painted upon by the given tool. Anything
     * drawn onto this graphics context will be "erased" from the underlying canvas by overlaying it with the
     * {@link AlphaComposite#DST_OUT} composite mode.
     * <p>
     * Accepts a stroke and shape to configure the clipping rectangle; the clipping region will be set equal to the
     * union of the existing clipping region and the bounds of this stroked shape.
     *
     * @param tool   The tool that will be drawing on the scratch, or null, if another type of object is requesting the
     *               buffer. Tool argument is used to retrieve the desired anti-aliasing mode for the context.
     * @param stroke The stroke that will be used when rendering the shape, or null if no stroke should be applied.
     * @param shape  The shape that will be painted by the tool, or null if the existing clipping region shouldn't be
     *               modified.
     * @return The remove-scratch buffer ready for use.
     */
    public Graphics2D getRemoveScratchGraphics(Tool tool, Stroke stroke, Shape shape) {
        return getRemoveScratchGraphics(tool, getShapeBounds(stroke, shape));
    }

    /**
     * Gets the remove-scratch graphics context configured and ready to be painted upon by the given tool. Anything
     * drawn onto this graphics context will be "erased" from the underlying canvas by overlaying it with the
     * {@link AlphaComposite#DST_OUT} composite mode.
     * <p>
     * Accepts a shape to configure the clipping rectangle; the clipping region will be set equal to the union of the
     * existing clipping region and the bounds of the shape.
     *
     * @param tool   The tool that will be drawing on the scratch, or null, if another type of object is requesting the
     *               buffer. Tool argument is used to retrieve the desired anti-aliasing mode for the context.
     * @param bounds The shape that will be painted by the tool, or null if the existing clipping region shouldn't be
     *               modified.
     * @return The remove-scratch buffer ready for use.
     */
    public Graphics2D getRemoveScratchGraphics(Tool tool, Shape bounds) {
        removeClip = updateClip(bounds, removeClip);

        if (tool != null) {
            tool.applyRenderingHints(removeScratchGraphics);
        }

        return removeScratchGraphics;
    }

    /**
     * Gets the add-scratch graphics context configured and ready to be painted upon by the given tool. Anything
     * drawn onto this graphics context will be "added" to the underlying canvas by overlaying it with the
     * {@link AlphaComposite#SRC_OVER} composite mode.
     * <p>
     * Accepts a stroke and shape to configure the clipping rectangle; the clipping region will be set equal to the
     * union of the existing clipping region and the bounds of this stroked shape.
     *
     * @param tool   The tool that will be drawing on the scratch, or null, if another type of object is requesting the
     *               buffer. Tool argument is used to retrieve the desired anti-aliasing mode for the context.
     * @param stroke The stroke that will be used when rendering the shape, or null if no stroke should be applied.
     * @param shape  The shape that will be painted by the tool, or null if the existing clipping region shouldn't be
     *               modified.
     * @return The remove-scratch buffer ready for use.
     */
    public Graphics2D getAddScratchGraphics(Tool tool, Stroke stroke, Shape shape) {
        return getAddScratchGraphics(tool, getShapeBounds(stroke, shape));
    }

    /**
     * Gets the add-scratch graphics context configured and ready to be painted upon by the given tool. Anything
     * drawn onto this graphics context will be "added" to the underlying canvas by overlaying it with the
     * {@link AlphaComposite#SRC_OVER} composite mode.
     * <p>
     * Accepts a shape to configure the clipping rectangle; the clipping region will be set equal to the union of the
     * existing clipping region and the bounds of this shape.
     *
     * @param tool   The tool that will be drawing on the scratch, or null, if another type of object is requesting the
     *               buffer. Tool argument is used to retrieve the desired anti-aliasing mode for the context.
     * @param bounds The shape that will be painted by the tool, or null if the existing clipping region shouldn't be
     *               modified.
     * @return The remove-scratch buffer ready for use.
     */
    public Graphics2D getAddScratchGraphics(Tool tool, Shape bounds) {
        addClip = updateClip(bounds, addClip);

        if (tool != null) {
            tool.applyRenderingHints(addScratchGraphics);
        }

        return addScratchGraphics;
    }

    /**
     * Replaces the current add-scratch buffer with a provided image. Set the buffer's clipping region to the bounds
     * provided.
     *
     * @param addScratch The new add-scratch image.
     * @param clip The dirty region of the new buffer; null means no area is dirty, whereas a rectangle whose dimensions
     *             equal those of the image means the entire buffer is dirty.
     */
    public void setAddScratch(BufferedImage addScratch, Rectangle clip) {
        if (addScratchGraphics != null) {
            addScratchGraphics.dispose();
        }

        this.addScratch = addScratch;
        this.addScratchGraphics = this.addScratch.createGraphics();
        this.addClip = clip;
    }

    /**
     * Replaces the current remove-scratch buffer with a provided image. Sets the buffer's clipping region to the bounds
     * of the image.
     *
     * @param removeScratch The new remove-scratch image.
     * @param clip The dirty region of the new buffer; null means no area is dirty, whereas a rectangle whose dimensions
     *             equal those of the image means the entire buffer is dirty.
     */
    public void setRemoveScratch(BufferedImage removeScratch, Rectangle clip) {
        if (removeScratchGraphics != null) {
            removeScratchGraphics.dispose();
        }

        this.removeScratch = removeScratch;
        this.removeScratchGraphics = this.removeScratch.createGraphics();
        this.removeClip = clip;
    }

    /**
     * Gets an {@link ImageLayer} representing the remove-scratch image.
     *
     * @return The remove-scratch, as a {@link ImageLayer}
     */
    public ImageLayer getRemoveScratchLayer() {

        if (removeClip == null || removeClip.isEmpty()) {
            return null;
        }

        return new ImageLayer(
                removeClip.getLocation(),
                removeScratch.getSubimage(removeClip.x, removeClip.y, removeClip.width, removeClip.height),
                AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
    }

    /**
     * Gets an {@link ImageLayer} representing the add-scratch image.
     *
     * @return The add-scratch, as a {@link ImageLayer}
     */
    public ImageLayer getAddScratchLayer() {

        if (addClip == null || addClip.isEmpty()) {
            return null;
        }

        return new ImageLayer(
                addClip.getLocation(),
                addScratch.getSubimage(addClip.x, addClip.y, addClip.width, addClip.height),
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Gets an {@link ImageLayerSet} comprised of the add-scratch overlaid upon the remove-scratch.
     *
     * @return The scratch buffer as a {@link ImageLayerSet}.
     */
    public ImageLayerSet getLayerSet() {
        ImageLayerSet imageLayerSet = new ImageLayerSet();

        if (removeClip != null) {
            imageLayerSet.addLayer(getRemoveScratchLayer());
        }

        if (addClip != null) {
            imageLayerSet.addLayer(getAddScratchLayer());
        }

        return imageLayerSet;
    }

    /**
     * Calculates the bounds of shape stroked by a given stroke.
     *
     * @param stroke The stroke with which to stroke the shape.
     * @param shape  The shape to be stroked and bounds calculated.
     * @return The smallest rectangle that encloses the stroked shape.
     */
    private Rectangle getShapeBounds(Stroke stroke, Shape shape) {
        if (shape == null) {
            return new Rectangle();
        }

        return stroke != null ? stroke.createStrokedShape(shape).getBounds() : shape.getBounds();
    }

    /**
     * Sets the scratch buffer's clipping region to union of the existing clipping region and the bounds of the given
     * shape.
     *
     * @param shape   The shape representing the bounds to be added to the existing region
     * @param clip The graphics context whose clipping region should be set.
     */
    private Rectangle updateClip(Shape shape, Rectangle clip) {
        if (shape != null) {
            if (clip == null || clip.isEmpty()) {
                clip = getBounds().intersection(shape.getBounds());
            } else {
                clip = getBounds().intersection(clip.union(shape.getBounds()));
            }
        }

        return clip;
    }
}
