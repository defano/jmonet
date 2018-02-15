package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool for drawing a single-pixel, black, free-form path on the canvas.
 */
public class PencilTool extends AbstractPathTool {

    public PencilTool() {
        super(PaintToolType.PENCIL);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint fillPaint, Point initialPoint) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        g.setStroke(new BasicStroke(1));
        g.setPaint(Color.BLACK);
        g.draw(new Line2D.Float(lastPoint, thisPoint));
    }
}
