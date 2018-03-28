package com.defano.jmonet.algo.transform;

public class BrightnessPixelTransform implements PixelTransform {

    private final int delta;

    public BrightnessPixelTransform(int delta) {
        this.delta = delta;
    }

    @Override
    public int transformPixel(int x, int y, int rgb) {
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
