package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PathTool extends BasicTool<PathToolDelegate> implements SurfaceInteractionObserver {

    private Point lastPoint;

    public PathTool(PaintToolType type) {
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
        getScratch().clear();

        getDelegate().startPath(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), imageLocation);
        lastPoint = imageLocation;

        getCanvas().repaint(getScratch().getDirtyRegion());
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point canvasLoc) {
        getDelegate().addPoint(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), lastPoint, canvasLoc);
        lastPoint = canvasLoc;

        // While mouse is down, only repaint the modified area of the canvas
        getCanvas().repaint(getScratch().getDirtyRegion());
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point canvasLoc) {
        getDelegate().completePath(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), getAttributes().getFillPaint().orElse(null));
        getCanvas().commit(getScratch().getLayerSet());
    }

}
