package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Mouse and keyboard handler for tools that define a free-form path on the canvas by clicking and dragging.
 */
public abstract class AbstractPathTool extends PaintTool {

    protected Point lastPoint;

    /**
     * Begins drawing a path on the given graphics context.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     * @param initialPoint The first point defined on the path
     */
    protected abstract void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint);

    /**
     * Adds a point to the path begun via a call to {@link #startPath(Scratch, Stroke, Paint, Point)}.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     * @param lastPoint The last point added to the current path
     * @param thisPoint The new point to add to the current path
     */
    protected abstract void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint);

    /**
     * Completes the path begun via a call to {@link #startPath(Scratch, Stroke, Paint, Point)}.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     */
    protected void completePath(Scratch scratch, Stroke stroke, Paint fillPaint) {}

    public AbstractPathTool(PaintToolType type) {
        super(type);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        getScratch().clear();

        startPath(getScratch(), getStroke(), getStrokePaint(), imageLocation);
        lastPoint = imageLocation;
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        addPoint(getScratch(), getStroke(), getStrokePaint(), lastPoint, imageLocation);

        lastPoint = imageLocation;
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        completePath(getScratch(), getStroke(), getStrokePaint());

        getCanvas().commit(getScratch().getChangeSet());
    }
}
