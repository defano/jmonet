package com.defano.jmonet.canvas.layer;

/**
 * An image with multiple layers that can be drawn at scale.
 */
public interface ScaledLayeredImage extends LayeredImage {

    /**
     * Gets the displayed scale factor of this image. A scale of 1.0 means no scaling; a value of 2.0 means the image
     * should be drawn at a 2:1 scale.
     *
     * @return The displayed scale factor of this image.
     */
    double getScale();
}
