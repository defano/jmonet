package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.StrokedCursorPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool for drawing free-form, textured paths on the canvas.
 */
public class PaintbrushTool extends StrokedCursorPathTool {

    public PaintbrushTool() {
        super(PaintToolType.PAINTBRUSH);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        Line2D line = new Line2D.Float(initialPoint, initialPoint);

        Graphics2D g = scratch.getAddScratchGraphics(this, stroke, line);
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.draw(line);
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        Line2D line = new Line2D.Float(lastPoint, thisPoint);

        Graphics2D g = scratch.getAddScratchGraphics(this, stroke, line);
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.draw(line);
    }
}
