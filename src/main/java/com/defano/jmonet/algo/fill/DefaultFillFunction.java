package com.defano.jmonet.algo.fill;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Default fill function; fills pixels with solid color or texture.
 */
public class DefaultFillFunction implements FillFunction {

    @Override
    public void fill(BufferedImage image, Point p, Paint fillPaint) {
        int rgb = getFillPixel(p.x, p.y, fillPaint);
        image.setRGB(p.x, p.y, rgb);
    }

    private int getFillPixel(int x, int y, Paint paint) {

        if (paint instanceof Color) {
            return ((Color) paint).getRGB();
        } else if (paint instanceof TexturePaint) {
            BufferedImage texture = ((TexturePaint) paint).getImage();
            return texture.getRGB(x % texture.getWidth(), y % texture.getHeight());
        }

        throw new IllegalArgumentException("Don't know how to fill with paint " + paint);
    }
}
