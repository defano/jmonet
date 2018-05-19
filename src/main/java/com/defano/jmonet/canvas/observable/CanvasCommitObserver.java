package com.defano.jmonet.canvas.observable;

import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.canvas.PaintCanvas;

import java.awt.image.BufferedImage;

/**
 * An object which listens to changes being committed to a canvas.
 */
public interface CanvasCommitObserver {
    /**
     * Fires when an new shape or image is committed from scratch onto the canvas.
     *
     * @param canvas The canvas on which the commit is occurring.
     * @param imageLayerSet The set of changes being committed.
     * @param canvasImage The resulting canvas image (including the committed change)
     */
    void onCommit(PaintCanvas canvas, ImageLayerSet imageLayerSet, BufferedImage canvasImage);
}
