package com.defano.jmonet.algo.transform;

import java.awt.*;

public class RemoveAlphaPixelTransform implements PixelTransform {

    private final boolean makeTransparent;

    public RemoveAlphaPixelTransform(boolean makeTransparent) {
        this.makeTransparent = makeTransparent;
    }

    @Override
    public int transformPixel(int x, int y, int rgb) {
        Color color = new Color(rgb, true);

        int alpha = color.getAlpha();
        if (alpha != 0 && alpha != 0xff) {
            alpha = makeTransparent ? 0x00 : 0xff;
        }

        // Adjust alpha preserving color channel
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
}
