package com.defano.jmonet.tools.selection;

import com.defano.jmonet.algo.transform.Transform;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Represents a selection that can be transformed using operations that affect both the pixels of the selected image
 * (i.e., change of brightness or opacity) and/or the selection's shape and location (i.e., flip, rotate).
 */
public interface TransformableSelection extends TransformableImageSelection {

    /**
     * Rotates the image 90 degrees counter-clockwise.
     */
    default void rotateLeft() {
        if (hasSelection()) {
            int width = getSelectedImage().getWidth();
            int height = getSelectedImage().getHeight();

            applyTransform(Transform.rotateLeft(width, height));
            translateSelection((width - height) / 2, -(width - height) / 2);
            redrawSelection(true);
        }
    }

    /**
     * Rotates the image 90 degrees clockwise.
     */
    default void rotateRight() {
        if (hasSelection()) {
            int width = getSelectedImage().getWidth();
            int height = getSelectedImage().getHeight();

            applyTransform(Transform.rotateRight(width, height));
            translateSelection((width - height) / 2, -(width - height) / 2);
            redrawSelection(true);
        }
    }

    /**
     * Flips or mirrors the image along its vertical axis. That is, all pixels on the left side of the image
     * will move the right side and vice versa.
     */
    default void flipHorizontal() {
        if (hasSelection()) {
            int width = getSelectedImage().getWidth();
            applyTransform(Transform.flipHorizontalTransform(width));
            redrawSelection(true);
        }
    }

    /**
     * Flips or mirrors the image along its horizontal axis. That is, all pixels on the top of the image will
     * move to the bottom and vice versa.
     */
    default void flipVertical() {
        if (hasSelection()) {
            int height = getSelectedImage().getHeight();
            applyTransform(Transform.flipVerticalTransform(height));
            redrawSelection(true);
        }
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
        }
    }
}
