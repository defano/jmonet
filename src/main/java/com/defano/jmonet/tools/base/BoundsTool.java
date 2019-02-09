package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.MathUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A tool that draws shapes by defined by a bounding box, like rectangles, round-rectangles, and ovals.
 *
 * Click to define the first point in the bounding box, then drag to define the second. There are no restrictions
 * on the relative location of the two points.
 */
public class BoundsTool extends BasicTool<BoundsToolDelegate> implements SurfaceInteractionObserver {

    private Point initialPoint;
    private Point currentPoint;

    public BoundsTool(PaintToolType type) {
        super(type);
    }

    /** {@inheritDoc} */
    @Override
    public Cursor getDefaultCursor() {
        return new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point canvasLoc) {
        currentPoint = canvasLoc;

        if (!getAttributes().isDrawMultiple()) {
            getScratch().clear();
        }

        Point originPoint = new Point(initialPoint);

        if (getAttributes().isDrawCentered()) {
            int height = currentPoint.y - initialPoint.y;
            int width = currentPoint.x - initialPoint.x;

            originPoint.x = initialPoint.x - width / 2;
            originPoint.y = initialPoint.y - height / 2;
        }

        Rectangle bounds = e.isShiftDown() ?
                MathUtils.square(originPoint, currentPoint) :
                MathUtils.rectangle(originPoint, currentPoint);

        getAttributes().getFillPaint().ifPresent(paint ->
                getDelegate().fillBounds(getScratch(), paint, new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), e.isShiftDown()));

        getDelegate().strokeBounds(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), e.isShiftDown());
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point canvasLoc) {
        getCanvas().commit();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point canvasLoc) {
        setToolCursor(getToolCursor());
    }

    /**
     * Gets the initial point defined by this tool, or null if no point has yet been defined.
     * @return The initial point
     */
    public Point getInitialPoint() {
        return initialPoint;
    }

    /**
     * Gets the last point defined by this tool, or null if no point has yet been defined.
     * @return The current point.
     */
    public Point getCurrentPoint() {
        return currentPoint;
    }
}
