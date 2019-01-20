package com.defano.jmonet.transform.pixel;

import com.defano.jmonet.transform.image.PixelTransform;

/**
 * Inverts the color value of each affected pixel.
 */
public class InvertPixelTransform implements PixelTransform {

    /**
     * {@inheritDoc}
     */
    @Override
    public int apply(int argb) {
        int alpha = 0xff000000 & argb;
        int rgb = 0x00ffffff & argb;

        // Invert preserving alpha channel
        return alpha | (~rgb & 0x00ffffff);
    }
}
