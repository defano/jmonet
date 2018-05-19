package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.layer.LayeredImage;

import java.awt.*;

/**
 * An image with multiple layers that can be drawn at scale.
 */
public interface ScaledLayeredImage extends LayeredImage {

    /**
     * Gets the displayed scale factor of this image.
     * @return The displayed scale factor of this image.
     */
    double getScale();

    /**
     * Converts a points on the rendered canvas to the equivalent point on the canvas' image, taking into account scale,
     * grids or other complications that may be applied during rendering.
     *
     * When a scale factor has been applied, then the input coordinates are divided by the scale factor. When a grid
     * factor has been applied, the result of de-scaling the input is then rounded to nearest grid spacing factor.
     *
     * @param p A point in scaled, surface space
     * @return The equivalent point in terms of the canvas image.
     */
    Point convertViewPointToModel(Point p);
}
