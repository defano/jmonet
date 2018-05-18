package com.defano.jmonet.algo.transform.affine;

import java.awt.geom.AffineTransform;

/**
 * An {@link AffineTransform} that performs a vertical mirroring of a shape or image about its horizontal center-line.
 */
public class FlipVerticalTransform extends AffineTransform {

    /**
     * Creates a vertical flip transform.
     *
     * @param height The height of the image/shape being flipped.
     */
    public FlipVerticalTransform(int height) {
        setToScale(1, -1);
        translate(0, -height);
    }
}
