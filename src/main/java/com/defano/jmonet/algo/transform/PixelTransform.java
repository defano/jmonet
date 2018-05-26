package com.defano.jmonet.algo.transform;

/**
 * A transform that can be applied to an individual pixel, like invert, or brightness adjustment.
 */
public interface PixelTransform {

    /**
     * Performs a transformation on a pixel's RGB color value.
     *
     * @param x The x-coordinate of the pixel being transformed
     * @param y The y-coordinate of the pixel being transformed
     * @param rgb The pixel's color value
     * @return The transformed pixel's color value
     */
    int apply(int x, int y, int rgb);
}
