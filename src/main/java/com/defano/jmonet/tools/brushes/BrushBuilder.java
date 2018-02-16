package com.defano.jmonet.tools.brushes;

import com.defano.jmonet.model.Quadrilateral;
import com.defano.jmonet.tools.util.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class BrushBuilder {

    public static ShapeBrushBuilder withShape() {
        return new ShapeBrushBuilder();
    }

    public static BasicBrushBuilder withStroke() {
        return new BasicBrushBuilder();
    }

    public static class ShapeBrushBuilder {

        private ArrayList<Shape> shapes = new ArrayList<>();
        private boolean requiresTranslation = false;
        private boolean linearInterpolation = true;

        private ShapeBrushBuilder() {}

        public ShapeBrushBuilder ofCircle(int diameter) {
            shapes.add(new Ellipse2D.Double(0, 0, diameter, diameter));
            return this;
        }

        public ShapeBrushBuilder ofOval(int width, int height) {
            shapes.add(new Ellipse2D.Float(0, 0, width, height));
            return this;
        }

        public ShapeBrushBuilder ofRegularPolygon(int sides, int sideLength, double rotationDegrees) {
            shapes.add(Geometry.polygon(new Point(0, 0), sides, sideLength, Math.toRadians(rotationDegrees)));
            return this;
        }

        public ShapeBrushBuilder ofQuadrilateral(Quadrilateral quadrilateral) {
            shapes.add(quadrilateral.getShape());
            return this;
        }

        public ShapeBrushBuilder ofRectangle(int width, int height) {
            shapes.add(new Rectangle2D.Float(0, 0, width, height));
            return this;
        }

        public ShapeBrushBuilder ofSquare(int size) {
            shapes.add(new Rectangle2D.Float(0, 0, size, size));
            return this;
        }

        public ShapeBrushBuilder ofRoundRectangle(int width, int height, int arc) {
            shapes.add(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            return this;
        }

        public ShapeBrushBuilder ofHorizontalLine(int length) {
            return ofLine(length, 0);
        }

        public ShapeBrushBuilder ofVerticalLine(int length) {
            return ofLine(length, 90);
        }

        public ShapeBrushBuilder ofLine(int length, double rotationDegrees) {
            shapes.add(AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)).createTransformedShape(new Rectangle2D.Float(0, 0, length, 2)));
            this.requiresTranslation = true;
            return this;
        }

        public ShapeBrushBuilder ofText(String text) {
            shapes.add(new JLabel().getFont().createGlyphVector(new JLabel().getFontMetrics(new JLabel().getFont()).getFontRenderContext(), text).getOutline());
            this.requiresTranslation = true;
            return this;
        }

        public ShapeBrushBuilder ofText(String text, Font font) {
            shapes.add(font.createGlyphVector(new JLabel().getFontMetrics(font).getFontRenderContext(), text).getOutline());
            this.requiresTranslation = true;
            return this;
        }

        public ShapeBrushBuilder ofShape(Shape shape) {
            shapes.add(shape);
            return this;
        }

        public ShapeBrushBuilder withRotation(double degrees) {
            if (!shapes.isEmpty()) {
                shapes.set(shapes.size() - 1, AffineTransform.getRotateInstance(Math.toRadians(degrees)).createTransformedShape(shapes.get(shapes.size() - 1)));
            }
            return this;
        }

        public ShapeBrushBuilder withShear(double xShear, double yShear) {
            if (!shapes.isEmpty()) {
                shapes.set(shapes.size() - 1, AffineTransform.getShearInstance(xShear, yShear).createTransformedShape(shapes.get(shapes.size() - 1)));
            }
            return this;
        }

        public ShapeBrushBuilder withScale(double xScale, double yScale) {
            if (!shapes.isEmpty()) {
                shapes.set(shapes.size() - 1, AffineTransform.getScaleInstance(xScale, yScale).createTransformedShape(shapes.get(shapes.size() - 1)));
            }
            return this;
        }

        public ShapeBrushBuilder withoutInterpolation() {
            this.linearInterpolation = false;
            return this;
        }

        public Stroke build() {
            ShapeBrush brush = new ShapeBrush(shapes);
            brush.setLinearInterpolated(linearInterpolation);
            return brush;
        }
    }

    public static class BasicBrushBuilder {

        private float width = 4;
        private int cap = BasicStroke.CAP_ROUND;
        private int join = BasicStroke.JOIN_ROUND;

        private BasicBrushBuilder() {}

        public BasicBrushBuilder ofWidth(float width) {
            this.width = width;
            return this;
        }

        public BasicBrushBuilder withRoundCap() {
            this.cap = BasicStroke.CAP_ROUND;
            return this;
        }

        public BasicBrushBuilder withButtCap() {
            this.cap = BasicStroke.CAP_BUTT;
            return this;
        }

        public BasicBrushBuilder withSquareCap() {
            this.cap = BasicStroke.CAP_SQUARE;
            return this;
        }

        public BasicBrushBuilder withRoundJoin() {
            this.join = BasicStroke.JOIN_ROUND;
            return this;
        }

        public BasicBrushBuilder withMiterJoin() {
            this.join = BasicStroke.JOIN_MITER;
            return this;
        }

        public BasicBrushBuilder withBevelJoin() {
            this.join = BasicStroke.JOIN_BEVEL;
            return this;
        }

        public Stroke build() {
            return new BasicStroke(width, cap, join);
        }
    }

}
