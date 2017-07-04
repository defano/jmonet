package com.defano.jmonet.tools.base;

import java.awt.geom.AffineTransform;

/**
 * Defines an object that can perform a well-defined set of "static" transforms on an image. A static transform is
 * one that does not require a user interface (like those provided by {@link com.defano.jmonet.tools.RotateTool} or
 * {@link com.defano.jmonet.tools.ProjectionTool}.
 */
public interface StaticTransformer {

    /**
     * Rotates the image 90 degrees counter-clockwise.
     */
    void rotateLeft();

    /**
     * Rotates the image 90 degrees clockwise.
     */
    void rotateRight();

    /**
     * Flips or mirrors the image along its vertical axis. That is, all pixels on the left side of the image
     * will move the right side and vice versa.
     */
    void flipHorizontal();

    /**
     * Flips or mirrors the image along its horizontal axis. That is, all pixels on the top of the image will
     * move to the bottom and vice versa.
     */
    void flipVertical();

    /**
     * Adjusts the brightness of the image by the amount specified in delta while preserving the image's alpha
     * transparency.
     *
     * @param delta An amount by which brightness should be adjusted, -256 to +256. Negative numbers darken an image,
     *              positive numbers brighten it.
     */
    void adjustBrightness(int delta);

    /**
     * Adjusts the alpha transparency of the image while maintain hue, saturation and brightness.
     *
     * @param delta An amount by which transparency should be adjusted -256 to +256; Negative numbers make the image
     *              more transparent, positive numbers make the image more opaque.
     */
    void adjustTransparency(int delta);

    /**
     * Inverts the color of the image while preserving the alpha transparency.
     */
    void invert();

    /**
     * Applies an AffineTransform the current image.
     * @param transform The transform to apply.
     */
    void applyTransform(AffineTransform transform);
}
