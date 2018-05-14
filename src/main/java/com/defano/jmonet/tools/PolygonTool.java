package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
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
    protected void strokePolyline(Scratch scratch, Stroke stroke, Paint paint, int[] xPoints, int[] yPoints) {
        Graphics2D g = scratch.getAddScratchGraphics();

        g.setPaint(paint);
        g.setStroke(stroke);
        g.drawPolyline(xPoints, yPoints, xPoints.length);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokePolygon(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints) {
        Graphics2D g = scratch.getAddScratchGraphics();

        g.setStroke(stroke);
        g.setPaint(strokePaint);
        g.drawPolygon(xPoints, yPoints, xPoints.length);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillPolygon(Scratch scratch, Paint fillPaint, int[] xPoints, int[] yPoints) {
        Graphics2D g = scratch.getAddScratchGraphics();

        g.setPaint(fillPaint);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
    }
}
