package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LinearTool extends BasicTool<LinearToolDelegate> implements SurfaceInteractionObserver {

    private Point initialPoint;

    public LinearTool(PaintToolType type) {
        super(type);
    }

    /** {@inheritDoc} */
    @Override
    public Cursor getDefaultCursor() {
        return new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point canvasLoc) {
        setToolCursor(getToolCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point canvasLoc) {
        getScratch().clear();

        Point currentLoc = canvasLoc;

        if (e.isShiftDown()) {
            currentLoc = Geometry.line(initialPoint, currentLoc, getAttributes().getConstrainedAngle());
        }

        getDelegate().drawLine(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), initialPoint.x, initialPoint.y, currentLoc.x, currentLoc.y);
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point canvasLoc) {
        getCanvas().commit();
    }

    public Point getInitialPoint() {
        return this.initialPoint;
    }
}
