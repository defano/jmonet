package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractLineTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool that draws straight lines on the canvas.
 */
public class LineTool extends AbstractLineTool {

    public LineTool() {
        super(PaintToolType.LINE);
    }

    /** {@inheritDoc} */
    @Override
    protected void drawLine(Scratch scratch, Stroke stroke, Paint paint, int x1, int y1, int x2, int y2) {
        Line2D line = new Line2D.Float(x1, y1, x2, y2);

        Graphics2D g = scratch.getAddScratchGraphics(this, stroke, line);
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(line);
    }
}
