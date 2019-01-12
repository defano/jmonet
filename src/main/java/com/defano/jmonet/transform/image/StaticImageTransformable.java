package com.defano.jmonet.transform.image;

/**
 * An image-containing object that can be transformed by a {@link StaticImageTransform}; that is, all the pixels of the
 * image may be transformed, but the dimensions or bounds of the image can not.
 */
public interface StaticImageTransformable {

    /**
     * Performs a transformation on the image that does not effect the dimensions, bounds or location of the
     * affected image.
     *
     * @param transform The transform to perform.
     */
    void transform(StaticImageTransform transform);

    /**
     * Applies a {@link PixelTransform} on all the pixels of the image.
     * @param transform The pixel transform to apply
     */
    void transform(PixelTransform transform);
}
