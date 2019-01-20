package com.defano.jmonet.transform.pixel;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class InvertPixelTransformTest {

    @Test
    void testInvertWithoutAlpha() {
        assertEquals(Color.white.getRGB(), new InvertPixelTransform().apply(Color.black.getRGB()));
    }

    @Test
    void testInvertWithAlpha() {
        Color transBlack = new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 20);
        Color transWhite = new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 20);

        assertEquals(transWhite.getRGB(), new InvertPixelTransform().apply(transBlack.getRGB()));
    }
}