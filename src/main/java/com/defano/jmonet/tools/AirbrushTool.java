package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.StrokedCursorPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * A tool that paints translucent textured paint on the canvas.
 */
public class AirbrushTool extends StrokedCursorPathTool {

    public AirbrushTool() {
        super(PaintToolType.AIRBRUSH);
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        Line2D line = new Line2D.Float(lastPoint, thisPoint);

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, line);
        g.setStroke(stroke);
        g.setPaint(fillPaint);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) getToolAttributes().getIntensity()));
        g.draw(line);
    }

    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint fillPaint) {
        // Nothing to do
    }

}
