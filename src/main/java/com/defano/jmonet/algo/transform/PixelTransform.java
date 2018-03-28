package com.defano.jmonet.algo.transform;

public interface PixelTransform {

    /**
     * Performs a transformation on an RGB color value.
     *
     * @param x The x-coordinate of the pixel being transformed
     * @param y The y-coordinate of the pixel being transformed
     * @param rgb The pixel's color value
     * @return The transformed pixel's color value
     */
    int transformPixel(int x, int y, int rgb);
}
