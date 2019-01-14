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
@SuppressWarnings("unused")
public class FloodFillTransform implements ImageTransform {

    private BoundaryFunction boundaryFunction;
    private FillFunction fill;
    private Point origin;
    private Paint fillPaint;

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

            if (bounds.contains(thisPixelX + 1, thisPixelY) && !boundaryFunction.isBoundary(source, transformed, thisPixelX + 1, thisPixelY)) {
                fillPixels.add(new Point(thisPixelX + 1, thisPixelY));
            }

            if (bounds.contains(thisPixelX - 1, thisPixelY) && !boundaryFunction.isBoundary(source, transformed, thisPixelX - 1, thisPixelY)) {
                fillPixels.add(new Point(thisPixelX - 1, thisPixelY));
            }

            if (bounds.contains(thisPixelX, thisPixelY + 1) && !boundaryFunction.isBoundary(source, transformed, thisPixelX, thisPixelY + 1)) {
                fillPixels.add(new Point(thisPixelX, thisPixelY + 1));
            }

            if (bounds.contains(thisPixelX, thisPixelY - 1) && !boundaryFunction.isBoundary(source, transformed, thisPixelX, thisPixelY - 1)) {
                fillPixels.add(new Point(thisPixelX, thisPixelY - 1));
            }
        }

        return transformed;
    }

    public BoundaryFunction getBoundaryFunction() {
        return boundaryFunction;
    }

    public void setBoundaryFunction(BoundaryFunction boundaryFunction) {
        this.boundaryFunction = boundaryFunction;
    }

    public FillFunction getFill() {
        return fill;
    }

    public void setFill(FillFunction fill) {
        this.fill = fill;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Paint getFillPaint() {
        return fillPaint;
    }

    public void setFillPaint(Paint fillPaint) {
        this.fillPaint = fillPaint;
    }
}
