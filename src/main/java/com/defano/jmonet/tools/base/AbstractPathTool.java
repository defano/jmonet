package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.ChangeSet;
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
     * @param g The graphics context on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     * @param initialPoint The first point defined on the path
     */
    protected abstract void startPath(Graphics2D g, Stroke stroke, Paint fillPaint, Point initialPoint);

    /**
     * Adds a point to the path begun via a call to {@link #startPath(Graphics2D, Stroke, Paint, Point)}.
     *
     * @param g The graphics context on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     * @param lastPoint The last point added to the current path
     * @param thisPoint The new point to add to the current path
     */
    protected abstract void addPoint(Graphics2D g, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint);

    /**
     * Completes the path begun via a call to {@link #startPath(Graphics2D, Stroke, Paint, Point)}.
     *
     * @param g The graphics context on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     */
    protected void completePath(Graphics2D g, Stroke stroke, Paint fillPaint) {}

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
        getCanvas().clearScratch();

        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        startPath(g2d, getStroke(), getStrokePaint(), imageLocation);
        g2d.dispose();

        lastPoint = imageLocation;
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        addPoint(g2d, getStroke(), getStrokePaint(), lastPoint, imageLocation);
        g2d.dispose();

        lastPoint = imageLocation;
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        completePath(g2d, getStroke(), getStrokePaint());
        g2d.dispose();

        getCanvas().commit(new ChangeSet(getCanvas().getScratchImage(), getComposite()));
    }
}
