package com.defano.jmonet.tools.brushes;

import java.awt.geom.RoundRectangle2D;

/**
 * A round rectangle-shaped brush. Produces a stroke where every point on the stroked path is "stamped" with a filled
 * round rectangle.
 */
public class RoundRectangleBrush extends ShapeBrush {

    /**
     * Creates a round rectangle-shaped brush.
     * @param width The width of the round rectangle
     * @param height The height of the round rectangle
     * @param arc The height and width of the arc used to round the corners
     */
    public RoundRectangleBrush(int width, int height, int arc) {
        super(new RoundRectangle2D.Float(-width / 2.0f, -height / 2.0f, width, height, arc, arc), false);
    }
}
