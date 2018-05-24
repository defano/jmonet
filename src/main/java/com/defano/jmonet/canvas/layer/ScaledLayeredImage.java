package com.defano.jmonet.canvas.layer;

/**
 * An image with multiple layers that can be drawn at scale.
 */
public interface ScaledLayeredImage extends LayeredImage {

    /**
     * Gets the displayed scale factor of this image.
     * @return The displayed scale factor of this image.
     */
    double getScale();
}
