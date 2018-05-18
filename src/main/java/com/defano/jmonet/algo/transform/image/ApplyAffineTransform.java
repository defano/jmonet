package com.defano.jmonet.algo.transform.image;

import com.defano.jmonet.algo.transform.ImageTransform;
import com.defano.jmonet.model.Interpolation;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Applies an {@link AffineTransform} to a given image.
 */
public class ApplyAffineTransform implements ImageTransform {

    private final AffineTransform transform;
    private final Interpolation interpolation;

    public ApplyAffineTransform(AffineTransform transform) {
        this(transform, Interpolation.BICUBIC);
    }

    public ApplyAffineTransform(AffineTransform transform, Interpolation interpolation) {
        this.transform = transform;
        this.interpolation = interpolation;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        return new AffineTransformOp(transform, toAffineTransformOp(interpolation)).filter(image, null);
    }

    private int toAffineTransformOp(Interpolation interpolation) {
        switch (interpolation) {
            case NONE:
                throw new IllegalArgumentException("Interpolation 'none' not supported.");
            case NEAREST_NEIGHBOR:
                return AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
            case BILINEAR:
                return AffineTransformOp.TYPE_BILINEAR;

            default:
                return AffineTransformOp.TYPE_BICUBIC;
        }
    }
}
