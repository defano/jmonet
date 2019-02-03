package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PathTool;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.base.PathToolDelegate;
import com.defano.jmonet.tools.cursors.CursorFactory;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Tool for drawing or erasing a single-pixel, free-form path on the canvas.
 */
public class PencilTool extends PathTool implements PathToolDelegate {

    // Flag indicating whether pencil is operating in eraser mode
    private boolean isErasing = false;

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    PencilTool() {
        super(PaintToolType.PENCIL);
        setDelegate(this);
    }

    @Override
    public Cursor getDefaultCursor() {
        return CursorFactory.makePencilCursor();
    }

    /** {@inheritDoc} */
    @Override
    public void startPath(Scratch scratch, Stroke stroke, Paint strokePaint, Point initialPoint) {
        ToolAttributes attributes = getAttributes();

        Color pixel = new Color(getCanvas().getCanvasImage().getRGB(initialPoint.x, initialPoint.y), true);

        // Pencil erases when user begins stoke over a "marked" pixel, otherwise pencil marks canvas
        isErasing = attributes.getMarkPredicate().isMarked(pixel, attributes.getEraseColor());

        renderStroke(scratch, strokePaint, new Line2D.Float(initialPoint, initialPoint));
    }

    /** {@inheritDoc} */
    @Override
    public void addPoint(Scratch scratch, Stroke stroke, Paint strokePaint, Point lastPoint, Point thisPoint) {
        renderStroke(scratch, strokePaint, new Line2D.Float(lastPoint, thisPoint));
    }

    @Override
    public void completePath(Scratch scratch, Stroke stroke, Paint strokePaint, Paint fillPaint) {
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
