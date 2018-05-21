package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractBoundsTool;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Tool for drawing outlined or filled rounded-rectangles on the canvas.
 */
public class RoundRectangleTool extends AbstractBoundsTool {

    public RoundRectangleTool() {
        super(PaintToolType.ROUND_RECTANGLE);
    }

    /** {@inheritDoc} */
    @Override
    protected void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        int cornerRadius = getCornerRadiusObservable().blockingFirst();
        RoundRectangle2D roundRect = new RoundRectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height, cornerRadius, cornerRadius);
        scratch.updateAddScratchClip(stroke, roundRect);

        Graphics2D g = scratch.getAddScratchGraphics();
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(roundRect);
    }

    /** {@inheritDoc} */
    @Override
    protected void fillBounds(Scratch scratch, Paint fill, Rectangle bounds, boolean isShiftDown) {
        Graphics2D g = scratch.getAddScratchGraphics();
        int cornerRadius = getCornerRadiusObservable().blockingFirst();

        g.setPaint(fill);
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, cornerRadius, cornerRadius);
    }
}
