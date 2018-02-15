package com.defano.jmonet.tools.brushes;

/**
 * A circular-shaped brush. Produces a stroke where every point on the stroked path is "stamped" with a filled circle.
 */
public class CircleBrush extends OvalBrush {

    /**
     * Produces a circle-shaped brush.
     * @param size The size (diameter) of the brush
     */
    public CircleBrush(int size) {
        super(size, size);
    }
}
