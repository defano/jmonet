package com.defano.jmonet.algo.fill;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A function for filling a single pixel on the canvas.
 */
public interface FillFunction {

    /**
     * Fills a single pixel in a image with a given paint or texture.
     *
     * @param image The image whose pixel should be filled.
     * @param p The point / pixel to fill
     * @param fillPaint The paint to apply to the given pixel.
     */
    void fill(BufferedImage image, Point p, Paint fillPaint);
}
