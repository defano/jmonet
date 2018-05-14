package com.defano.jmonet.algo;

import com.defano.jmonet.canvas.PaintCanvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * A queue implementation of the flood-fill (seed fill) algorithm as used by {@link com.defano.jmonet.tools.FillTool}.
 */
public class FloodFill {

    /**
     * Flood-fills a canvas with paint, starting from a given coordinate.
     *
     * @param canvas The canvas to be flood-fill.
     * @param fillPaint The paint or texture with which to fill
     * @param x The x-coordinate where the flood-fill should begin
     * @param y The y-coordinate where the flood-fill should begin
     * @param fill A fill function to apply paint to the canvas
     * @param boundary A boundary function to determine "boundary pixels" on the canvas
     */
    public static void floodFill(PaintCanvas canvas, Paint fillPaint, int x, int y, FillFunction fill, BoundaryFunction boundary) {
        final Stack<Point> fillPixels = new Stack<>();

        BufferedImage canvasImage = canvas.getCanvasImage();
        BufferedImage scratchImage = canvas.getScratch().getAddScratch();
        Rectangle bounds = canvas.getBounds();

        // Start by filling clicked pixel
        fillPixels.push(new Point(x, y));

        while (!fillPixels.isEmpty()) {
            final Point thisPixel = fillPixels.pop();
            fill.fill(scratchImage, thisPixel, fillPaint);

            final Point right = new Point(thisPixel.x + 1, thisPixel.y);
            final Point left = new Point(thisPixel.x - 1, thisPixel.y);
            final Point down = new Point(thisPixel.x, thisPixel.y + 1);
            final Point up = new Point(thisPixel.x, thisPixel.y - 1);

            if (bounds.contains(right) && boundary.shouldFillPixel(canvasImage, scratchImage, right)) {
                fillPixels.push(right);
            }

            if (bounds.contains(left) && boundary.shouldFillPixel(canvasImage, scratchImage, left)) {
                fillPixels.push(left);
            }

            if (bounds.contains(down) && boundary.shouldFillPixel(canvasImage, scratchImage, down)) {
                fillPixels.push(down);
            }

            if (bounds.contains(up) && boundary.shouldFillPixel(canvasImage, scratchImage, up)) {
                fillPixels.push(up);
            }
        }
    }
}
