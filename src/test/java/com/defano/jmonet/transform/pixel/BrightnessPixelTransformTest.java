package com.defano.jmonet.transform.pixel;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BrightnessPixelTransformTest {

    @Test
    void testThatZeroDeltaLeavesColorUnchanged() {
        assertEquals(Color.black.getRGB(),
                new BrightnessPixelTransform(0).apply(Color.black.getRGB()));
    }

    @Test
    void testUnsaturatedTransform() {
        final int delta = 10;

        assertEquals(new Color(
                Color.black.getRed() + delta,
                Color.black.getGreen() + delta,
                Color.black.getBlue() + delta
        ).getRGB(), new BrightnessPixelTransform(delta).apply(Color.black.getRGB()));
    }

    @Test
    void testFullySaturatedTransform() {
        assertEquals(Color.white.getRGB(), new BrightnessPixelTransform(10).apply(Color.white.getRGB()));
    }

    @Test
    void testPartiallySaturatedTransform() {
        Color c = new Color(255, 100, 255, 20);
        final int delta = 20;

        assertEquals(new Color(
                c.getRed(),
                c.getGreen() + delta,
                c.getBlue(),
                c.getAlpha()
        ).getRGB(), new BrightnessPixelTransform(delta).apply(c.getRGB()));
    }
}