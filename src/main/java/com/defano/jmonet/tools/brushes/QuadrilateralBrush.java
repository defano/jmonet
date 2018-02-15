package com.defano.jmonet.tools.brushes;

import com.defano.jmonet.model.Quadrilateral;

/**
 * A quadrilateral-shaped brush. Produces a stroke where every point on the stroked path is "stamped" with a filled
 * quadrilateral (points are user-defined).
 */
public class QuadrilateralBrush extends ShapeBrush {

    /**
     * Produces a quadrilateral-shaped brush.
     * @param quadrilateral The shape of the brush
     */
    public QuadrilateralBrush(Quadrilateral quadrilateral) {
        super(quadrilateral.getShape(), false);
    }
}
