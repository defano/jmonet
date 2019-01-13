package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class PathTool extends BasicTool implements SurfaceInteractionObserver {

    private Point lastPoint;

    public PathTool(PaintToolType type) {
        super(type);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /**
     * Begins drawing a path on the given graphics context.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     * @param initialPoint The first point defined on the path
     */
    public abstract void startPath(Scratch scratch, Stroke stroke, Paint fillPaint, Point initialPoint);

    /**
     * Adds a point to the path begun via a call to {@link #startPath(Scratch, Stroke, Paint, Point)}.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     * @param lastPoint The last point added to the current path
     * @param thisPoint The new point to add to the current path
     */
    public abstract void addPoint(Scratch scratch, Stroke stroke, Paint fillPaint, Point lastPoint, Point thisPoint);

    /**
     * Completes the path begun via a call to {@link #startPath(Scratch, Stroke, Paint, Point)}.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param fillPaint The paint with which to draw
     */
    public abstract void completePath(Scratch scratch, Stroke stroke, Paint fillPaint);

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        getScratch().clear();

        startPath(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), imageLocation);
        lastPoint = imageLocation;

        getCanvas().repaint(getScratch().getDirtyRegion());
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        addPoint(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), lastPoint, imageLocation);
        lastPoint = imageLocation;

        // While mouse is down, only repaint the modified area of the canvas
        getCanvas().repaint(getScratch().getDirtyRegion());
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        completePath(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint());
        getCanvas().commit(getScratch().getLayerSet());
    }

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }
}
