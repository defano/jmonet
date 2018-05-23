package com.defano.jmonet.algo.transform.image;

import com.defano.jmonet.algo.fill.BoundaryFunction;
import com.defano.jmonet.algo.fill.FillFunction;
import com.defano.jmonet.algo.transform.ImageTransform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * Performs a "flood fill", "seed fill" (sometimes called "spill paint") of the image with a provided paint or texture.
 *
 * Given an origin point in the image, this algorithm iteratively paints every adjacent pixel with the given color or
 * texture until it reaches a boundary pixel.
 */
public class FloodFillTransform implements ImageTransform {

    private final BoundaryFunction boundary;
    private final FillFunction fill;
    private final Point origin;
    private final Paint fillPaint;

    /**
     * Creates a flood-fill transform.
     *
     * @param fillPaint The color or texture to flood-fill the image with
     * @param origin The seed or origin point in the image where the flooding should begin
     * @param fill A function that applies the fill paint to pixels identified for painting
     * @param boundary A function that determines which pixels in the image "enclose" the paint
     */
    public FloodFillTransform(Paint fillPaint, Point origin, FillFunction fill, BoundaryFunction boundary) {
        this.fillPaint = fillPaint;
        this.origin = origin;
        this.fill = fill;
        this.boundary = boundary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage source) {

        Rectangle bounds = new Rectangle(0, 0, source.getWidth(), source.getHeight());
        BufferedImage transformed = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int depth = 0;
        final int[] fillPixelsX = new int[source.getWidth() * source.getHeight()];
        final int[] fillPixelsY = new int[source.getHeight() * source.getHeight()];

        // Start by filling origin pixel (i.e., clicked pixel)
        fillPixelsX[depth] = origin.x;
        fillPixelsY[depth++] = origin.y;

        while (depth != 0) {
            int thisPixelX = fillPixelsX[--depth];
            int thisPixelY = fillPixelsY[depth];

            fill.fill(transformed, thisPixelX, thisPixelY, fillPaint);

            if (bounds.contains(thisPixelX + 1, thisPixelY) && boundary.shouldFillPixel(source, transformed, thisPixelX + 1, thisPixelY)) {
                fillPixelsX[depth] = thisPixelX + 1;
                fillPixelsY[depth++] = thisPixelY;
            }

            if (bounds.contains(thisPixelX - 1, thisPixelY) && boundary.shouldFillPixel(source, transformed, thisPixelX - 1, thisPixelY)) {
                fillPixelsX[depth] = thisPixelX - 1;
                fillPixelsY[depth++] = thisPixelY;
            }

            if (bounds.contains(thisPixelX, thisPixelY + 1) && boundary.shouldFillPixel(source, transformed, thisPixelX, thisPixelY + 1)) {
                fillPixelsX[depth] = thisPixelX;
                fillPixelsY[depth++] = thisPixelY + 1;
            }

            if (bounds.contains(thisPixelX, thisPixelY - 1) && boundary.shouldFillPixel(source, transformed, thisPixelX, thisPixelY - 1)) {
                fillPixelsX[depth] = thisPixelX;
                fillPixelsY[depth++] = thisPixelY - 1;
            }
        }

        return transformed;
    }
}
