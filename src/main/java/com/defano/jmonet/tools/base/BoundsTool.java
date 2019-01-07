package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

public class BoundsTool extends BasicTool implements SurfaceInteractionObserver {

    private BoundsToolDelegate boundsToolDelegate;
    private Point initialPoint;
    private Point currentPoint;

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }

    public BoundsTool(PaintToolType type) {
        super(type);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        if (boundsToolDelegate == null) {
            throw new IllegalStateException("Bounds tool delegate not set.");
        }

        currentPoint = imageLocation;

        if (!getToolAttributes().isDrawMultiple()) {
            getScratch().clear();
        }

        Point originPoint = new Point(initialPoint);

        if (getToolAttributes().isDrawCentered()) {
            int height = currentPoint.y - initialPoint.y;
            int width = currentPoint.x - initialPoint.x;

            originPoint.x = initialPoint.x - width / 2;
            originPoint.y = initialPoint.y - height / 2;
        }

        Rectangle bounds = e.isShiftDown() ?
                Geometry.square(originPoint, currentPoint) :
                Geometry.rectangle(originPoint, currentPoint);

        if (getToolAttributes().getFillPaint().isPresent()) {
            boundsToolDelegate.fillBounds(getScratch(), getToolAttributes().getFillPaint().get(), new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), e.isShiftDown());
        }

        boundsToolDelegate.strokeBounds(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), e.isShiftDown());
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    protected BoundsToolDelegate getBoundsToolDelegate() {
        return boundsToolDelegate;
    }

    protected void setBoundsToolDelegate(BoundsToolDelegate boundsToolDelegate) {
        this.boundsToolDelegate = boundsToolDelegate;
    }

    public Point getInitialPoint() {
        return initialPoint;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }
}
