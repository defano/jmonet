package com.defano.jmonet.tools.base;

import com.defano.jmonet.algo.Transform;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Defines a series of "static" transforms that can be applied to a selection. A static transform is one that can
 * be applied directly, without user/mouse engagement (e.g., scale, slant, or projection).
 */
public interface TransformableSelection extends MutableSelection {

    /**
     * Rotates the image 90 degrees counter-clockwise.
     */
    default void rotateLeft() {
        int width = getSelectedImage().getWidth();
        int height = getSelectedImage().getHeight();

        applyTransform(Transform.rotateLeft(width, height));
    }

    /**
     * Rotates the image 90 degrees clockwise.
     */
    default void rotateRight() {
        int width = getSelectedImage().getWidth();
        int height = getSelectedImage().getHeight();

        applyTransform(Transform.rotateRight(width, height));
    }

    /**
     * Flips or mirrors the image along its vertical axis. That is, all pixels on the left side of the image
     * will move the right side and vice versa.
     */
    default void flipHorizontal() {
        int width = getSelectedImage().getWidth();

        applyTransform(Transform.flipHorizontalTransform(width));
    }

    /**
     * Flips or mirrors the image along its horizontal axis. That is, all pixels on the top of the image will
     * move to the bottom and vice versa.
     */
    default void flipVertical() {
        int height = getSelectedImage().getHeight();

        applyTransform(Transform.flipVerticalTransform(height));
    }

    /**
     * Adjusts the brightness of the image by the amount specified in delta while preserving the image's alpha
     * transparency.
     *
     * @param delta An amount by which brightness should be adjusted, -256 to +256. Negative numbers darken an image,
     *              positive numbers brighten it.
     */
    default void adjustBrightness(int delta) {
        Transform.adjustBrightness(getSelectedImage(), delta);
    }

    /**
     * Adjusts the alpha transparency of the image while maintain hue, saturation and brightness.
     *
     * @param delta An amount by which transparency should be adjusted -256 to +256; Negative numbers make the image
     *              more transparent, positive numbers make the image more opaque.
     */
    default void adjustTransparency(int delta) {
        Transform.adjustTransparency(getSelectedImage(), delta);
    }

    /**
     * Inverts the color of the image while preserving the alpha transparency.
     */
    default void invert() {
        Transform.invert(getSelectedImage());
    }

    /**
     * Applies an AffineTransform the current image.
     * @param transform The transform to apply.
     */
    default void applyTransform(AffineTransform transform) {
        if (hasSelection()) {
            setDirty();

            // Get the original location of the selection
            Point originalLocation = getSelectionLocation();

            // Transform the selected image
            setSelectedImage(Transform.transform(getSelectedImage(), transform));

            // Relocate the image to its original location
            Rectangle newBounds = getSelectedImage().getRaster().getBounds();
            newBounds.setLocation(originalLocation);
            setSelectionOutline(newBounds);

            redrawSelection();
        }
    }
}
