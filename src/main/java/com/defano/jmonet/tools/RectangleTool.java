package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BoundsTool;
import com.defano.jmonet.tools.base.BoundsToolDelegate;

import java.awt.*;

/**
 * Draws outlined or filled rectangles/squares on the canvas.
 */
public class RectangleTool extends BoundsTool implements BoundsToolDelegate {

    public RectangleTool() {
        super(PaintToolType.RECTANGLE);
        setBoundsToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        Rectangle rectangle = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, rectangle);
        g.setStroke(stroke);
        g.setPaint(paint);
        g.draw(rectangle);
    }

    /** {@inheritDoc} */
    @Override
    public void fillBounds(Scratch scratch, Paint fill, Rectangle bounds, boolean isShiftDown) {
        GraphicsContext g = scratch.getAddScratchGraphics(this, null);
        g.setPaint(fill);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
