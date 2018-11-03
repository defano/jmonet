package com.defano.jmonet.algo.transform.image;

import java.awt.image.BufferedImage;

/**
 * A transform that can be applied to a {@link BufferedImage} as a whole, like rotate, flip, and scale operations.
 */
public interface ImageTransform {

    /**
     * Applies a transform operation to the given image, producing a new, transformed image. This method does not
     * destroy or otherwise mutate the source image.
     *
     * Note that this operation may return an image whose dimensions differ from the source image. See
     * {@link StaticImageTransform} for a transform which maintains source dimensions.
     *
     * @param source The source image to which the transform should be applied.
     * @return A new, transformed image.
     */
    BufferedImage apply(BufferedImage source);
}
