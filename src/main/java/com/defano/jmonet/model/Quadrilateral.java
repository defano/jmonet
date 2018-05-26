package com.defano.jmonet.model;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * A four-sided, simple polygon (quadrilateral) with no restrictions on the length, angle or parallelism, except that
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
    default int getWidth() {
        return getRight() - getLeft();
    }

    /**
     * The height of the quadrilateral.
     * @return The height.
     */
    default int getHeight() {
        return getBottom() - getTop();
    }

    /**
     * The left-most point of the quadrilateral.
     * @return The left-most coordinate.
     */
    default int getLeft() {
        return (int) Math.min(getLeftLine().getX1(), getLeftLine().getX2());
    }

    /**
     * The top-most point of the quadrilateral.
     * @return The top-most coordinate.
     */
    default int getTop() {
        return (int) Math.min(getTopLine().getY1(), getTopLine().getY2());
    }

    /**
     * The bottom-most point of the quadrilateral.
     * @return The bottom-most coordinate.
     */
    default int getBottom() {
        return (int) Math.max(getBottomLine().getY1(), getBottomLine().getY2());
    }

    /**
     * The right-most point of the quadrilateral.
     * @return The right-most coordinate.
     */
    default int getRight() {
        return (int) Math.max(getRightLine().getX1(), getRightLine().getX2());
    }

    /**
     * Gets the line formed by {@link #getTopLeft()} and {@link #getTopRight()}.
     *
     * @return The line between these points
     */
    default Line2D getTopLine() {
        return new Line2D.Double(getTopLeft(), getTopRight());
    }

    /**
     * Gets the line formed by {@link #getBottomLeft()} and {@link #getBottomRight()}.
     *
     * @return The line between these points
     */
    default Line2D getBottomLine() {
        return new Line2D.Double(getBottomLeft(), getBottomRight());
    }

    /**
     * Gets the line formed by {@link #getTopLeft()} and {@link #getBottomLeft()}.
     *
     * @return The line between these points
     */
    default Line2D getLeftLine() {
        return new Line2D.Double(getTopLeft(), getBottomLeft());
    }

    /**
     * Gets the line formed by {@link #getTopRight()} and {@link #getBottomRight()}.
     *
     * @return The line between these points
     */
    default Line2D getRightLine() {
        return new Line2D.Double(getTopRight(), getBottomRight());
    }

    /**
     * Gets the diagonal line formed by {@link #getTopLeft()} and {@link #getBottomRight()}.
     *
     * @return The line between these points
     */
    default Line2D getTopLeftBottomRightDiagonal() {
        return new Line2D.Double(getTopLeft(), getBottomRight());
    }

    /**
     * Gets the diagonal line formed by {@link #getBottomRight()} and {@link #getTopLeft()}.
     *
     * @return The line between these points
     */
    default Line2D getBottomRightTopLeftDiagonal() {
        return new Line2D.Double(getBottomRight(), getTopLeft());
    }

    /**
     * Gets the diagonal line formed by {@link #getBottomLeft()} and {@link #getTopRight()}.
     *
     * @return The line between these points
     */
    default Line2D getBottomLeftTopRightDiagonal() {
        return new Line2D.Double(getBottomLeft(), getTopRight());
    }

    /**
     * Gets the diagonal line formed by {@link #getTopRight()} and {@link #getBottomLeft()}.
     *
     * @return The line between these points
     */
    default Line2D getTopRightBottomLeftDiagonal() {
        return new Line2D.Double(getTopRight(), getBottomLeft());
    }

    /**
     * Gets a four-element array of {@link Point} representing the top-left, top-right, bottom-right and bottom-left
     * corners of the quadrilateral, in that order.
     *
     * @return The four corners of this shape.
     */
    default Point[] getCorners() {
        return new Point[]{getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft()};
    }

    /**
     * Gets the center-point of the bounds of this shape.
     *
     * @return The center point.
     */
    default Point getCenter() {
        return new Point(getShape().getBounds().width / 2, getShape().getBounds().height / 2);
    }


    /**
     * Gets a {@link Shape} from this Quadrilateral.
     *
     * @return The shape.
     */
    default Shape getShape() {
        GeneralPath path = new GeneralPath();
        path.moveTo(getTopLeft().getX(), getTopLeft().getY());
        path.lineTo(getTopRight().getX(), getTopRight().getY());
        path.lineTo(getBottomRight().getX(), getBottomRight().getY());
        path.lineTo(getBottomLeft().getX(), getBottomLeft().getY());
        path.closePath();

        return path;
    }

}
