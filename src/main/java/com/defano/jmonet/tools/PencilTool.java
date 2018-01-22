package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool for drawing a single-pixel, black, free-form path on the canvas.
 */
public class PencilTool extends AbstractPathTool {

    private Path2D path;

    public PencilTool() {
        super(PaintToolType.PENCIL);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Graphics2D g, Stroke stroke, Paint fillPaint, Point initialPoint) {
        path = new Path2D.Double();
        path.moveTo(initialPoint.getX(), initialPoint.getY());
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Graphics2D g, Stroke stroke, Paint fillPaint, Point point) {
        path.lineTo(point.getX(), point.getY());

        g.setStroke(new BasicStroke(1));
        g.setPaint(Color.BLACK);
        g.draw(path);
    }
}
