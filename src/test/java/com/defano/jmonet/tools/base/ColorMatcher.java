package com.defano.jmonet.tools.base;

import org.mockito.ArgumentMatcher;

import java.awt.*;

public class ColorMatcher extends ArgumentMatcher<Color> {

    private final Color c1;
    private boolean ignoreAlpha = false;

    public ColorMatcher(Color c1) {
        this.c1 = c1;
    }

    public ColorMatcher ignoringAlpha() {
        this.ignoreAlpha = true;
        return this;
    }

    @Override
    public boolean matches(Object o) {

        if (o instanceof Color) {
            Color c2 = (Color) o;

            if (ignoreAlpha) {
                return c1.getRed() == c2.getRed() &&
                        c1.getGreen() == c2.getGreen() &&
                        c1.getBlue() == c2.getBlue();
            } else {
                return c1.getRed() == c2.getRed() &&
                        c1.getGreen() == c2.getGreen() &&
                        c1.getBlue() == c2.getBlue() &&
                        c1.getAlpha() == c2.getAlpha();
            }
        }

        return false;
    }
}
