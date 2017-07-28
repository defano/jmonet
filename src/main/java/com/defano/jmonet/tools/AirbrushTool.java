package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * A tool that paints translucent textured paint on the canvas.
 */
public class AirbrushTool extends AbstractPathTool {

    private Point lastPoint;

    public AirbrushTool() {
        super(PaintToolType.AIRBRUSH);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint paint, Point initialPoint) {
        lastPoint = initialPoint;
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint paint, Point point) {
        g.setStroke(stroke);
        g.setPaint(paint);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        g.draw(new Line2D.Float(lastPoint, point));

        lastPoint = point;
    }
}
