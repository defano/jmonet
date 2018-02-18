package com.defano.jmonet.tools.builder;

import com.defano.jmonet.model.Quadrilateral;
import com.defano.jmonet.tools.brushes.ShapeStroke;
import com.defano.jmonet.tools.util.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class StrokeBuilder {

    /**
     * Builds a stroke in which a shape is "stamped" along each point of the stroked path. Note that the stamped shape
     * (the pen) is not automatically rotated to match the angular perpendicular to the path (as occurs with a
     * {@link #withBasicStroke()}.
     *
     * @return The builder.
     */
    public static ShapeStrokeBuilder withShape() {
        return new ShapeStrokeBuilder();
    }

    /**
     * Builds a basic stroke.
     *
     * @return The builder
     */
    public static BasicStrokeBuilder withBasicStroke() {
        return new BasicStrokeBuilder();
    }

    /**
     * Builds strokes in which every point on the stroked shape's line is stamped with a shape.
     */
    public static class ShapeStrokeBuilder {

        private ArrayList<Shape> shapes = new ArrayList<>();
        private int interpolation = 1;

        /**
         * Use {@link StrokeBuilder#withShape()} to get an instance of this class
         */
        private ShapeStrokeBuilder() {
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
            shapes.add(Geometry.polygon(new Point(0, 0), sides, sideLength, Math.toRadians(rotationDegrees)));
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
         * @param length          The length of the line
         * @param rotationDegrees A rotation, in degrees, of the line. 0 degrees is horizontal; 90 degrees is vertical.
         * @return
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
         * When invoked, the stroke shape will be "stamped" only at each point where two paths join.
         *
         * @return The builder
         */
        public ShapeStrokeBuilder withoutInterpolation() {
            this.interpolation = 0;
            return this;
        }

        public ShapeStrokeBuilder interpolated(int resolution) {
            this.interpolation = resolution;
            return this;
        }

        /**
         * Creates the stroke as specified.
         *
         * @return The built stroke
         */
        public Stroke build() {
            ShapeStroke brush = new ShapeStroke(shapes);
            brush.setInterpolationInterval(interpolation);
            return brush;
        }
    }

    /**
     * Builds {@link BasicStroke} objects.
     */
    public static class BasicStrokeBuilder {

        private float width = 1;
        private int cap = BasicStroke.CAP_ROUND;
        private int join = BasicStroke.JOIN_ROUND;
        private ArrayList<Float> dash = new ArrayList<>();
        private float dashPhase = 0;
        private float miterLimit = 1;

        /**
         * Use {@link StrokeBuilder#withBasicStroke()} to get an instance of this class.
         */
        private BasicStrokeBuilder() {}

        /**
         * Specifies the width, in pixels, of the stroke.
         * @param width The width
         * @return The builder
         */
        public BasicStrokeBuilder ofWidth(float width) {
            this.width = width;
            return this;
        }

        /**
         * Ends unclosed subpaths and dash segments with a round decoration that has a radius equal to half of the
         * width of the pen.
         *
         * @return The builder
         */
        public BasicStrokeBuilder withRoundCap() {
            this.cap = BasicStroke.CAP_ROUND;
            return this;
        }

        /**
         * Ends unclosed subpaths and dash segments with no added decoration.
         *
         * @return The builder
         */
        public BasicStrokeBuilder withButtCap() {
            this.cap = BasicStroke.CAP_BUTT;
            return this;
        }

        /**
         * Ends unclosed subpaths and dash segments with a square projection that extends beyond the end of the segment
         * to a distance equal to half of the line width.
         *
         * @return The builder
         */
        public BasicStrokeBuilder withSquareCap() {
            this.cap = BasicStroke.CAP_SQUARE;
            return this;
        }

        /**
         * Joins path segments by rounding off the corner at a radius of half the line width.
         *
         * @return The builder
         */
        public BasicStrokeBuilder withRoundJoin() {
            this.join = BasicStroke.JOIN_ROUND;
            return this;
        }

        /**
         * Joins path segments by extending their outside edges until they meet.
         *
         * @return The builder
         */
        public BasicStrokeBuilder withMiterJoin() {
            this.join = BasicStroke.JOIN_MITER;
            return this;
        }

        /**
         * Joins path segments by connecting the outer corners of their wide outlines with a straight segment.
         *
         * @return The builder
         */
        public BasicStrokeBuilder withBevelJoin() {
            this.join = BasicStroke.JOIN_BEVEL;
            return this;
        }

        /**
         * Adds a dash pattern element to the stroke.
         *
         * For example, specifying '5' as the argument to this method produces a dashed/dotted line where every 5 pixels
         * are filled, and every 5 pixels are not. Invoking this method subsequent times appends values to the dash
         * pattern. Calling a second time with '2' produces a line where every 5 pixels are filled followed by 2 that
         * are not.
         *
         * @param dashLength The length of the dash pattern element to append
         * @return The Builder
         */
        public BasicStrokeBuilder withDash(float dashLength) {
            this.dash.add(dashLength);
            return this;
        }

        /**
         * The limit to trim the miter join; must be greater than or equal to 1.0f.
         *
         * @param miterLimit The miter limit
         * @return The builder
         */
        public BasicStrokeBuilder withMiterLimit(float miterLimit) {
            this.miterLimit = miterLimit;
            return this;
        }

        /**
         * The offset at which to start the dashing pattern.
         *
         * @param dashPhase The dash phase offset
         * @return The builder
         */
        public BasicStrokeBuilder withDashPhase(float dashPhase) {
            this.dashPhase = dashPhase;
            return this;
        }

        /**
         * Creates the {@link BasicStroke} as specified.
         *
         * @return The stroke
         */
        public BasicStroke build() {
            if (dash.isEmpty()) {
                return new BasicStroke(width, cap, join, miterLimit);
            } else {
                float[] dashArray = new float[dash.size()];
                for (int index = 0; index < dash.size(); index++) {
                    dashArray[index] = dash.get(index);
                }
                return new BasicStroke(width, cap, join, miterLimit, dashArray, dashPhase);
            }
        }
    }

}
