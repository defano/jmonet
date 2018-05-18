package com.defano.jmonet.algo.transform.pixel;

import com.defano.jmonet.algo.transform.PixelTransform;

import java.awt.*;

public class TransparencyPixelTransform implements PixelTransform {

    private final int delta;

    public TransparencyPixelTransform(int delta) {
        this.delta = delta;
    }

    @Override
    public int apply(int x, int y, int rgb) {
        Color color = new Color(rgb, true);

        int alpha = color.getAlpha() + delta;
        alpha = alpha > 0xff ? 0xff : alpha < 0 ? 0 : alpha;

        // Adjust alpha preserving color channel
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
}
