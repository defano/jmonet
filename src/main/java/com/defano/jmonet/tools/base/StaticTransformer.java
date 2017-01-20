package com.defano.jmonet.tools.base;

public interface StaticTransformer {

    /**
     * Rotates the selected image 90 degrees counter-clockwise.
     */
    void rotateLeft();

    /**
     * Rotates the selected image 90 degrees clockwise.
     */
    void rotateRight();

    /**
     * Flips or mirrors the selected image along its vertical axis. That is, all pixels on the left side of the image
     * will move the right side and vice versa.
     */
    void flipHorizontal();

    /**
     * Flips or mirrors the selected image along its horizontal axis. That is, all pixels on the top of the image will
     * move to the bottom and vice versa.
     */
    void flipVertical();
}
