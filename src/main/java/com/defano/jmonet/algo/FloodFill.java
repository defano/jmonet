package com.defano.jmonet.algo;

import java.awt.*;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * A queue implementation of the flood-fill (seed fill) algorithm as used by {@link com.defano.jmonet.tools.FillTool}.
 */
public class FloodFill {

    public static void floodFill(int x, int y, Rectangle bounds, FillFunction fill, Predicate<Point> boundaryFunction) {
        final Stack<Point> fillPixels = new Stack<>();
        fillPixels.push(new Point(x, y));

        while (!fillPixels.isEmpty()) {
            final Point thisPixel = fillPixels.pop();
            fill.fill(thisPixel);

            final Point right = new Point(thisPixel.x + 1, thisPixel.y);
            final Point left = new Point(thisPixel.x - 1, thisPixel.y);
            final Point down = new Point(thisPixel.x, thisPixel.y + 1);
            final Point up = new Point(thisPixel.x, thisPixel.y - 1);

            if (bounds.contains(right) && boundaryFunction.test(right)) {
                fillPixels.push(right);
            }

            if (bounds.contains(left) && boundaryFunction.test(left)) {
                fillPixels.push(left);
            }

            if (bounds.contains(down) && boundaryFunction.test(down)) {
                fillPixels.push(down);
            }

            if (bounds.contains(up) && boundaryFunction.test(up)) {
                fillPixels.push(up);
            }
        }
    }
}
