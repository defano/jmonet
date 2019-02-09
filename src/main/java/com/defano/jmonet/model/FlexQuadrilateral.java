package com.defano.jmonet.model;

import com.defano.jmonet.tools.util.MathUtils;

import java.awt.*;

/**
 * Models a mutable, four-sided, simple polygon (quadrilateral) with no restrictions on the length, angle or
 * parallelism, except that the shape's edges cannot cross each other (self-intersecting quadrilaterals are
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
    @SuppressWarnings("WeakerAccess")
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
                MathUtils.isAbove(getBottomLeftTopRightDiagonal(), topLeft) &&
                MathUtils.isAbove(getBottomLine(), topLeft) &&
                MathUtils.isBelow(getRightLine(), topLeft)) {
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
                MathUtils.isAbove(getTopLeftBottomRightDiagonal(), topRight) &&
                MathUtils.isAbove(getLeftLine(), topRight) &&
                MathUtils.isAbove(getBottomLine(), topRight)) {
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
                MathUtils.isBelow(getTopLeftBottomRightDiagonal(), bottomLeft) &&
                MathUtils.isBelow(getRightLine(), bottomLeft) &&
                MathUtils.isBelow(getTopLine(), bottomLeft)) {
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
                MathUtils.isBelow(getBottomLeftTopRightDiagonal(), bottomRight) &&
                MathUtils.isAbove(getLeftLine(), bottomRight) &&
                MathUtils.isBelow(getTopLine(), bottomRight)) {
            this.bottomRight = bottomRight;
        }
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
