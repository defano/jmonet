package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
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
    protected void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        Rectangle rectangle = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);

        Graphics2D g = scratch.getAddScratchGraphics(stroke, rectangle);
        g.setStroke(stroke);
        g.setPaint(paint);
        g.draw(rectangle);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillBounds(Scratch scratch, Paint fill, Rectangle bounds, boolean isShiftDown) {
        Graphics2D g = scratch.getAddScratchGraphics(null);
        g.setPaint(fill);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
