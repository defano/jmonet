package com.defano.jmonet.tools;

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

        // Eraser is basically a paintbrush whose stroke "clears" the pixels underneath it via the DST_OUT composite mode
        setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint fillPaint, Point initialPoint) {
        g.setStroke(stroke);
        g.setPaint(getCanvas().getCanvasColor());
        g.draw(new Line2D.Float(initialPoint, initialPoint));
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        g.setStroke(stroke);
        g.setPaint(getCanvas().getCanvasColor());
        g.draw(new Line2D.Float(lastPoint, thisPoint));
    }
}
