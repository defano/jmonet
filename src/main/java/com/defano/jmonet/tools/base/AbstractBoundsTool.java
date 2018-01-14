package com.defano.jmonet.tools.base;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.model.ImmutableProvider;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Mouse and keyboard event handler for tools that define a bounding box by clicking and dragging
 * the mouse from the top-left point of the bounds to the bottom-right point.
 *
 * When the shift key is held down the bounding box is constrained to a square whose height and width is equal to the
 * larger of the two dimensions defined by the mouse location.
 */
public abstract class AbstractBoundsTool extends PaintTool {

    private Provider<Boolean> drawMultiple = new Provider<>(false);
    private Provider<Boolean> drawCentered = new Provider<>(false);

    protected Point initialPoint;
    protected Point currentPoint;

    public AbstractBoundsTool(PaintToolType type) {
        super(type);
    }

    /**
     * Draws the stroke (outline) of a shape described by a rectangular boundary.
     *
     * @param g The graphics context on which to draw
     * @param stroke The stroke with which to draw
     * @param paint The paint with which to draw
     * @param bounds The bounds of the shape to draw
     * @param isShiftDown True to indicate that the user is holding the shift key; implementers may use this flag to
     *                    constrain the bounds or otherwise modify the tool behavior.
     */
    protected abstract void strokeBounds(Graphics2D g, Stroke stroke, Paint paint, Rectangle bounds, boolean isShiftDown);

    /**
     * Fills a shape described by a rectangular boundary.
     *
     * @param g The graphics context on which to draw
     * @param fill The paint with which to fill the shape
     * @param bounds The bounds of the shape to draw
     * @param isShiftDown True to indicate that the user is holding the shift key; implementers may use this flag to
     *                    constrain the bounds or otherwise modify the tool behavior.
     */
    protected abstract void fillBounds(Graphics2D g, Paint fill, Rectangle bounds, boolean isShiftDown);

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        currentPoint = imageLocation;

        if (!drawMultiple.get()) {
            getCanvas().clearScratch();
        }

        Point originPoint = new Point(initialPoint);

        if (drawCentered.get()) {
            int height = currentPoint.y - initialPoint.y;
            int width = currentPoint.x - initialPoint.x;

            originPoint.x = initialPoint.x - width / 2;
            originPoint.y = initialPoint.y - height / 2;
        }

        Rectangle bounds = e.isShiftDown() ?
                Geometry.square(originPoint, currentPoint) :
                Geometry.rectangle(originPoint, currentPoint);

        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();

        if (getFillPaint() != null) {
            fillBounds(g2d, getFillPaint(), new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), e.isShiftDown());
        }

        strokeBounds(g2d, getStroke(), getStrokePaint(), new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), e.isShiftDown());

        g2d.dispose();
        getCanvas().invalidateCanvas();
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

    public ImmutableProvider<Boolean> getDrawMultiple() {
        return drawMultiple;
    }

    public void setDrawMultiple(Provider<Boolean> drawMultiple) {
        this.drawMultiple = drawMultiple;
    }

    public Provider<Boolean> getDrawCentered() {
        return drawCentered;
    }

    public void setDrawCentered(Provider<Boolean> drawCentered) {
        this.drawCentered = drawCentered;
    }
}
