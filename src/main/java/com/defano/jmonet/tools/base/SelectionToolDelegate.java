package com.defano.jmonet.tools.base;

import java.awt.*;

public interface SelectionToolDelegate {

    /**
     * Gets the current selection frame shape, or null, if there is no active selection.
     *
     * @return The shape of the active selection frame, or null if there is no selection.
     */
    Shape getSelectionFrame();

    /**
     * Resets the selection frame such that subsequent calls to {@link #getSelectionFrame()} return null.
     */
    void clearSelectionFrame();

    /**
     * Creates or replaces the selection frame with the given shape. Note that tools that do not support arbitrary
     * selection shapes (like the marquee tool) will use the bounds of the given shape instead.
     *
     * @param bounds The shape or bounds of the new selection frame.
     */
    void setSelectionFrame(Shape bounds);

    /**
     * Translates the location of the current selection frame, if one exists.
     *
     * @param xDelta The number of pixels to translate in the x-direction
     * @param yDelta The number of pixels to translate in the y-direction
     */
    void translateSelectionFrame(int xDelta, int yDelta);

    /**
     * Invoked to indicate that the user has defined a new point on the selection path.
     *
     * @param initialPoint   The first point defined by the user (i.e., where the mouse was initially pressed)
     * @param newPoint       A new point to append to the selection path (i.e., where the mouse is now)
     * @param isShiftKeyDown When true, indicates user is holding the shift key down
     */
    void addPointToSelectionFrame(Point initialPoint, Point newPoint, boolean isShiftKeyDown);

    /**
     * Invoked to indicate that the given point should be considered the last point in the selection path, and the
     * path shape should be closed.
     *
     * @param finalPoint The final point on the selection path.
     */
    void closeSelectionFrame(Point finalPoint);

}
