package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PolylineTool;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool to draw outlined or filled irregular polygons on the canvas.
 */
public class PolygonTool extends PolylineTool {

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    PolygonTool() {
        super(PaintToolType.POLYGON);
    }

    /** {@inheritDoc} */
    @Override
    public void strokePolyline(Scratch scratch, Stroke stroke, Paint paint, int[] xPoints, int[] yPoints) {
        Path2D poly = getPolylineShape(xPoints, yPoints, xPoints.length);

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, poly);
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(poly);
    }

    /** {@inheritDoc} */
    @Override
    public void strokePolygon(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints) {
        Path2D polygon = getPolygonShape(xPoints, yPoints, xPoints.length);

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, polygon);
        g.setStroke(stroke);
        g.setPaint(strokePaint);
        g.draw(polygon);
    }

    /** {@inheritDoc} */
    @Override
    public void fillPolygon(Scratch scratch, Paint fillPaint, int[] xPoints, int[] yPoints) {
        GraphicsContext g = scratch.getAddScratchGraphics(this, null);
        g.setPaint(fillPaint);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    private Path2D getPolygonShape(int[] xPoints, int[] yPoints, int points) {
        Path2D poly = getPolylineShape(xPoints, yPoints, points);
        poly.closePath();
        return poly;
    }

    private Path2D getPolylineShape(int[] xPoints, int[] yPoints, int points) {
        Path2D poly = new Path2D.Double();

        if (points > 0) {
            poly.moveTo(xPoints[0], yPoints[0]);

            for (int index = 1; index < points; index++) {
                poly.lineTo(xPoints[index], yPoints[index]);
            }
        }

        return poly;
    }
}
