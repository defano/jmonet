package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.google.inject.Inject;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class PathTool extends BasicTool implements SurfaceInteractionObserver {

    protected PathToolDelegate delegate;
    protected Point lastPoint;

    @Inject
    public PathTool(PaintToolType type)
    {
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
        if (delegate == null) {
            throw new IllegalStateException("Path tool delegate not set.");
        }

        getScratch().clear();

        delegate.startPath(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), imageLocation);
        lastPoint = imageLocation;
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        if (delegate == null) {
            throw new IllegalStateException("Path tool delegate not set.");
        }

        delegate.addPoint(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), lastPoint, imageLocation);

        lastPoint = imageLocation;
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        if (delegate == null) {
            throw new IllegalStateException("Path tool delegate not set.");
        }

        delegate.completePath(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint());

        getCanvas().commit(getScratch().getLayerSet());
    }

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }

    protected PathToolDelegate getPathToolDelegate() {
        return delegate;
    }

    protected void setPathToolDelegate(PathToolDelegate delegate) {
        this.delegate = delegate;
    }
}
