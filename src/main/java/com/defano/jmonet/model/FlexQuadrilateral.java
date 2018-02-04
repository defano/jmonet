package com.defano.jmonet.model;

import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * Models a mutable, four-sided, simple polygon (quadrilateral) with no restrictions on the length, angle or
 * parallelness, except that the shape's edges cannot cross each other (self-intersecting quadrilaterals are
 * disallowed).
 */
public class FlexQuadrilateral implements Quadrilateral {

    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;

    /**
     * Constructs a mutable quadrilateral whose corners appear in the given locations.
     *
     * Note that this constructor does not validate the corner arguments. Caller is responsible for assuring corners
     * do not produce a self-intersecting polygon.
     *
     * @param topLeft The top-left corner
     * @param topRight The top-right corner
     * @param bottomLeft The bottom-left corner
     * @param bottomRight The bottom-right corner
     */
    public FlexQuadrilateral(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Constructs a mutable quadrilateral whose corners are defined by the bounds (minimum bounding rectangle) of the
     * given shape.
     * @param fromShape The shape whose bounds should determine the corners of the new quadrilateral.
     */
    public FlexQuadrilateral(Shape fromShape) {
        Rectangle bounds = fromShape.getBounds();
        topLeft = new Point(bounds.x, bounds.y);
        topRight = new Point(bounds.x + bounds.width, bounds.y);
        bottomLeft = new Point(bounds.x, bounds.y + bounds.height);
        bottomRight = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getTopLeft() {
        return topLeft;
    }

    /**
     * Sets the top-left corner of this shape.
     * <p>
     * Has no affect if placing the corner in the requested location would produce a self-intersecting polygon.
     *
     * @param topLeft The requested location of the corner.
     */
    public void setTopLeft(Point topLeft) {
        if (topLeft.x < getRight() &&
                Geometry.isAbove(getBottomLeftTopRightDiagonal(), topLeft) &&
                Geometry.isAbove(getBottomLine(), topLeft) &&
                Geometry.isBelow(getRightLine(), topLeft)) {
            this.topLeft = topLeft;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getTopRight() {
        return topRight;
    }

    /**
     * Sets the top-right corner of this shape.
     * <p>
     * Has no affect if placing the corner in the requested location would produce a self-intersecting polygon.
     *
     * @param topRight The requested location of the corner.
     */
    public void setTopRight(Point topRight) {
        if (topRight.x > getLeft() && topRight.y < getBottom() &&
                Geometry.isAbove(getTopLeftBottomRightDiagonal(), topRight) &&
                Geometry.isAbove(getLeftLine(), topRight) &&
                Geometry.isAbove(getBottomLine(), topRight)) {
            this.topRight = topRight;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBottomLeft() {
        return bottomLeft;
    }

    /**
     * Sets the bottom-left corner of this shape.
     * <p>
     * Has no affect if placing the corner in the requested location would produce a self-intersecting polygon.
     *
     * @param bottomLeft The requested location of the corner.
     */
    public void setBottomLeft(Point bottomLeft) {
        if (bottomLeft.x < getRight() && bottomLeft.y > getTop() &&
                Geometry.isBelow(getTopLeftBottomRightDiagonal(), bottomLeft) &&
                Geometry.isBelow(getRightLine(), bottomLeft) &&
                Geometry.isBelow(getTopLine(), bottomLeft)) {
            this.bottomLeft = bottomLeft;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBottomRight() {
        return bottomRight;
    }

    /**
     * Sets the bottom-right corner of this shape.
     * <p>
     * Has no affect if placing the corner in the requested location would produce a self-intersecting polygon.
     *
     * @param bottomRight The requested location of the corner.
     */
    public void setBottomRight(Point bottomRight) {
        if (bottomRight.x > getLeft() && bottomRight.y > getTop() &&
                Geometry.isBelow(getBottomLeftTopRightDiagonal(), bottomRight) &&
                Geometry.isAbove(getLeftLine(), bottomRight) &&
                Geometry.isBelow(getTopLine(), bottomRight)) {
            this.bottomRight = bottomRight;
        }
    }

    /**
     * Gets the line formed by {@link #getTopLeft()} and {@link #getTopRight()}.
     *
     * @return The line between these points
     */
    public Line2D getTopLine() {
        return new Line2D.Double(getTopLeft(), getTopRight());
    }

    /**
     * Gets the line formed by {@link #getBottomLeft()} and {@link #getBottomRight()}.
     *
     * @return The line between these points
     */
    public Line2D getBottomLine() {
        return new Line2D.Double(getBottomLeft(), getBottomRight());
    }

    /**
     * Gets the line formed by {@link #getTopLeft()} and {@link #getBottomLeft()}.
     *
     * @return The line between these points
     */
    public Line2D getLeftLine() {
        return new Line2D.Double(getTopLeft(), getBottomLeft());
    }

    /**
     * Gets the line formed by {@link #getTopRight()} and {@link #getBottomRight()}.
     *
     * @return The line between these points
     */
    public Line2D getRightLine() {
        return new Line2D.Double(getTopRight(), getBottomRight());
    }

    /**
     * Gets the diagonal line formed by {@link #getTopLeft()} and {@link #getBottomRight()}.
     *
     * @return The line between these points
     */
    public Line2D getTopLeftBottomRightDiagonal() {
        return new Line2D.Double(getTopLeft(), getBottomRight());
    }

    /**
     * Gets the diagonal line formed by {@link #getBottomRight()} and {@link #getTopLeft()}.
     *
     * @return The line between these points
     */
    public Line2D getBottomRightTopLeftDiagonal() {
        return new Line2D.Double(getBottomRight(), getTopLeft());
    }

    /**
     * Gets the diagonal line formed by {@link #getBottomLeft()} and {@link #getTopRight()}.
     *
     * @return The line between these points
     */
    public Line2D getBottomLeftTopRightDiagonal() {
        return new Line2D.Double(getBottomLeft(), getTopRight());
    }

    /**
     * Gets the diagonal line formed by {@link #getTopRight()} and {@link #getBottomLeft()}.
     *
     * @return The line between these points
     */
    public Line2D getTopRightBottomLeftDiagonal() {
        return new Line2D.Double(getTopRight(), getBottomLeft());
    }

    /**
     * Gets a four-element array of {@link Point} representing the top-left, top-right, bottom-right and bottom-left
     * corners of the quadrilateral, in that order.
     *
     * @return The four corners of this shape.
     */
    public Point[] getCorners() {
        return new Point[]{getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft()};
    }

    /**
     * Gets the center-point of the bounds of this shape.
     *
     * @return The center point.
     */
    public Point getCenter() {
        return new Point(getShape().getBounds().width / 2, getShape().getBounds().height / 2);
    }

    /**
     * Gets a {@link Shape} from this Quadrilateral.
     *
     * @return The shape.
     */
    public Shape getShape() {
        GeneralPath path = new GeneralPath();
        path.moveTo(topLeft.getX(), topLeft.getY());
        path.lineTo(topRight.getX(), topRight.getY());
        path.lineTo(bottomRight.getX(), bottomRight.getY());
        path.lineTo(bottomLeft.getX(), bottomLeft.getY());
        path.closePath();

        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return getRight() - getLeft();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return getBottom() - getTop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLeft() {
        return (int) Math.min(getLeftLine().getX1(), getLeftLine().getX2());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTop() {
        return (int) Math.min(getTopLine().getY1(), getTopLine().getY2());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBottom() {
        return (int) Math.max(getBottomLine().getY1(), getBottomLine().getY2());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRight() {
        return (int) Math.max(getRightLine().getX1(), getRightLine().getX2());
    }

    /**
     * Produces a copy of this quadrilateral, translated by a given amount in the x and y directions. Does not affect
     * this shape in any way.
     *
     * @param dx The number of pixels in the x-direction to translate
     * @param dy The number of pixels in the y-direction to translate
     * @return A new, translated quadrilateral.
     */
    public FlexQuadrilateral translate(int dx, int dy) {
        return new FlexQuadrilateral(
                new Point(topLeft.x - dx, topLeft.y - dy),
                new Point(topRight.x - dx, topRight.y - dy),
                new Point(bottomLeft.x - dx, bottomLeft.y - dy),
                new Point(bottomRight.x - dx, bottomRight.y - dy)
        );
    }
}
