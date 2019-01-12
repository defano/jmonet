package com.defano.jmonet.transform.image;

import com.defano.jmonet.tools.attributes.BoundaryFunction;
import com.defano.jmonet.tools.attributes.FillFunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Performs a "flood fill" (sometimes called "seed fill" or "spill paint") of the image with a provided paint or
 * texture. Terrible performance on very large fill surfaces.
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

        ArrayList<Point> fillPixels = new ArrayList<>();

        Rectangle bounds = new Rectangle(0, 0, source.getWidth(), source.getHeight());
        BufferedImage transformed = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Start by filling origin pixel (i.e., clicked pixel)
        fillPixels.add(origin);

        while (!fillPixels.isEmpty()) {

            Point popped = fillPixels.remove(fillPixels.size() - 1);
            int thisPixelX = popped.x;
            int thisPixelY = popped.y;

            fill.fill(transformed, thisPixelX, thisPixelY, fillPaint);

            if (bounds.contains(thisPixelX + 1, thisPixelY) && !boundary.isBoundary(source, transformed, thisPixelX + 1, thisPixelY)) {
                fillPixels.add(new Point(thisPixelX + 1, thisPixelY));
            }

            if (bounds.contains(thisPixelX - 1, thisPixelY) && !boundary.isBoundary(source, transformed, thisPixelX - 1, thisPixelY)) {
                fillPixels.add(new Point(thisPixelX - 1, thisPixelY));
            }

            if (bounds.contains(thisPixelX, thisPixelY + 1) && !boundary.isBoundary(source, transformed, thisPixelX, thisPixelY + 1)) {
                fillPixels.add(new Point(thisPixelX, thisPixelY + 1));
            }

            if (bounds.contains(thisPixelX, thisPixelY - 1) && !boundary.isBoundary(source, transformed, thisPixelX, thisPixelY - 1)) {
                fillPixels.add(new Point(thisPixelX, thisPixelY - 1));
            }
        }

        return transformed;
    }
}
