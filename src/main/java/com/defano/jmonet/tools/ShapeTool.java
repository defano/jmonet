package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractBoundsTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

/**
 * Tool for drawing regular polygons ("shapes") based on a configurable number of sides. For example, triangles,
 * squares, pentagons, hexagons, etc.
 */
public class ShapeTool extends AbstractBoundsTool {

    public ShapeTool() {
        super(PaintToolType.SHAPE);
    }

    /** {@inheritDoc} */
    @Override
    protected void drawBounds(Graphics2D g, Stroke stroke, Paint paint, int x, int y, int width, int height) {
        g.setStroke(stroke);
        g.setPaint(paint);
        g.drawPolygon(Geometry.polygon(initialPoint, getShapeSides(), getRadius(), getRotationAngle()));
    }

    /** {@inheritDoc} */
    @Override
    protected void drawFill(Graphics2D g, Paint fill, int x, int y, int width, int height) {
        g.setPaint(fill);
        g.fill(Geometry.polygon(initialPoint, getShapeSides(), getRadius(), getRotationAngle()));
    }

    private double getRadius() {
        return Geometry.distance(initialPoint, currentPoint);
    }

    private double getRotationAngle() {
        return Math.toRadians(Geometry.angle(initialPoint.x, initialPoint.y, currentPoint.x, currentPoint.y));
    }
}
