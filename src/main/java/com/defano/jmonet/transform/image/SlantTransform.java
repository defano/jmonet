package com.defano.jmonet.transform.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Vertically slants (shears) an image by a given angle and applies an x-translation to compensate for the change
 * in position implied by the slant (allows the vertical center-line to remain constant, if desired).
 */
public class SlantTransform implements ImageTransform {

    private final double theta;
    private final int xTranslation;

    /**
     * Creates a slant image transform.
     *
     * @param theta        The angle, in radians, to shear the image
     * @param xTranslation The number of pixels to translate the image, typically this is calculated by determining the
     *                     number of pixels left or right the image has been sheared (delta between top-left corner and
     *                     bottom-left corner), then dividing this value in half.
     */
    public SlantTransform(double theta, int xTranslation) {
        this.theta = theta;
        this.xTranslation = xTranslation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage source) {
        AffineTransform transform = AffineTransform.getTranslateInstance(xTranslation, 0);
        transform.shear(Math.tan(theta), 0);

        transform.translate(xTranslation, 0);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(source, null);
    }
}
