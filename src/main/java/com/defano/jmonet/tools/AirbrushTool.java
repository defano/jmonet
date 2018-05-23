package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 * A tool that paints translucent textured paint on the canvas.
 */
public class AirbrushTool extends AbstractPathTool {

    public AirbrushTool() {
        super(PaintToolType.AIRBRUSH);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        Line2D line = new Line2D.Float(lastPoint, thisPoint);

        Graphics2D g = scratch.getAddScratchGraphics(stroke, line);
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getIntensityObservable().blockingFirst().floatValue()));
        g.draw(line);
    }
}
