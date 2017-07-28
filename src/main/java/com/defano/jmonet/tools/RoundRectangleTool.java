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
    protected void drawBounds(Graphics2D g, Stroke stroke, Paint paint, int x, int y, int width, int height) {
        g.setPaint(paint);
        g.setStroke(stroke);
        g.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius);
    }

    /** {@inheritDoc} */
    @Override
    protected void drawFill(Graphics2D g, Paint fill, int x, int y, int width, int height) {
        g.setPaint(fill);
        g.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}
