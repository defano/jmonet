package com.defano.jmonet.algo.transform.image;

import com.defano.jmonet.algo.fill.BoundaryFunction;
import com.defano.jmonet.algo.fill.FillFunction;
import com.defano.jmonet.algo.transform.ImageTransform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class FloodFillTransform implements ImageTransform {

    private final BoundaryFunction boundary;
    private final FillFunction fill;
    private final Point origin;
    private final Paint fillPaint;

    public FloodFillTransform(Paint fillPaint, Point origin, FillFunction fill, BoundaryFunction boundary) {
        this.fillPaint = fillPaint;
        this.origin = origin;
        this.fill = fill;
        this.boundary = boundary;
    }

    @Override
    public BufferedImage apply(BufferedImage source) {

        Rectangle bounds = new Rectangle(0, 0, source.getWidth(), source.getHeight());
        BufferedImage transformed = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final Stack<Point> fillPixels = new Stack<>();

        // Start by filling origin pixel (i.e., clicked pixel)
        fillPixels.push(origin);

        while (!fillPixels.isEmpty()) {
            final Point thisPixel = fillPixels.pop();
            fill.fill(transformed, thisPixel, fillPaint);

            final Point right = new Point(thisPixel.x + 1, thisPixel.y);
            final Point left = new Point(thisPixel.x - 1, thisPixel.y);
            final Point down = new Point(thisPixel.x, thisPixel.y + 1);
            final Point up = new Point(thisPixel.x, thisPixel.y - 1);

            if (bounds.contains(right) && boundary.shouldFillPixel(source, transformed, right)) {
                fillPixels.push(right);
            }

            if (bounds.contains(left) && boundary.shouldFillPixel(source, transformed, left)) {
                fillPixels.push(left);
            }

            if (bounds.contains(down) && boundary.shouldFillPixel(source, transformed, down)) {
                fillPixels.push(down);
            }

            if (bounds.contains(up) && boundary.shouldFillPixel(source, transformed, up)) {
                fillPixels.push(up);
            }
        }

        return transformed;
    }
}
