package com.defano.jmonet.tools.attributes;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A function for filling a single pixel on the canvas.
 */
public interface FillFunction {

    /**
     * Fills a single pixel in an image with a given paint or texture.
     *
     * @param image     The image whose pixel should be filled.
     * @param x         The x coordinate of the point / pixel to fill
     * @param y         The y coordinate of the point / pixel to fill
     * @param fillPaint The paint to apply to the given pixel.
     */
    default void fill(BufferedImage image, int x, int y, Paint fillPaint) {
        if (fillPaint instanceof Color) {
            image.setRGB (x, y, ((Color) fillPaint).getRGB());
        } else if (fillPaint instanceof TexturePaint) {
            BufferedImage texture = ((TexturePaint) fillPaint).getImage();
            image.setRGB (x, y, texture.getRGB(x % texture.getWidth(), y % texture.getHeight()));
        } else {
            throw new IllegalArgumentException("Don't know how to fill using this kind of paint: " + fillPaint);
        }
    }
}
