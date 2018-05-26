package com.defano.jmonet.algo.fill;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Default fill function; fills pixels with solid color or texture.
 */
public class DefaultFillFunction implements FillFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(BufferedImage image, int x, int y, Paint fillPaint) {

        if (fillPaint instanceof Color) {
            image.setRGB (x, y, ((Color) fillPaint).getRGB());
        } else if (fillPaint instanceof TexturePaint) {
            BufferedImage texture = ((TexturePaint) fillPaint).getImage();
            int rgb = texture.getRGB(x % texture.getWidth(), y % texture.getHeight());
            image.setRGB (x, y, rgb);
        } else {
            throw new IllegalArgumentException("Don't know how to fill with paint " + fillPaint);
        }
    }
}
