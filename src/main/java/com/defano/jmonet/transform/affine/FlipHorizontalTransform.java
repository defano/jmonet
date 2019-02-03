package com.defano.jmonet.transform.affine;

import java.awt.geom.AffineTransform;

/**
 * An {@link AffineTransform} that performs a horizontal mirroring of a shape or image about its vertical center-line.
 */
public class FlipHorizontalTransform extends AffineTransform {

    /**
     * Creates a horizontal flip transformation.
     *
     * @param width The width of the image/shape being flipped.
     */
    public FlipHorizontalTransform(int width) {
        setToScale(-1, 1);
        translate(-width, 0);
    }
}
