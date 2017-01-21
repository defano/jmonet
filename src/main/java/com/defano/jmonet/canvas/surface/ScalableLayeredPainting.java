package com.defano.jmonet.canvas.surface;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image with multiple layers that can be drawn at scale.
 */
public interface ScalableLayeredPainting {

    /**
     * Gets the layers of this image, in the order in which they should be drawn.
     * @return The image layers
     */
    BufferedImage[] getPaintLayers();

    /**
     * Gets the displayed scale factor of this image.
     * @return The displayed scale factor of this image.
     */
    double getScale();

    /**
     * Converts a scaled, surface coordinate to a canvas coordinate and snaps the resulting coordinate to the nearest grid
     * coordinate. When no scale and no grid has been applied (i.e., scale = 1.0, grid = 1) then the output of this
     * function is the same as its input.
     *
     * When a scale factor has been applied, then the input coordinates are divided by the scale factor.
     *
     * When a grid factor has been applied, the result of de-scaling the input is then rounded to nearest grid spacing
     * factor.
     *
     * @param p A point in scaled, surface space
     * @return The equivalent point in terms of the canvas image.
     */
    Point convertPointToImage(Point p);
}
