package com.defano.jmonet.tools.base;

import java.awt.*;

public class DefaultMarkPredicate implements MarkPredicate {

    @Override
    public boolean isMarked(Color pixel, Color eraseColor) {
        if (eraseColor == null) {
            return pixel.getAlpha() >= 128;
        } else {
            return eraseColor.getRed() != pixel.getRed() || eraseColor.getBlue() != pixel.getBlue() || eraseColor.getGreen() != pixel.getGreen();
        }
    }
}
