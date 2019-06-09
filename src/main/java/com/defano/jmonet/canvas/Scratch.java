package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.layer.ImageLayer;
import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.context.AwtGraphicsContext;
import com.defano.jmonet.context.GraphicsContext;
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
 * buffer manages a dirty region to paint only the portion of the buffer that the tool has modified.
 */
public class Scratch {

    // Region of the addScratch and removeScratch that have been dirtied by tools (null implies no changes)
    private Rectangle addScratchDirtyRgn;
    private Rectangle removeScratchDirtyRgn;

    // Dimension of the scratch buffer
    private int width;
    private int height;

    // Scratch buffer data
    private BufferedImage addScratch;
    private BufferedImage removeScratch;

    // Graphics context created from the buffers
    private GraphicsContext addScratchGraphics;
    private GraphicsContext removeScratchGraphics;

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

    /**
     * Gets the bounds (location and dimension) of the scratch buffer. Note the buffer is always located at the orgin
     * (0,0).
     *
     * @return The bounding rectangle of the scratch buffer.
     */
    public Rectangle getBounds() {
        return new Rectangle(0, 0, this.width, this.height);
    }

    /**
     * Clears the scratch buffer (both add and remove buffers), restoring it to its original, unmodified (fully
     * transparent) state.
     */
    public void clear() {
        clearAddScratch();
        clearRemoveScratch();
    }

    /**
     * Clears the remove-scratch buffer, restoring it to its original, unmodified (fully transparent) state.
     */
    public void clearRemoveScratch() {
        setRemoveScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), null);
    }

    /**
     * Clears the add-scratch buffer, restoring it to its original, unmodified (fully transparent) state.
     */
    public void clearAddScratch() {
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
    public GraphicsContext getRemoveScratchGraphics(Tool tool, Stroke stroke, Shape shape) {
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
    public GraphicsContext getRemoveScratchGraphics(Tool tool, Shape bounds) {
        removeScratchDirtyRgn = updateDirtiedRgn(bounds, removeScratchDirtyRgn);

        if (tool != null) {
            removeScratchGraphics.setAntialiasingMode(tool.getAttributes().getAntiAliasing());
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
    public GraphicsContext getAddScratchGraphics(Tool tool, Stroke stroke, Shape shape) {
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
    public GraphicsContext getAddScratchGraphics(Tool tool, Shape bounds) {
        addScratchDirtyRgn = updateDirtiedRgn(bounds, addScratchDirtyRgn);

        if (tool != null) {
            addScratchGraphics.setAntialiasingMode(tool.getAttributes().getAntiAliasing());
        }

        return addScratchGraphics;
    }

    /**
     * Replaces the current add-scratch buffer with a provided image. Set the buffer's clipping region to the bounds
     * provided.
     *
     * @param addScratch The new add-scratch image.
     * @param dirtyRgn The dirty region of the new buffer; null means no area is dirty, whereas a rectangle whose dimensions
     *             equal those of the image means the entire buffer is dirty.
     */
    public void setAddScratch(BufferedImage addScratch, Rectangle dirtyRgn) {
        if (addScratchGraphics != null) {
            addScratchGraphics.dispose();
        }

        this.addScratch = addScratch;
        this.addScratchGraphics = new AwtGraphicsContext(this.addScratch.createGraphics());
        this.addScratchDirtyRgn = dirtyRgn;
    }

    /**
     * Replaces the current remove-scratch buffer with a provided image. Sets the buffer's clipping region to the bounds
     * of the image.
     *
     * @param removeScratch The new remove-scratch image.
     * @param dirtyRgn The dirty region of the new buffer; null means no area is dirty, whereas a rectangle whose dimensions
     *             equal those of the image means the entire buffer is dirty.
     */
    public void setRemoveScratch(BufferedImage removeScratch, Rectangle dirtyRgn) {
        if (removeScratchGraphics != null) {
            removeScratchGraphics.dispose();
        }

        this.removeScratch = removeScratch;
        this.removeScratchGraphics = new AwtGraphicsContext(this.removeScratch.createGraphics());
        this.removeScratchDirtyRgn = dirtyRgn;
    }

    /**
     * Gets an {@link ImageLayer} representing the remove-scratch image.
     *
     * @return The remove-scratch, as a {@link ImageLayer}
     */
    public ImageLayer getRemoveScratchLayer() {

        if (removeScratchDirtyRgn == null || removeScratchDirtyRgn.isEmpty()) {
            return null;
        }

        return new ImageLayer(
                removeScratchDirtyRgn.getLocation(),
                removeScratch.getSubimage(removeScratchDirtyRgn.x, removeScratchDirtyRgn.y, removeScratchDirtyRgn.width, removeScratchDirtyRgn.height),
                AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
    }

    /**
     * Gets an {@link ImageLayer} representing the add-scratch image.
     *
     * @return The add-scratch, as a {@link ImageLayer}
     */
    public ImageLayer getAddScratchLayer() {

        if (addScratchDirtyRgn == null || addScratchDirtyRgn.isEmpty()) {
            return null;
        }

        return new ImageLayer(
                addScratchDirtyRgn.getLocation(),
                addScratch.getSubimage(addScratchDirtyRgn.x, addScratchDirtyRgn.y, addScratchDirtyRgn.width, addScratchDirtyRgn.height),
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Gets an {@link ImageLayerSet} comprised of the add-scratch overlaid upon the remove-scratch.
     *
     * @return The scratch buffer as a {@link ImageLayerSet}.
     */
    public ImageLayerSet getLayerSet() {
        ImageLayerSet imageLayerSet = new ImageLayerSet();

        if (removeScratchDirtyRgn != null) {
            imageLayerSet.addLayer(getRemoveScratchLayer());
        }

        if (addScratchDirtyRgn != null) {
            imageLayerSet.addLayer(getAddScratchLayer());
        }

        return imageLayerSet;
    }

    /**
     * A convenience method to erase paint from all pixels bounded by a given shape and stroke, taking into account
     * canvas background and erase color.
     *
     * @param tool The tool that is erasing
     * @param shape The shape to be erased
     * @param stroke The stroke of the shape being erased
     */
    public void erase(Tool tool, Shape shape, Stroke stroke) {
        Paint erasePaint = tool.getAttributes().getEraseColor();

        GraphicsContext g = erasePaint == null ?
                getRemoveScratchGraphics(tool, stroke, shape) :
                getAddScratchGraphics(tool, stroke, shape);

        g.setStroke(stroke);
        g.setPaint(erasePaint == null ? tool.getCanvas().getCanvasBackground() : erasePaint);
        g.draw(shape);
    }

    /**
     * Gets a rectangle identifying a sub-region of the scratch buffer that has been modified and needs to be repainted.
     * @return The region of the scratch buffer that has been marked as dirty by tools.
     */
    public Rectangle getDirtyRegion() {
        if (addScratchDirtyRgn == null) {
            return removeScratchDirtyRgn;
        }

        if (removeScratchDirtyRgn == null) {
            return addScratchDirtyRgn;
        }

        return addScratchDirtyRgn.union(removeScratchDirtyRgn);
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
     * Returns the union of the bounds of the given shape with a given dirty region rectangle, intersected by the bounds
     * of this buffer.
     *
     * @param shape   The shape representing the bounds to be added to the existing region
     * @param dirtyRgn A rectangle identifying a region of the scratch buffer that has been dirtied
     * @return A union of the previously dirtied region and the newly dirtied region
     */
    private Rectangle updateDirtiedRgn(Shape shape, Rectangle dirtyRgn) {
        if (shape != null) {
            if (dirtyRgn == null || dirtyRgn.isEmpty()) {
                dirtyRgn = getBounds().intersection(shape.getBounds());
            } else {
                dirtyRgn = getBounds().intersection(dirtyRgn.union(shape.getBounds()));
            }
        }

        return dirtyRgn;
    }
}
