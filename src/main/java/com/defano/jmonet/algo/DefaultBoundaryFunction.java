package com.defano.jmonet.algo;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Default boundary function; fills all fully-transparent pixels.
 */
public class DefaultBoundaryFunction implements BoundaryFunction {
    @Override
    public boolean shouldFillPixel(BufferedImage canvas, BufferedImage scratch, Point point) {
        Color canvasPixel = new Color(canvas.getRGB(point.x, point.y), true);
        Color scratchPixel = new Color(scratch.getRGB(point.x, point.y), true);
        return canvasPixel.getAlpha() == 0 && scratchPixel.getAlpha() == 0;
    }
}
