package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BoundsTool;
import com.defano.jmonet.tools.base.BoundsToolDelegate;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

/**
 * Tool for drawing regular polygons ("shapes") based on a configurable number of sides. For example, triangles,
 * squares, pentagons, hexagons, etc.
 */
public class ShapeTool extends BoundsTool implements BoundsToolDelegate {

    public ShapeTool() {
        super(PaintToolType.SHAPE);
        setBoundsToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        Polygon poly = Geometry.polygon(getInitialPoint(), getToolAttributes().getShapeSides(), getRadius(), getRotationAngle(isShiftDown));

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
        g.fill(Geometry.polygon(getInitialPoint(), getToolAttributes().getShapeSides(), getRadius(), getRotationAngle(isShiftDown)));
    }

    private double getRadius() {
        return Geometry.distance(getInitialPoint(), getCurrentPoint());
    }

    private double getRotationAngle(boolean isShiftDown) {
        double degrees = Geometry.angle(getInitialPoint().x, getInitialPoint().y, getCurrentPoint().x, getCurrentPoint().y);

        if (isShiftDown) {
            degrees = Geometry.round(degrees, getToolAttributes().getConstrainedAngle());
        }

        return Math.toRadians(degrees);
    }
}
