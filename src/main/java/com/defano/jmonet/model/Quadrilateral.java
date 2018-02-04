package com.defano.jmonet.model;

import java.awt.*;

/**
 * A four-sided, simple polygon (quadrilateral) with no restrictions on the length, angle or parallelness, except that
 * the shape's edges cannot cross each other (self-intersecting quadrilaterals are disallowed).
 */
public interface Quadrilateral {

    /**
     * Gets the top-left corner of this quadrilateral.
     * @return The requested corner point.
     */
    Point getTopLeft();

    /**
     * Gets the top-right corner of this quadrilateral.
     * @return The requested corner point.
     */
    Point getTopRight();

    /**
     * Gets the bottom-left corner of this quadrilateral.
     * @return The requested corner point.
     */
    Point getBottomLeft();

    /**
     * Gets the bottom-right corner of this quadrilateral.
     * @return The requested corner point.
     */
    Point getBottomRight();

    /**
     * The width of the quadrilateral.
     * @return The width.
     */
    int getWidth();

    /**
     * The height of the quadrilateral.
     * @return The height.
     */
    int getHeight();

    /**
     * The left-most point of the quadrilateral.
     * @return The left-most coordinate.
     */
    int getLeft();

    /**
     * The top-most point of the quadrilateral.
     * @return The top-most coordinate.
     */
    int getTop();

    /**
     * The bottom-most point of the quadrilateral.
     * @return The bottom-most coordinate.
     */
    int getBottom();

    /**
     * The right-most point of the quadrilateral.
     * @return The right-most coordinate.
     */
    int getRight();

}
