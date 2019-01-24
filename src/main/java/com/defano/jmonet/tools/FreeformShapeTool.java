package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PathTool;
import com.defano.jmonet.tools.base.PathToolDelegate;
import com.defano.jmonet.tools.builder.PaintToolBuilder;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tool allowing user to draw a free-form path (like a paintbrush) that is closed upon completion
 * and can thusly be filled with paint.
 */
public class FreeformShapeTool extends PathTool implements PathToolDelegate {

    private Path2D path;

    /**
     * Tool must be constructed via {@link PaintToolBuilder} to handle dependency
     * injection.
     */
    FreeformShapeTool() {
        super(PaintToolType.FREEFORM);
        setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint strokePaint, Point initialPoint) {
        path = new Path2D.Double();
        path.moveTo(initialPoint.getX(), initialPoint.getY());
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint strokePaint, Point lastPoint, Point thisPoint) {
        path.lineTo(thisPoint.getX(), thisPoint.getY());

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, path);
        g.setStroke(stroke);
        g.setPaint(getAttributes().getStrokePaint());
        g.draw(path);
    }

    /** {@inheritDoc} */
    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint strokePaint, Paint fillPaint) {
        path.closePath();

        GraphicsContext g = scratch.getAddScratchGraphics(this, stroke, path);

        if (fillPaint != null) {
            g.setPaint(fillPaint);
            g.fill(path);
        }

        g.setStroke(stroke);
        g.setPaint(strokePaint);
        g.draw(path);
    }
}
