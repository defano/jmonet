package com.defano.jmonet.tools.base;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface MutableSelection {

    /**
     * Reset the selection boundary to its initial, no-selection state. {@link #getSelectionOutline()} should return
     * null following a selection reset, but prior to defining a new selection.
     */
    void resetSelection();

    /**
     * Determines the location (top-left coordinate) of the selection outline ({@link #getSelectionOutline()} on
     * the canvas.
     *
     * @return Get the top-left coordinate of the selection boundary
     */
    Point getSelectionLocation();

    /**
     * Gets the bounding selection shape of the current selection outline, i.e., the path on which "marching ants"
     * will be drawn.
     *
     * @return The selection outline
     */
    Shape getSelectionOutline();

    /**
     * Specifies the selection rectangle (bounds) of the current selection, i.e., the rectangular path on which
     * "marching ants" will be drawn.
     *
     * @param bounds The new selection bounds.
     */
    void setSelectionOutline(Rectangle bounds);

    /**
     * Determines if the user has an active selection. A selection exists only after the user has completed defining
     * a closed, selection outline.
     *
     * @return True is a selection exists, false otherwise.
     */
    boolean hasSelection();

    /**
     * Marks the selection as having been mutated (either by transformation or movement).
     */
    void setDirty();

    /**
     * Replaces the current selected image with the given image. It is the caller's responsibility to mask the given
     * image to assure it does not exceed the selection bounds.
     * @param image The image with which to replace the selection.
     */
    void setSelectedImage(BufferedImage image);

    /**
     * Gets the image represented by the selection.
     *
     * Note that a user can select and modify a non-rectangular selection, but the selected image returned by this
     * method will always be a rectangle whose bounds are the smallest rectangle that can fit in the selected shape.
     * Any pixels outside the user's selection shape will be fully transparent pixels in the returned image.
     *
     * @return The selected image
     */
    BufferedImage getSelectedImage();

    /**
     * Invoked to indicate that the selection has moved on the canvas. The selection shape's coordinates should be
     * translated by the given amount.
     *
     * @param xDelta Number of pixels to move horizontally.
     * @param yDelta Number of pixels to move vertically.
     */
    void adjustSelectionBounds(int xDelta, int yDelta);

    /**
     * Clears the scratch buffer then draws the selected image and selection outline ("marching ants") onto it.
     */
    void redrawSelection();

}
