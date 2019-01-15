package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BoundsTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

/**
 * Tool for drawing regular polygons ("shapes") based on a configurable number of sides. For example, triangles,
 * squares, pentagons, hexagons, etc.
 */
public class ShapeTool extends BoundsTool {

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    ShapeTool() {
        super(PaintToolType.SHAPE);
    }

    /** {@inheritDoc} */
    @Override
    public void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        Polygon poly = Geometry.polygon(getInitialPoint(), getAttributes().getShapeSides(), getRadius(), getRotationAngle(isShiftDown));

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, poly);
        g.setStroke(stroke);
        g.setPaint(paint);
        g.draw(poly);
    }

    /** {@inheritDoc} */
    @Override
    public void fillBounds(Scratch scratch, Paint fill, Rectangle bounds, boolean isShiftDown) {
        GraphicsContext g = scratch.getAddScratchGraphics(this, null);
        g.setPaint(fill);
        g.fill(Geometry.polygon(getInitialPoint(), getAttributes().getShapeSides(), getRadius(), getRotationAngle(isShiftDown)));
    }

    private double getRadius() {
        return Geometry.distance(getInitialPoint(), getCurrentPoint());
    }

    private double getRotationAngle(boolean isShiftDown) {
        double degrees = Geometry.angle(getInitialPoint().x, getInitialPoint().y, getCurrentPoint().x, getCurrentPoint().y);

        if (isShiftDown) {
            degrees = Geometry.round(degrees, getAttributes().getConstrainedAngle());
        }

        return Math.toRadians(degrees);
    }
}
