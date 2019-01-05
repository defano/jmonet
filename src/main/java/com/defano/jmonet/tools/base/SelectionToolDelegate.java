package com.defano.jmonet.tools.base;

import java.awt.*;

public interface SelectionToolDelegate {
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
