package com.defano.jmonet.transform.pixel;

import com.defano.jmonet.transform.image.PixelTransform;

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
     *              is fully opaque; a value of -255 assures every pixel is fully transparent.
     */
    public TransparencyPixelTransform(int delta) {
        this.delta = delta;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("squid:S3358")
    @Override
    public int apply(int rgb) {
        Color color = new Color(rgb, true);

        int alpha = color.getAlpha() + delta;
        alpha = alpha > 0xff ? 0xff : alpha < 0 ? 0 : alpha;

        // Adjust alpha preserving color channel
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
}
