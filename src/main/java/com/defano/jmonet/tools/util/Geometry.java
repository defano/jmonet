package com.defano.jmonet.tools.util;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class with geometric and trigonometric routines used by various tools.
 */
public class Geometry {

    /**
     * Rounds a double value to the nearest provided integer multiple. For example rounding 24.3 to the nearest 10
     * yields 20.
     *
     * @param value The value to round
     * @param toNearest The nearest integer multiple.
     * @return The nearest integer multiple
     */
    public static int round(Double value, int toNearest) {
        return (int) (toNearest * Math.round(value / toNearest));
    }

    /**
     * Rounds an integer value to the nearest provided integer multiple. For example rounding 24 to the nearest 10
     * yields 20.
     *
     * @param value The value to round
     * @param toNearest The nearest integer multiple; if 0, no rounding occurs
     * @return The nearest integer multiple
     */
    public static int round(int value, int toNearest) {
        if (toNearest == 0) {
            toNearest = 1;
        }

        return toNearest * Math.round(value / toNearest);
    }


    /**
     * Calculates the length of the line represented by two points.
     *
     * @param p1 The first point on the line
     * @param p2 The second point on the line
     * @return The length of the line
     */
    public static double length(Point p1, Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) + Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    /**
     * Produces a linear interpolation of all points existing between two other points.
     * @param p1 The first point
     * @param p2 The second point
     * @param interval The interval on which to perform the interpolation
     * @return A list of all unique points existing on the line between p1 and p2
     */
    public static List<Point> linearInterpolation(Point p1, Point p2, int interval) {

        ArrayList<Point> interpolatedPoints = new ArrayList<>();

        double length = Geometry.length(p1, p2);
        double xStep = (p2.x - p1.x) / length;
        double yStep = (p2.y - p1.y) / length;

        for (int step = 0; step < length; step += interval) {
            Point interpolated = new Point((int)(p1.x + xStep * step), (int)(p1.y + yStep * step));
            if (!interpolatedPoints.contains(interpolated)) {
                interpolatedPoints.add(interpolated);
            }
        }

        return interpolatedPoints;
    }

    /**
     * Returns the angle (in degrees) of the line represented by (x1, y1), (x2, y2).
     *
     * @param x1 First x coordinate
     * @param y1 First y coordinate
     * @param x2 Second x coordinate
     * @param y2 Second y coordinate
     * @return The angle (in degrees) of the line formed by these two points
     */
    public static double angle(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    /**
     * Returns the angle (in degrees) of the given line.
     *
     * @param line The line whose angle should be calculated
     * @return The angle of the line, in degrees.
     */
    public static double angle(Line2D line) {
        return angle(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    /**
     * Calculates the second point of a line whose length and angle match the given length and angle.
     *
     * @param origin Origin/location of the line
     * @param length Length of the line
     * @param angle Desired angle (in degrees) of the resulting line
     * @return The second point on the line
     */
    public static Point2D line(Point2D origin, double length, double angle) {
        double radians = Math.toRadians(angle);
        return new Point2D.Double((origin.getX() + length * Math.cos(radians)), (origin.getY() + length * Math.sin(radians)));
    }

    /**
     * Given a line represented by points p1 and p2, returns a third point forming a line between it and p1 whose angle
     * is the closest multiple of the value provided by toNearestAngle.
     *
     * @param p1 First point on line
     * @param p2 Second point on line
     * @param toNearestAngle Closest multiple of degrees for which result should be produced
     * @return A third point such that the line formed by p1 and this value is a multiple of toNearestAngle
     */
    public static Point line(Point p1, Point p2, int toNearestAngle) {
        double length = distance(p1, p2);
        double nearestAngle = round(Geometry.angle(p1.x, p1.y, p2.x, p2.y), toNearestAngle);
        return asPoint(line(p1, length, nearestAngle));
    }

    /**
     * Returns the distance between points p1 and p2
     *
     * @param p1 First point on line
     * @param p2 Second point on line
     * @return Length of resulting line (or the distance between points)
     */
    public static double distance(Point2D p1, Point2D p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    /**
     * Calculates the theta angle (in radians) formed between two lines represented by (origin, p1) and (origin, p2).
     *
     * @param origin The common point of both lines
     * @param p1 The second point in line 1 (origin, p1)
     * @param p2 The second point in line 2 (origin, p2)
     * @return The theta angle (in radians) between (origin, p1) and (origin, p2)
     */
    public static double theta(Point origin, Point p1, Point p2) {

        double angle1 = Math.atan2(p1.y - origin.y, p1.x - origin.x);
        double angle2 = Math.atan2(p2.y - origin.y, p2.x - origin.x);

        return angle1 - angle2;
    }

    /**
     * Changes the location of rect such that it's center is positioned at the same location of the center of inBounds.
     * @param rect The rectangle whose position should be translated
     * @param inBounds The reference/boundary rectangle that rect should be centered within
     */
    public static void center(Rectangle rect, Rectangle inBounds) {
        int x = inBounds.x + ((inBounds.width - rect.width) / 2);
        int y = inBounds.y + ((inBounds.height - rect.height) / 2);

        rect.setLocation(x, y);
    }

    /**
     * Returns a Rectangle whose height and width equal the larger dimension of the rectangle formed by anchor and
     * bound, but which always has a corner at anchor.
     *
     * (Which corner, of course, depends on the location of bound in coordinate space relative to anchor.)
     *
     * @param anchor Either top-left, top-right, bottom-left or bottom-right of the square
     * @param bound The bound used to determine the max dimension of the square
     * @return A {@link Rectangle} with equal heights and widths
     */
    public static Rectangle square(Point anchor, Point bound) {
        Rectangle rectangle = rectangle(anchor, bound);

        int xDelta = 0;
        int yDelta = 0;
        int maxDim = Math.max(rectangle.width, rectangle.height);

        // Calculate difference in height or width required to square the rectangle
        if (rectangle.width > rectangle.height) {
            yDelta += rectangle.width - rectangle.height;
        } else {
            xDelta += rectangle.height - rectangle.width;
        }

        // Square down and right of anchor
        if (bound.x > anchor.x && bound.y > anchor.y) {
            return new Rectangle(anchor.x, anchor.y, maxDim, maxDim);
        }

        // Square up and right of anchor
        else if (bound.x > anchor.x && bound.y < anchor.y) {
            return new Rectangle(anchor.x, bound.y - yDelta, maxDim, maxDim);
        }

        // Square down and left of anchor
        else if (bound.x < anchor.x && bound.y < anchor.y) {
            return new Rectangle(bound.x - xDelta, bound.y - yDelta, maxDim, maxDim);
        }

        // Square up and left of anchor
        else {
            return new Rectangle(bound.x - xDelta, anchor.y, maxDim, maxDim);
        }
    }

    /**
     * Returns a rectangle that intersects the two points. No order is implied; p1 may be to the top-left or
     * to the bottom-right of p2.
     *
     * @param p1 Either top-left, top-right, bottom-left or bottom-right of the rectangle
     * @param p2 The corner opposite p1
     * @return A rectangle interesting p1 and p2
     */
    public static Rectangle rectangle(Point p1, Point p2) {
        int left = Math.min(p1.x, p2.x);
        int top = Math.min(p1.y, p2.y);
        int right = Math.max(p1.x, p2.x);
        int bottom = Math.max(p1.y, p2.y);

        return new Rectangle(left, top, right - left, bottom - top);
    }

    /**
     * Returns a point closest to the given point but which remains inside the provided boundary.
     *
     * @param p The point to test
     * @param bounds The boundary in which to limit the resultant point
     * @return The value of p if p is within bounds, or the closest point to p that remains inside bounds
     */
    public static Point constrainToBounds(Point p, Rectangle bounds) {

        if (bounds.contains(p)) {
            return p;
        }

        int newX = p.x;
        int newY = p.y;

        if (p.x < bounds.x) {
            newX = bounds.x;
        } else if (p.x > bounds.x + bounds.width) {
            newX = bounds.x + bounds.width;
        }

        if (p.y < bounds.y) {
            newY = bounds.y;
        } else if (p.y > bounds.y + bounds.height) {
            newY = bounds.y + bounds.height;
        }

        return new Point(newX, newY);
    }

    /**
     * Draws a regular polygon (a shape) whose center is located at the provided location and containing a given number
     * of sides, length and rotation.
     *
     * @param location Desired centerpoint of the polygon
     * @param sides The number of sides to be drawn
     * @param length The longest dimension of the polygon
     * @param rotation The angle of the polygon (in radians)
     * @return The specified polygon
     */
    public static Polygon polygon(Point location, int sides, double length, double rotation) {
        double angle = (2 * Math.PI) / sides;
        double radius = (length / 2) / Math.sin(angle / 2);

        double xPoint = (Math.cos(rotation) * radius) + location.x;
        double yPoint = (Math.sin(rotation) * radius) + location.y;

        Polygon polygon = new Polygon();
        polygon.addPoint((int) Math.round(xPoint), (int) Math.round(yPoint));

        for (int i = 1; i <= sides; i++) {
            xPoint = (Math.cos((angle * i) + rotation) * radius) + location.x;
            yPoint = (Math.sin((angle * i) + rotation) * radius) + location.y;
            polygon.addPoint((int) Math.round(xPoint), (int) Math.round(yPoint));
        }

        return polygon;
    }

    /**
     * Extrapolates a point on a line parallel to a given line. Calculates the distance between reference and anchor
     * and returns a new point that is equidistant from anchor but produces an imaginary line parallel to angle.
     *
     * @param angle The reference line representing an angle to constrain the result to
     * @param anchor The anchor point from which the new point will be relative to
     * @param reference The reference point, used to determine distance from anchor
     * @return A point that appears on a line parallel to angle and which is the same distance from anchor as reference.
     */
    public static Point extrapolate(Line2D angle, Point anchor, Point reference) {
        double degrees = Geometry.angle(angle);
        return Geometry.asPoint(Geometry.line(anchor, Geometry.distance(anchor, reference), degrees));
    }

    /**
     * Determines if the given point is below the given line. If the line is perfectly vertical, below is defined as
     * being to the right of.
     *
     * @param line The line
     * @param p The point to compare
     * @return True if the point is below the line.
     */
    public static boolean isBelow(Line2D line, Point p) {
        return (line.getX2() - line.getX1()) * (p.y - line.getY1()) - (line.getY2() - line.getY1()) * (p.x - line.getX1()) > 0;
    }

    /**
     * Determines if the given point is above the given line. If the line is perfectly vertical, above is defined as
     * being to the left of.
     *
     * @param line The line
     * @param p The point to compare
     * @return True if the point is above the line.
     */
    public static boolean isAbove(Line2D line, Point p) {
        return (line.getX2() - line.getX1()) * (p.y - line.getY1()) - (line.getY2() - line.getY1()) * (p.x - line.getX1()) < 0;
    }

    /**
     * Rounds a Point2D (with floating-point precision) to an integer-precision Point.
     * @param p2d The Point2D to truncate
     * @return The nearest integer-precision point to p2d
     */
    public static Point asPoint(Point2D p2d) {
        return new Point((int) Math.round(p2d.getX()), (int) Math.round(p2d.getY()));
    }
}
