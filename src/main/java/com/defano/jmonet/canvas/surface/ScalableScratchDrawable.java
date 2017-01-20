package com.defano.jmonet.canvas.surface;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Models a drawable surface with two layers; a canvas, and a temporary "scratch" buffer that can be scaled.
 */
public interface ScalableScratchDrawable {

    /**
     * Gets the "scratch" image associated with this drawable. The "scratch" image is a temporary buffer allowing
     * tools to draw on the canvas in a way that doesn't affect the underlying graphic.
     *
     * @return The scratch buffer image.
     */
    BufferedImage getScratchImage();

    /**
     * Gets the image represented by this drawable; not including any ephemeral changes made to the scratch image.
     *
     * @return The canvas image.
     */
    BufferedImage getCanvasImage();

    /**
     * Gets the displayed scale factor of this image.
     *
     * @return The displayed scale factor of this image.
     */
    double getScale();

    Point convertPointToImage(Point p);
}
