package com.defano.jmonet.tools.builder;

import com.defano.jmonet.model.Quadrilateral;
import com.defano.jmonet.tools.brushes.ShapeStroke;
import com.defano.jmonet.tools.util.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Builds strokes in which every point on the stroked shape's line is stamped with a shape.
 */
@SuppressWarnings("unused")
public class ShapeStrokeBuilder {

    private final ArrayList<Shape> shapes = new ArrayList<>();
    private int interpolatedInterval = 1;

    /**
     * Use {@link StrokeBuilder#withShape()} to get an instance of this class
     */
    ShapeStrokeBuilder() {
    }

    /**
     * Creates a circular stroke of a specified diameter.
     *
     * @param diameter The diameter of the circular stroke
     * @return The builder
     */
    public ShapeStrokeBuilder ofCircle(int diameter) {
        shapes.add(new Ellipse2D.Double(0, 0, diameter, diameter));
        return this;
    }

    /**
     * Creates an oval stroke of a specified width and height.
     *
     * @param width  The width of the oval
     * @param height The height of the oval
     * @return The builder
     */
    public ShapeStrokeBuilder ofOval(int width, int height) {
        shapes.add(new Ellipse2D.Float(0, 0, width, height));
        return this;
    }

    /**
     * Creates a regular polygon stroke.
     *
     * @param sides           The number of sides of the polygon (i.e., 3 for a triangle, 8 for an octagon, etc.)
     * @param sideLength      The length, in pixels, of each side.
     * @param rotationDegrees Degrees to rotate the orientation of the polygon
     * @return The builder
     */
    public ShapeStrokeBuilder ofRegularPolygon(int sides, int sideLength, double rotationDegrees) {
        shapes.add(MathUtils.polygon(new Point(0, 0), sides, sideLength, Math.toRadians(rotationDegrees)));
        return this;
    }

    /**
     * Creates a quadrilateral stroke.
     *
     * @param quadrilateral The quadrilateral
     * @return The builder
     */
    public ShapeStrokeBuilder ofQuadrilateral(Quadrilateral quadrilateral) {
        shapes.add(quadrilateral.getShape());
        return this;
    }

    /**
     * Creates a rectangular stroke.
     *
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @return The builder
     */
    public ShapeStrokeBuilder ofRectangle(int width, int height) {
        shapes.add(new Rectangle2D.Float(0, 0, width, height));
        return this;
    }

    /**
     * Creates a square stroke.
     *
     * @param size The length of each edge of the square, in pixels
     * @return The builder
     */
    public ShapeStrokeBuilder ofSquare(int size) {
        shapes.add(new Rectangle2D.Float(0, 0, size, size));
        return this;
    }

    /**
     * Creates a round rectangle stroke.
     *
     * @param width  The width of the round rectangle
     * @param height The height of the round rectangle
     * @param arc    And height and width of the arc that rounds the rectangle
     * @return The builder
     */
    public ShapeStrokeBuilder ofRoundRectangle(int width, int height, int arc) {
        shapes.add(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
        return this;
    }

    /**
     * Creates a horizontal line stroke.
     *
     * @param length The length of the line, in pixels
     * @return The builder
     */
    public ShapeStrokeBuilder ofHorizontalLine(int length) {
        return ofLine(length, 2, 0);
    }

    /**
     * Creates a vertical line stroke.
     *
     * @param length The length of the line, in pixels
     * @return The builder
     */
    public ShapeStrokeBuilder ofVerticalLine(int length) {
        return ofLine(length, 2, 90);
    }

    /**
     * Creates a linear stroke.
     *
     * @param length The length of the line
     * @param width The width of the line
     * @param rotationDegrees A rotation, in degrees, of the line. 0 degrees is horizontal; 90 degrees is vertical.
     * @return The builder
     */
    public ShapeStrokeBuilder ofLine(int length, int width, double rotationDegrees) {
        shapes.add(AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)).createTransformedShape(new Rectangle2D.Float(0, 0, length, width)));
        return this;
    }

    /**
     * Creates a stroke whose shape is a provided string (rendered in the system default font).
     *
     * @param text The text shape of the stroke
     * @return The builder
     */
    public ShapeStrokeBuilder ofText(String text) {
        shapes.add(new JLabel().getFont().createGlyphVector(new JLabel().getFontMetrics(new JLabel().getFont()).getFontRenderContext(), text).getOutline());
        return this;
    }

    /**
     * Creates a stroke whose shape is a provided string, rendered in a provided font.
     *
     * @param text The text shape of the stroke
     * @param font The font in which to render the text
     * @return The builder
     */
    public ShapeStrokeBuilder ofText(String text, Font font) {
        shapes.add(font.createGlyphVector(new JLabel().getFontMetrics(font).getFontRenderContext(), text).getOutline());
        return this;
    }

    /**
     * Creates a stroke of an arbitrary shape.
     *
     * @param shape The shape of the stroke
     * @return The builder
     */
    public ShapeStrokeBuilder ofShape(Shape shape) {
        shapes.add(shape);
        return this;
    }

    /**
     * Rotates the last-specified stroke shape by a given amount. Throws an exception if invoked prior to specifying
     * a shape.
     *
     * @param degrees Number of degrees to rotate the shape.
     * @return The builder
     */
    public ShapeStrokeBuilder rotated(double degrees) {
        if (!shapes.isEmpty()) {
            shapes.set(shapes.size() - 1, AffineTransform.getRotateInstance(Math.toRadians(degrees)).createTransformedShape(shapes.get(shapes.size() - 1)));
        } else {
            throw new IllegalStateException("Specify a shape before calling 'rotated'.");
        }
        return this;
    }

    /**
     * Shears (slants) the last-specified stroke shape. Throws an exception if invoked prior to specifying a shape.
     *
     * @param xShear The horizontal shear multiplier; 0 means no shear
     * @param yShear The vertical shear multiplier; 0 means no shear
     * @return The builder
     */
    public ShapeStrokeBuilder sheared(double xShear, double yShear) {
        if (!shapes.isEmpty()) {
            shapes.set(shapes.size() - 1, AffineTransform.getShearInstance(xShear, yShear).createTransformedShape(shapes.get(shapes.size() - 1)));
        } else {
            throw new IllegalStateException("Specify a shape before calling 'sheared'.");
        }
        return this;
    }

    /**
     * Scales (enlarges or reduces) the last-specified stroke shape. Throws an exception if invoked prior to
     * specifying a shape.
     *
     * @param xScale The horizontal scale multiplier (1.0 means no scale)
     * @param yScale The vertical scale of the multiplier (1.0 means no scale)
     * @return The builder
     */
    public ShapeStrokeBuilder scaled(double xScale, double yScale) {
        if (!shapes.isEmpty()) {
            shapes.set(shapes.size() - 1, AffineTransform.getScaleInstance(xScale, yScale).createTransformedShape(shapes.get(shapes.size() - 1)));
        } else {
            throw new IllegalStateException("Specify a shape before calling 'scaled'.");
        }
        return this;
    }

    /**
     * Transforms the last specified stroke shape from a filled (solid) shape into an outlined shape.
     *
     * @param width The width, in pixels, of the outline
     * @return The builder
     */
    public ShapeStrokeBuilder outlined(float width) {
        return stroked(new BasicStroke(width));
    }

    /**
     * Transforms the last specified shape from a filled (solid) shape into a stroked shape that is stroked by the
     * given stroke. That is, this method produces a stroke from another stroke.
     * <p>
     * It is advised not to stroke a stroke with a {@link com.defano.jmonet.tools.brushes.StampStroke}, as doing
     * so exponentially increases the drawing complexity of whatever shape is being stroked.
     *
     * @param stroke The stroke with which to stroke the current stroke.
     * @return The builder.
     */
    public ShapeStrokeBuilder stroked(Stroke stroke) {
        if (!shapes.isEmpty()) {
            shapes.set(shapes.size() - 1, stroke.createStrokedShape(shapes.get(shapes.size() - 1)));
        } else {
            throw new IllegalStateException("Specify a shape before calling 'stroked'.");
        }
        return this;
    }

    /**
     * When invoked, the stroke shape will be "stamped" only at each point where two paths join. Equivalent to invoking
     * {@link #interpolatedInterval(int)} with a resolution of 0.
     *
     * @return The builder
     */
    public ShapeStrokeBuilder withoutInterpolation() {
        this.interpolatedInterval = 0;
        return this;
    }

    /**
     * Specifies the interval at which each stroke will be stamped. A value of 1 indicates the shape will be stamped
     * at each pixel along the drawn path.
     *
     * @param interval The number of pixels between each "stamp" interpolated between defined points in the drawn path.
     * @return The builder
     */
    public ShapeStrokeBuilder interpolatedInterval(int interval) {
        this.interpolatedInterval = interval;
        return this;
    }

    /**
     * Creates the stroke as specified.
     *
     * @return The built stroke
     */
    public Stroke build() {
        ShapeStroke brush = new ShapeStroke(shapes);
        brush.setInterpolationInterval(interpolatedInterval);
        return brush;
    }
}
