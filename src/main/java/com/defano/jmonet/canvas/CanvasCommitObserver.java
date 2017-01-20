package com.defano.jmonet.canvas;

import java.awt.image.BufferedImage;

/**
 * An object which listens to changes being committed to a canvas.
 */
public interface CanvasCommitObserver {
    /**
     * Fires when an new shape or image is committed from scratch onto the canvas.
     *
     * @param canvas The canvas on which the commit is occurring.
     * @param committedElement An image representing just the change being committed.
     * @param canvasImage The resulting canvas image (including the committed change)
     */
    void onCommit(PaintCanvas canvas, BufferedImage committedElement, BufferedImage canvasImage);
}
