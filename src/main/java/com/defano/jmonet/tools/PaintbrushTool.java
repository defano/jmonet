package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool for drawing free-form, textured paths on the canvas.
 */
public class PaintbrushTool extends AbstractPathTool {

    public PaintbrushTool() {
        super(PaintToolType.PAINTBRUSH);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint fillPaint, Point initialPoint) {
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.draw(new Line2D.Float(initialPoint, initialPoint));
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.draw(new Line2D.Float(lastPoint, thisPoint));
    }
}
