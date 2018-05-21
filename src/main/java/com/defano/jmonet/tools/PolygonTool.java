package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPolylineTool;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool to draw outlined or filled irregular polygons on the canvas.
 */
public class PolygonTool extends AbstractPolylineTool {

    public PolygonTool() {
        super(PaintToolType.POLYGON);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokePolyline(Scratch scratch, Stroke stroke, Paint paint, int[] xPoints, int[] yPoints) {
        Path2D poly = getPolylineShape(xPoints, yPoints, xPoints.length);
        scratch.updateAddScratchClip(stroke, poly);

        Graphics2D g = scratch.getAddScratchGraphics();
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(poly);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokePolygon(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints) {
        Path2D polygon = getPolygonShape(xPoints, yPoints, xPoints.length);
        scratch.updateAddScratchClip(stroke, polygon);

        Graphics2D g = scratch.getAddScratchGraphics();
        g.setStroke(stroke);
        g.setPaint(strokePaint);
        g.draw(polygon);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillPolygon(Scratch scratch, Paint fillPaint, int[] xPoints, int[] yPoints) {
        Graphics2D g = scratch.getAddScratchGraphics();
        g.setPaint(fillPaint);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    private Path2D getPolygonShape(int xPoints[], int yPoints[], int points) {
        Path2D poly = getPolylineShape(xPoints, yPoints, points);
        poly.closePath();
        return poly;
    }

    private Path2D getPolylineShape(int xPoints[], int yPoints[], int points) {
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
