package com.defano.jmonet.canvas.surface;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An image that composed of multiple image layers rendered atop one another.
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
        ImageLayer[] layers = getImageLayers();

        if (layers == null || layers.length == 0) {
            throw new IllegalStateException("Cannot render empty image.");
        }

        Dimension size = getSize();
        BufferedImage rendering = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rendering.createGraphics();

        for (ImageLayer layer : layers) {
            layer.drawOnto(g);
        }

        g.dispose();

        return rendering;
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
