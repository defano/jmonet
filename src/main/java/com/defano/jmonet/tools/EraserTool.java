package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool that erases pixels from the canvas by turning them back to fully transparent.
 */
public class EraserTool extends AbstractPathTool {

    public EraserTool() {
        super(PaintToolType.ERASER);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        Graphics2D g = scratch.getRemoveScratchGraphics();

        g.setStroke(stroke);
        g.setPaint(getCanvas().getCanvasColor());
        g.draw(new Line2D.Float(initialPoint, initialPoint));
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        Graphics2D g = scratch.getRemoveScratchGraphics();

        g.setStroke(stroke);
        g.setPaint(getCanvas().getCanvasColor());
        g.draw(new Line2D.Float(lastPoint, thisPoint));
    }
}
