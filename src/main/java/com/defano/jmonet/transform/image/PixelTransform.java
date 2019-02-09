package com.defano.jmonet.transform.image;

/**
 * A transform that can be applied to an individual pixel, like invert, or brightness adjustment.
 */
public interface PixelTransform {

    /**
     * Performs a transformation on a pixel's RGB color value.
     *
     * @param rgb The pixel's color value
     * @return The transformed pixel's color value
     */
    int apply(int rgb);
}
