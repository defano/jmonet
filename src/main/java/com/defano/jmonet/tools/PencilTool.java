package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;
import com.defano.jmonet.tools.util.CursorFactory;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool for drawing a single-pixel, black, free-form path on the canvas.
 */
public class PencilTool extends AbstractPathTool {

    public PencilTool() {
        super(PaintToolType.PENCIL);
        setToolCursor(CursorFactory.makePencilCursor());
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        Line2D line = new Line2D.Float(lastPoint, thisPoint);
        Graphics2D g = scratch.getAddScratchGraphics(this, new BasicStroke(1), line);

        g.setStroke(new BasicStroke(1));
        g.setPaint(Color.BLACK);
        g.draw(line);
    }
}
