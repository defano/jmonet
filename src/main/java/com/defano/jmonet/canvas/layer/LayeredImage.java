package com.defano.jmonet.canvas.layer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image that is composed of multiple layers rendered atop one another.
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
        drawOnto(g);
        g.dispose();

        return rendering;
    }

    /**
     * Draws this layered image onto the given graphics context.
     *
     * @param g The graphics context on which to draw
     */
    default void drawOnto(Graphics2D g) {
        for (ImageLayer layer : getImageLayers()) {
            layer.drawOnto(g);
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
            Dimension layerDimension = thisLayer.getSize();
            height = Math.max(height, layerDimension.height);
            width = Math.max(width, layerDimension.width);
        }

        return new Dimension(width, height);
    }

}
