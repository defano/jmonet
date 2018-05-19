package com.defano.jmonet.algo.transform.pixel;

import com.defano.jmonet.algo.transform.PixelTransform;

import java.awt.*;

/**
 * Adjusts the level of transparency (alpha) in each affected pixel.
 */
public class TransparencyPixelTransform implements PixelTransform {

    private final int delta;

    /**
     * Creates a transparency-adjusting transform.
     *
     * @param delta The amount by which to adjust each affected pixel's alpha value. A value of +255 assures every pixel
     *              if fully opaque; a value of -255 assures every pixel is fully transparent.
     */
    public TransparencyPixelTransform(int delta) {
        this.delta = delta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int apply(int x, int y, int rgb) {
        Color color = new Color(rgb, true);

        int alpha = color.getAlpha() + delta;
        alpha = alpha > 0xff ? 0xff : alpha < 0 ? 0 : alpha;

        // Adjust alpha preserving color channel
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
}
