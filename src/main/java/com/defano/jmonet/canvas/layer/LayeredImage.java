package com.defano.jmonet.canvas.layer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image that is composed of multiple layers rendered one atop another.
 */
public interface LayeredImage {

    /**
     * Gets the layers of this image in the order in which they should be drawn.
     *
     * @return The image layers
     */
    ImageLayer[] getImageLayers();

    /**
     * Produces a new BufferedImage containing each of the layers of this image rendered atop one another.
     *
     * @return A rendering of this image.
     */
    default BufferedImage render() {
        Dimension size = getSize();
        BufferedImage rendering = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = rendering.createGraphics();
        paint(g, 1.0, null);
        g.dispose();

        return rendering;
    }

    /**
     * Draws this layered image onto the given graphics context.
     *
     * @param g     The graphics context on which to draw
     * @param scale The scale at which to draw the image
     * @param clip  The clipping rectangle describing the bounds of the graphics context that should be painted
     */
    default void paint(Graphics2D g, Double scale, Rectangle clip) {
        for (ImageLayer thisLayer : getImageLayers()) {
            if (thisLayer != null) {
                thisLayer.paint(g, scale, clip);
            }
        }
    }

    /**
     * Calculates the size of this image; equal to the dimensions of the largest layer in the image.
     *
     * @return The size of this image.
     */
    default Dimension getSize() {
        int height = 0, width = 0;

        for (ImageLayer thisLayer : getImageLayers()) {
            Dimension layerDimension = thisLayer.getDisplayedSize();
            height = Math.max(height, layerDimension.height);
            width = Math.max(width, layerDimension.width);
        }

        return new Dimension(width, height);
    }

}
