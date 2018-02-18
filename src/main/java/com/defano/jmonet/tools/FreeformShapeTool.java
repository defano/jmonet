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

    /** {@inheritDoc} */
    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint fillPaint, Point initialPoint) {
        path = new Path2D.Double();
        path.moveTo(initialPoint.getX(), initialPoint.getY());
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        path.lineTo(thisPoint.getX(), thisPoint.getY());

        g.setStroke(stroke);
        g.setPaint(getStrokePaint());
        g.draw(path);
    }

    /** {@inheritDoc} */
    @Override
    protected void completePath(Graphics2D g, Stroke stroke, Paint fillPaint) {
        path.closePath();

        if (getFillPaint().isPresent()) {
            g.setPaint(getFillPaint().get());
            g.fill(path);
        }

        g.setStroke(stroke);
        g.setPaint(getStrokePaint());
        g.draw(path);
    }
}
