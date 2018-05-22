package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.layer.ImageLayer;
import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.tools.builder.PaintTool;
import com.defano.jmonet.tools.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A scratch buffer for changes being made to the canvas that haven't yet been committed.
 */
public class Scratch {

    private int width, height;
    private BufferedImage addScratch;
    private BufferedImage removeScratch;
    private Graphics2D addScratchGraphics, removeScratchGraphics;
    private final PaintTool tool;

    public Scratch(int width, int height) {
        this(null, width, height);
    }

    public Scratch(PaintTool tool, int width, int height) {
        this.tool = tool;
        this.width = width;
        this.height = height;

        clear();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        BufferedImage newAddScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newAddScratchGraphics = newAddScratch.getGraphics();
        newAddScratchGraphics.drawImage(getAddScratch(), 0, 0, null);
        setAddScratch(newAddScratch);
        newAddScratchGraphics.dispose();

        BufferedImage newRemoveScratch = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics newRemoveScratchGraphics = newRemoveScratch.getGraphics();
        newRemoveScratchGraphics.drawImage(getRemoveScratch(), 0, 0, null);
        setRemoveScratch(newRemoveScratch);
        newRemoveScratchGraphics.dispose();
    }

    public void clear() {
        clearAdd();
        clearRemove();
    }

    public void clearRemove() {
        setRemoveScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));

        if (tool != null) {
            tool.applyRenderingHints(removeScratchGraphics);
        }
    }

    public void clearAdd() {
        setAddScratch(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));

        if (tool != null) {
            tool.applyRenderingHints(addScratchGraphics);
        }
    }

    public BufferedImage getAddScratch() {
        return addScratch;
    }

    public Graphics2D getRemoveScratchGraphics() {
        return removeScratchGraphics;
    }

    public Graphics2D getAddScratchGraphics() {
        return addScratchGraphics;
    }

    public void setAddScratch(BufferedImage addScratch) {
        if (addScratchGraphics != null) {
            addScratchGraphics.dispose();
        }

        this.addScratch = addScratch;
        this.addScratchGraphics = this.addScratch.createGraphics();
    }

    public ImageLayer getRemoveScratchLayer() {
        Rectangle minBounds = removeScratchGraphics.getClipBounds();
        if (minBounds == null || minBounds.isEmpty()) {
            return null;
        }

        BufferedImage subimage = removeScratch.getSubimage(minBounds.x, minBounds.y, minBounds.width, minBounds.height);
        return new ImageLayer(minBounds.getLocation(), subimage, AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
    }

    public ImageLayer getAddScratchLayer() {
        Rectangle minBounds = addScratchGraphics.getClipBounds();

        if (minBounds == null || minBounds.isEmpty()) {
            return null;
        }

        BufferedImage subimage = addScratch.getSubimage(minBounds.x, minBounds.y, minBounds.width, minBounds.height);
        return new ImageLayer(minBounds.getLocation(), subimage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public BufferedImage getRemoveScratch() {
        return removeScratch;
    }

    public void setRemoveScratch(BufferedImage removeScratch) {
        if (removeScratchGraphics != null) {
            removeScratchGraphics.dispose();
        }

        this.removeScratch = removeScratch;
        this.removeScratchGraphics = this.removeScratch.createGraphics();
    }

    public ImageLayerSet getLayerSet() {
        ImageLayerSet imageLayerSet = new ImageLayerSet();

        if (removeScratchGraphics.getClipBounds() != null) {
            imageLayerSet.addLayer(getRemoveScratchLayer());
        }

        if (addScratchGraphics.getClipBounds() != null) {
            imageLayerSet.addLayer(getAddScratchLayer());
        }

        return imageLayerSet;
    }

    public void updateAddScratchClip(Stroke stroke, Shape shape) {
        updateClip(stroke, shape, addScratchGraphics);
    }

    public void updateRemoveScratchClip(Stroke stroke, Shape shape) {
        updateClip(stroke, shape, removeScratchGraphics);
    }

    private void updateClip(Stroke stroke, Shape shape, Graphics2D context) {
        Rectangle clip = stroke != null ?
                stroke.createStrokedShape(shape).getBounds() :
                shape.getBounds();

        clip = clip.intersection(new Rectangle(0, 0, width, height));
        Rectangle bounds = context.getClipBounds();

        if (bounds == null) {
            context.setClip(clip.x, clip.y, clip.width, clip.height);
        }

        else if (!bounds.contains(clip)) {
            Rectangle union = bounds.union(clip);
            context.setClip(union.x, union.y, union.width, union.height);
        }
    }
}
