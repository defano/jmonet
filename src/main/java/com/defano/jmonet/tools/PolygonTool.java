package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPolylineTool;

import java.awt.*;

/**
 * Tool to draw outlined or filled irregular polygons on the canvas.
 */
public class PolygonTool extends AbstractPolylineTool {

    public PolygonTool() {
        super(PaintToolType.POLYGON);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokePolyline(Graphics2D g, Stroke stroke, Paint paint, int[] xPoints, int[] yPoints) {
        g.setPaint(paint);
        g.setStroke(stroke);
        g.drawPolyline(xPoints, yPoints, xPoints.length);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokePolygon(Graphics2D g, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints) {
        g.setStroke(stroke);
        g.setPaint(strokePaint);
        g.drawPolygon(xPoints, yPoints, xPoints.length);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillPolygon(Graphics2D g, Paint fillPaint, int[] xPoints, int[] yPoints) {
        g.setPaint(fillPaint);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
    }
}
