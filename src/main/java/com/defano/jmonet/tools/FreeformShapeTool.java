package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PathTool;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool allowing user to draw a free-form path (like a paintbrush) that is closed upon completion
 * and can thusly be filled with paint.
 */
public class FreeformShapeTool extends PathTool {

    private Path2D path;

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    FreeformShapeTool() {
        super(PaintToolType.FREEFORM);
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        path = new Path2D.Double();
        path.moveTo(initialPoint.getX(), initialPoint.getY());
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        path.lineTo(thisPoint.getX(), thisPoint.getY());

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, path);
        g.setStroke(stroke);
        g.setPaint(getToolAttributes().getStrokePaint());
        g.draw(path);
    }

    /** {@inheritDoc} */
    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint fillPaint) {
        path.closePath();

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, path);

        if (getToolAttributes().getFillPaint().isPresent()) {
            g.setPaint(getToolAttributes().getFillPaint().get());
            g.fill(path);
        }

        g.setStroke(stroke);
        g.setPaint(getToolAttributes().getStrokePaint());
        g.draw(path);
    }
}
