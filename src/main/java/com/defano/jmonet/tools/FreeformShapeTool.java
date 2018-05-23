package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractPathTool;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool allowing user to draw a free-form path (like a paintbrush) that is closed upon completion
 * and can thusly be filled with paint.
 */
public class FreeformShapeTool extends AbstractPathTool {

    private Path2D path;

    public FreeformShapeTool() {
        super(PaintToolType.FREEFORM);
    }

    /** {@inheritDoc} */
    @Override
    protected void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        path = new Path2D.Double();
        path.moveTo(initialPoint.getX(), initialPoint.getY());
    }

    /** {@inheritDoc} */
    @Override
    protected void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        path.lineTo(thisPoint.getX(), thisPoint.getY());

        Graphics2D g = scratch.getAddScratchGraphics(stroke, path);
        g.setStroke(stroke);
        g.setPaint(getStrokePaint());
        g.draw(path);
    }

    /** {@inheritDoc} */
    @Override
    protected void completePath(Scratch scratch, Stroke stroke, Paint fillPaint) {
        path.closePath();

        Graphics2D g = scratch.getAddScratchGraphics(stroke, path);

        if (getFillPaint().isPresent()) {
            g.setPaint(getFillPaint().get());
            g.fill(path);
        }

        g.setStroke(stroke);
        g.setPaint(getStrokePaint());
        g.draw(path);
    }
}
