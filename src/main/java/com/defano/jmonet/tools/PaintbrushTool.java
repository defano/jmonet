package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
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
    protected void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        Line2D line = new Line2D.Float(initialPoint, initialPoint);
        scratch.updateAddScratchClip(stroke, line);

        Graphics2D g = scratch.getAddScratchGraphics();
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.draw(line);
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        Line2D line = new Line2D.Float(lastPoint, thisPoint);
        scratch.updateAddScratchClip(stroke, line);

        Graphics2D g = scratch.getAddScratchGraphics();
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.draw(line);
    }
}
