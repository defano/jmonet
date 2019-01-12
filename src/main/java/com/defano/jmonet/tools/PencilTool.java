package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PathTool;
import com.defano.jmonet.tools.base.PathToolDelegate;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.util.CursorFactory;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool for drawing or erasing a single-pixel, free-form path on the canvas.
 */
public class PencilTool extends PathTool implements PathToolDelegate {

    // Flag indicating whether pencil is operating in eraser mode
    private boolean isErasing = false;

    public PencilTool() {
        super(PaintToolType.PENCIL);
        setPathToolDelegate(this);
        setToolCursor(CursorFactory.makePencilCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint) {
        ToolAttributes attributes = getToolAttributes();

        Color pixel = new Color(getCanvas().getCanvasImage().getRGB(initialPoint.x, initialPoint.y), true);

        // Pencil erases when user begins stoke over a "marked" pixel, otherwise pencil marks canvas
        isErasing = attributes.getMarkPredicate().isMarked(pixel, attributes.getEraseColor());

        renderStroke(scratch, fillPaint, new Line2D.Float(initialPoint, initialPoint));
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint) {
        renderStroke(scratch, fillPaint, new Line2D.Float(lastPoint, thisPoint));
    }

    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint fillPaint) {
        // Nothing to do
    }

    private void renderStroke(Scratch scratch, Paint fillPaint, Line2D line) {
        if (isErasing) {
            scratch.erase(this, line, new BasicStroke(1));
        }

        else {
            GraphicsContext g = scratch.getAddScratchGraphics(this, new BasicStroke(1), line);
            g.setStroke(new BasicStroke(1));
            g.setPaint(fillPaint);
            g.draw(line);
        }
    }

}
