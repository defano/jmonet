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
    protected void drawBounds(Graphics2D g, Stroke stroke, Paint paint, int x, int y, int width, int height) {
        g.setStroke(stroke);
        g.setPaint(paint);
        g.drawOval(x, y, width, height);
    }

    /** {@inheritDoc} */
    @Override
    protected void drawFill(Graphics2D g, Paint fill, int x, int y, int width, int height) {
        g.setPaint(getFillPaint());
        g.fillOval(x, y, width, height);
    }
}
