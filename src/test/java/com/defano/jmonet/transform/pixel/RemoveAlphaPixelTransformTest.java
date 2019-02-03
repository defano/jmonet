package com.defano.jmonet.transform.pixel;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoveAlphaPixelTransformTest {

    @Test
    void testThatTranslucentColorIsMadeTransparent() {
        Color orig = new Color(Color.ORANGE.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 127);
        Color transformed = new Color(orig.getRed(), orig.getGreen(), orig.getBlue(), 0);

        assertEquals(transformed.getRGB(), new RemoveAlphaPixelTransform(true).apply(orig.getRGB()));
    }

    @Test
    void testThatTranslucentColorIsMadeOpaque() {
        Color orig = new Color(Color.ORANGE.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 127);
        Color transformed = new Color(orig.getRed(), orig.getGreen(), orig.getBlue(), 255);

        assertEquals(transformed.getRGB(), new RemoveAlphaPixelTransform(false).apply(orig.getRGB()));
    }

    @Test
    void testThatOpaqueColorIsUnchanged() {
        Color orig = new Color(Color.ORANGE.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 255);

        assertEquals(orig.getRGB(), new RemoveAlphaPixelTransform(false).apply(orig.getRGB()));
        assertEquals(orig.getRGB(), new RemoveAlphaPixelTransform(true).apply(orig.getRGB()));
    }

    @Test
    void testThatTransparentColorIsUnchanged() {
        Color orig = new Color(Color.ORANGE.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 0);

        assertEquals(orig.getRGB(), new RemoveAlphaPixelTransform(false).apply(orig.getRGB()));
        assertEquals(orig.getRGB(), new RemoveAlphaPixelTransform(true).apply(orig.getRGB()));
    }

}