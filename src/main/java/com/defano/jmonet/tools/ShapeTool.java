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
    protected void drawBounds(Graphics2D g, Stroke stroke, Paint paint, Rectangle rectangle, boolean isShiftDown) {
        g.setStroke(stroke);
        g.setPaint(paint);
        g.drawPolygon(Geometry.polygon(initialPoint, getShapeSides(), getRadius(), getRotationAngle(isShiftDown)));
    }

    /** {@inheritDoc} */
    @Override
    protected void drawFill(Graphics2D g, Paint fill, Rectangle rectangle, boolean isShiftDown) {
        g.setPaint(fill);
        g.fill(Geometry.polygon(initialPoint, getShapeSides(), getRadius(), getRotationAngle(isShiftDown)));
    }

    private double getRadius() {
        return Geometry.distance(initialPoint, currentPoint);
    }

    private double getRotationAngle(boolean isShiftDown) {
        double degrees = Geometry.angle(initialPoint.x, initialPoint.y, currentPoint.x, currentPoint.y);

        if (isShiftDown) {
            degrees = Geometry.round(degrees, getConstrainedAngle());
        }

        return Math.toRadians(degrees);
    }
}
