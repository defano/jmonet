package com.defano.jmonet.algo.fill;

import java.awt.image.BufferedImage;

/**
 * A function for determining boundaries when flood-filling a raster.
 */
public interface BoundaryFunction {

    /**
     * Determines if a given pixel on the canvas should be flood-filled. This method will be invoked repeatedly until
     * the flood fill algorithm has filled all available pixels, thus, this method should execute quickly.
     * <p>
     * When flood-filling an image, all pixels adjacent to the selected pixel will attempt to be recursively "filled"
     * with paint. This method will be invoked with each point to determine when the flood fill has reached a pixel or
     * image boundary that should not be filled.
     * <p>
     * NOTE: This method may be called more than once with the same point. To prevent an infinite loop, this method
     * must return 'false' for any point that previously returned true (i.e., any flood-filled pixel must constitute a
     * boundary).
     *
     * @param canvas  The existing canvas image to be filled (that is, the user's image prior to any fill-related
     *                changes).
     * @param scratch The scratch buffer containing just the flood-fill changes (does not contain any of the user's
     *                image committed to the canvas). Note the bounds of this image are equal to that of the canvas
     *                image.
     * @param x       The x coordinate of the pixel in the image to boundary check
     * @param y       The y coordinate of the pixel in the image to boundary check
     * @return True if the given point in the image should be filled, false otherwise (i.e., the pixel represents a
     * boundary)
     */
    boolean shouldFillPixel(BufferedImage canvas, BufferedImage scratch, int x, int y);
}
