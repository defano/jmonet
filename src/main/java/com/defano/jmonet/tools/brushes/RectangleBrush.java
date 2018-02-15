package com.defano.jmonet.tools.brushes;

import java.awt.geom.Rectangle2D;

/**
 * A rectangular-shaped brush. Produces a stroke where every point on the stroked path is "stamped" with a filled
 * rectangle.
 */
public class RectangleBrush extends ShapeBrush {

    /**
     * Produces a rectangular-shaped brush.
     * @param width The width of the brush
     * @param height The height of the brush
     */
    public RectangleBrush(int width, int height) {
        super(new Rectangle2D.Float(-width / 2.0f, -height / 2.0f, width, height), false);
    }
}
