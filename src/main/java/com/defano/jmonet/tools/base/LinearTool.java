package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class LinearTool extends BasicTool implements SurfaceInteractionObserver {

    private Point initialPoint;

    public LinearTool(PaintToolType type) {
        super(type);
    }

    /**
     * Draws a line from (x1, y1) to (x2, y2) on the given graphics context.
     *
     * @param scratch The scratch buffer on which to draw
     * @param stroke The stroke with which to draw
     * @param paint The paint with which to draw
     * @param x1 First x coordinate of the line
     * @param y1 First y coordinate of the line
     * @param x2 Second x coordinate of the line
     * @param y2 Second y coordinate of the line
     */
    public abstract void drawLine(Scratch scratch, Stroke stroke, Paint paint, int x1, int y1, int x2, int y2);

    @Override
    public Cursor getDefaultCursor() {
        return new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        getScratch().clear();

        Point currentLoc = imageLocation;

        if (e.isShiftDown()) {
            currentLoc = Geometry.line(initialPoint, currentLoc, getAttributes().getConstrainedAngle());
        }

        drawLine(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), initialPoint.x, initialPoint.y, currentLoc.x, currentLoc.y);
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit();
    }

}
