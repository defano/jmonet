package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractBoundsTool;

import java.awt.*;

/**
 * Draws outlined or filled rectangles/squares on the canvas.
 */
public class RectangleTool extends AbstractBoundsTool {

    public RectangleTool() {
        super(PaintToolType.RECTANGLE);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokeBounds(Graphics2D g, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        g.setStroke(stroke);
        g.setPaint(paint);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillBounds(Graphics2D g, Paint fill, Rectangle bounds, boolean isShiftDown) {
        g.setPaint(fill);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
