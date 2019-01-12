package com.defano.jmonet.transform.pixel;

import com.defano.jmonet.transform.image.PixelTransform;

/**
 * Modifies the brightness (luminosity) of each affected pixel by adding/subtracting a delta value to each color channel
 * of an affected pixel.
 */
public class BrightnessPixelTransform implements PixelTransform {

    private final int delta;

    /**
     * Creates a brightness-adjusting transform.
     *
     * @param delta The amount by which to adjust brightness; a value of -255 assures that every pixel is completely
     *              black; a value of +255 assures that every pixel is completely white.
     */
    public BrightnessPixelTransform(int delta) {
        this.delta = delta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int apply(int x, int y, int rgb) {
        int alpha = 0xff000000 & rgb;
        int r = ((0xff0000 & rgb) >> 16) + delta;
        int g = ((0xff00 & rgb) >> 8) + delta;
        int b = (0xff & rgb) + delta;

        // Saturate at 0 and 256
        r = r > 0xff ? 0xff : r < 0 ? 0 : r;
        g = g > 0xff ? 0xff : g < 0 ? 0 : g;
        b = b > 0xff ? 0xff : b < 0 ? 0 : b;

        // Adjust preserving alpha channel
        return alpha | (r << 16) | (g << 8) | b;
    }
}
