package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LinearTool extends BasicTool implements SurfaceInteractionObserver {

    private Point initialPoint;
    private LineToolDelegate lineToolDelegate;

    public LinearTool(PaintToolType type) {
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
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        if (lineToolDelegate == null) {
            throw new IllegalStateException("Line tool delegate not set.");
        }

        getScratch().clear();

        Point currentLoc = imageLocation;

        if (e.isShiftDown()) {
            currentLoc = Geometry.line(initialPoint, currentLoc, getToolAttributes().getConstrainedAngle());
        }

        lineToolDelegate.drawLine(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), initialPoint.x, initialPoint.y, currentLoc.x, currentLoc.y);
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit();
    }

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }

    protected LineToolDelegate getLineToolDelegate() {
        return lineToolDelegate;
    }

    protected void setLineToolDelegate(LineToolDelegate lineToolDelegate) {
        this.lineToolDelegate = lineToolDelegate;
    }
}
