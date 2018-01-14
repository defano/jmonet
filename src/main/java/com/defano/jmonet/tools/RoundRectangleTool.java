package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractBoundsTool;

import java.awt.*;

/**
 * Tool for drawing outlined or filled rounded-rectangles on the canvas.
 */
public class RoundRectangleTool extends AbstractBoundsTool {

    private int cornerRadius = 10;

    public RoundRectangleTool() {
        super(PaintToolType.ROUND_RECTANGLE);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokeBounds(Graphics2D g, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        g.setPaint(paint);
        g.setStroke(stroke);
        g.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, cornerRadius, cornerRadius);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillBounds(Graphics2D g, Paint fill, Rectangle bounds, boolean isShiftDown) {
        g.setPaint(fill);
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, cornerRadius, cornerRadius);
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}
