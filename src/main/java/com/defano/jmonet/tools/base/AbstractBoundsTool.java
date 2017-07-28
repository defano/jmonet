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

    private Cursor boundaryCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

    public AbstractBoundsTool(PaintToolType type) {
        super(type);
        setToolCursor(boundaryCursor);
    }

    protected abstract void drawBounds(Graphics2D g, Stroke stroke, Paint paint, int x, int y, int width, int height);
    protected abstract void drawFill(Graphics2D g, Paint fill, int x, int y, int width, int height);

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
                Geometry.squareAtAnchor(originPoint, currentPoint) :
                Geometry.rectangleFromPoints(originPoint, currentPoint);

        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();

        if (getFillPaint() != null) {
            drawFill(g2d, getFillPaint(), bounds.x, bounds.y, bounds.width, bounds.height);
        }

        drawBounds(g2d, getStroke(), getStrokePaint(), bounds.x, bounds.y, bounds.width, bounds.height);

        g2d.dispose();
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit();
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

    public Cursor getBoundaryCursor() {
        return boundaryCursor;
    }

    public void setBoundaryCursor(Cursor boundaryCursor) {
        this.boundaryCursor = boundaryCursor;
        setToolCursor(boundaryCursor);
    }
}
