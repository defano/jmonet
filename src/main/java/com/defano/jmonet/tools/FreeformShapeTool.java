package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool allowing user to draw a free-form path (like a paintbrush) that is closed upon completion
 * and can thusly be filled with paint.
 */
public class FreeformShapeTool extends AbstractPathTool {

    private Path2D path;

    public FreeformShapeTool() {
        super(PaintToolType.FREEFORM);
    }

    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint paint, Point initialPoint) {
        path = new Path2D.Double();
        path.moveTo(initialPoint.getX(), initialPoint.getY());
    }

    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint paint, Point point) {
        path.lineTo(point.getX(), point.getY());

        g.setStroke(stroke);
        g.setPaint(getStrokePaint());
        g.draw(path);
    }

    @Override
    protected void completePath(Graphics2D g, Stroke stroke, Paint paint) {
        path.closePath();

        if (getFillPaint() != null) {
            g.setPaint(getFillPaint());
            g.fill(path);
        }

        g.setStroke(stroke);
        g.setPaint(getStrokePaint());
        g.draw(path);
    }
}
