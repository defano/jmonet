package com.defano.jmonet.model;

import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * Models a four-sided shape with no restrictions on the relationship between those sides (in terms of length or
 * parallelness).
 */
public class FlexQuadrilateral {

    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;

    public FlexQuadrilateral(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public FlexQuadrilateral(Shape fromShape) {
        Rectangle bounds = fromShape.getBounds();
        topLeft = new Point(bounds.x, bounds.y);
        topRight = new Point(bounds.x + bounds.width, bounds.y);
        bottomLeft = new Point(bounds.x, bounds.y + bounds.height);
        bottomRight = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point topLeft) {
        if (topLeft.x < right() &&
                Geometry.isAbove(getBottomLeftTopRightDiaganol(), topLeft) &&
                Geometry.isAbove(getBottomLine(), topLeft) &&
                Geometry.isBelow(getRightLine(), topLeft))
        {
            this.topLeft = topLeft;
        }
    }

    public Point getTopRight() {
        return topRight;
    }

    public void setTopRight(Point topRight) {
        if (topRight.x > left() && topRight.y < bottom() &&
                Geometry.isAbove(getTopLeftBottomRightLine(), topRight) &&
                Geometry.isAbove(getLeftLine(), topRight) &&
                Geometry.isAbove(getBottomLine(), topRight))
        {
            this.topRight = topRight;
        }
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Point bottomLeft) {
        if (bottomLeft.x < right() && bottomLeft.y > top() &&
                Geometry.isBelow(getTopLeftBottomRightLine(), bottomLeft) &&
                Geometry.isBelow(getRightLine(), bottomLeft) &&
                Geometry.isBelow(getTopLine(), bottomLeft))
        {
            this.bottomLeft = bottomLeft;
        }
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Point bottomRight) {
        if (bottomRight.x > left() && bottomRight.y > top() &&
                Geometry.isBelow(getBottomLeftTopRightDiaganol(), bottomRight) &&
                Geometry.isAbove(getLeftLine(), bottomRight) &&
                Geometry.isBelow(getTopLine(), bottomRight))
        {
            this.bottomRight = bottomRight;
        }
    }

    public Line2D getTopLine() {
        return new Line2D.Double(getTopLeft(), getTopRight());
    }

    public Line2D getBottomLine() {
        return new Line2D.Double(getBottomLeft(), getBottomRight());
    }

    public Line2D getLeftLine() {
        return new Line2D.Double(getTopLeft(), getBottomLeft());
    }

    public Line2D getRightLine() {
        return new Line2D.Double(getTopRight(), getBottomRight());
    }

    public Line2D getTopLeftBottomRightLine() {
        return new Line2D.Double(getTopLeft(), getBottomRight());
    }

    public Line2D getBottomLeftTopRightDiaganol() {
        return new Line2D.Double(getBottomLeft(), getTopRight());
    }

    public Point[] getCorners() {
        return new Point[] {getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft()};
    }

    public Point getCenter() {
        return new Point(getShape().getBounds().width / 2, getShape().getBounds().height / 2);
    }

    public Shape getShape() {
        GeneralPath path = new GeneralPath();
        path.moveTo(topLeft.getX(), topLeft.getY());
        path.lineTo(topRight.getX(), topRight.getY());
        path.lineTo(bottomRight.getX(), bottomRight.getY());
        path.lineTo(bottomLeft.getX(), bottomLeft.getY());
        path.closePath();

        return path;
    }

    public int width() {
        return right() - left();
    }

    public int height() {
        return bottom() - top();
    }

    public int left() {
        return (int) Math.min(getLeftLine().getX1(), getLeftLine().getX2());
    }

    public int top() {
        return (int) Math.min(getTopLine().getY1(), getTopLine().getY2());
    }

    public int bottom() {
        return (int) Math.max(getBottomLine().getY1(), getBottomLine().getY2());
    }

    public int right() {
        return (int) Math.max(getRightLine().getX1(), getRightLine().getX2());
    }

    public FlexQuadrilateral translate(int dx, int dy) {
        return new FlexQuadrilateral(
                new Point(topLeft.x - dx,topLeft.y - dy),
                new Point(topRight.x - dx, topRight.y - dy),
                new Point(bottomLeft.x - dx , bottomLeft.y - dy),
                new Point(bottomRight.x - dx, bottomRight.y - dy)
        );
    }
}
