package com.defano.jmonet.algo.transform.affine;

import java.awt.geom.AffineTransform;

/**
 * An {@link AffineTransform} that performs a 90-degree counter-clockwise rotation around an object's center point.
 */
public class RotateRightTransform extends AffineTransform {

    /**
     * Creates a rotate-right transform.
     *
     * @param width  The width of the shape or image to be rotated
     * @param height The height of the shape or image to be rotated
     */
    public RotateRightTransform(int width, int height) {
        setToTranslation(height / 2.0, width / 2.0);
        quadrantRotate(1);
        translate(-width / 2.0, -height / 2.0);
    }

}
