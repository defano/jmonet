package com.defano.jmonet.algo.fill;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Default boundary function; fills all fully-transparent pixels.
 */
public class DefaultBoundaryFunction implements BoundaryFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldFillPixel(BufferedImage canvas, BufferedImage scratch, int x, int y) {
        Color canvasPixel = new Color(canvas.getRGB(x, y), true);
        Color scratchPixel = new Color(scratch.getRGB(x, y), true);
        return canvasPixel.getAlpha() == 0 && scratchPixel.getAlpha() == 0;
    }
}
