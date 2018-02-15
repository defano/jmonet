package com.defano.jmonet.tools.brushes;

import java.awt.geom.Ellipse2D;

/**
 * An oval-shaped brush. Produces a stroke where every point on the stroked path is "stamped" with a filled oval.
 */
public class OvalBrush extends ShapeBrush {

    /**
     * Creates an oval-shaped brush.
     * @param width The width of the brush.
     * @param height The height of the brush.
     */
    public OvalBrush(int width, int height) {
        super(new Ellipse2D.Float(-width / 2.0f, -height / 2.0f, width, height), false);
    }

}
