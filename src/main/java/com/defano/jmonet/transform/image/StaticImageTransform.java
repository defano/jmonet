package com.defano.jmonet.transform.image;

import java.awt.image.BufferedImage;

/**
 * A transformation that can be applied to a {@link BufferedImage} as a whole, but which does not modify the dimensions
 * of the image (like color reduction or fill).
 *
 * See {@link ImageTransform} for a transform that can modify image dimensions.
 */
public interface StaticImageTransform extends ImageTransform {

    /**
     * Applies a transform operation to the given image, producing a new, transformed image of the same dimensions as
     * the source image. This method does not destroy or otherwise mutate the source image.
     *
     * @param source The source image to which the transform should be applied.
     * @return A new, transformed image.
     */
    BufferedImage apply(BufferedImage source);
}
