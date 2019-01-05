package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BoundsTool;
import com.defano.jmonet.tools.base.BoundsToolDelegate;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Tool for drawing outlined or filled ovals/circles on the canvas.
 */
public class OvalTool extends BoundsTool implements BoundsToolDelegate {

    public OvalTool() {
        super(PaintToolType.OVAL);
        setBoundsToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void strokeBounds(Scratch scratch, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown) {
        Ellipse2D oval = new Ellipse2D.Float(bounds.x, bounds.y, bounds.width, bounds.height);

        Graphics2D g = scratch.getAddScratchGraphics(this, stroke, oval);
        g.setStroke(stroke);
        g.setPaint(paint);
        g.draw(oval);
    }

    /** {@inheritDoc} */
    @Override
    public void fillBounds(Scratch scratch, Paint fill, Rectangle bounds, boolean isShiftDown) {
        Graphics2D g = scratch.getAddScratchGraphics(this, null);
        g.setPaint(fill);
        g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
