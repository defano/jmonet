package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractBoundsTool;

import java.awt.*;

/**
 * Tool for drawing outlined or filled ovals/circles on the canvas.
 */
public class OvalTool extends AbstractBoundsTool {

    public OvalTool() {
        super(PaintToolType.OVAL);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokeBounds(Graphics2D g, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        g.setStroke(stroke);
        g.setPaint(paint);
        g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillBounds(Graphics2D g, Paint fill, Rectangle bounds, boolean isShiftDown) {
        g.setPaint(fill);
        g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
