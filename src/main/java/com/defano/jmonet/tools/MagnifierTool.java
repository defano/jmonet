package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.surface.SurfaceScrollController;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Tool that changes the scale factor of the canvas and adjusts the scroll position to re-center the scaled image
 * in the scrollable viewport.
 */
public class MagnifierTool extends PaintTool {

    private Cursor zoomInCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private Cursor zoomOutCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private double magnificationStep = 2;
    private boolean recenter = true;

    public MagnifierTool() {
        super(PaintToolType.MAGNIFIER);
        setToolCursor(zoomInCursor);
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        setToolCursor(e.isShiftDown() ? zoomOutCursor : zoomInCursor);
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        setToolCursor(e.isShiftDown() ? zoomOutCursor : zoomInCursor);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point clickLoc) {

        if (e.isControlDown() || e.isAltDown() || e.isMetaDown()) {
            reset();
        }

        else if (e.isShiftDown()) {
            zoomOut(e.getX(), e.getY());
        }

        else {
            zoomIn();
        }

        SurfaceScrollController scrollController = getCanvas().getSurfaceScrollController();
        if (recenter && scrollController != null) {
            Rectangle visibleRect = scrollController.getScrollRect();

            clickLoc = getCanvas().scalePoint(clickLoc);
            clickLoc.translate(-visibleRect.width / 2, -visibleRect.height / 2);

            scrollController.setScrollPosition(clickLoc);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);
    }

    private void reset() {
        getCanvas().setScale(1.0);

        if (getCanvas().getSurfaceScrollController() != null) {
            getCanvas().getSurfaceScrollController().setScrollPosition(new Point(0, 0));
        }
    }

    private void zoomIn() {
        getCanvas().setScale(getCanvas().getScale() + magnificationStep);
    }

    private void zoomOut(int x, int y) {
        double scale = getCanvas().getScale() - magnificationStep;

        if (scale <= 1.0) {
            reset();
            return;
        }

        getCanvas().setScale(scale);
    }

    public Cursor getZoomInCursor() {
        return zoomInCursor;
    }

    public void setZoomInCursor(Cursor zoomInCursor) {
        this.zoomInCursor = zoomInCursor;
    }

    public Cursor getZoomOutCursor() {
        return zoomOutCursor;
    }

    public void setZoomOutCursor(Cursor zoomOutCursor) {
        this.zoomOutCursor = zoomOutCursor;
    }

    public double getMagnificationStep() {
        return magnificationStep;
    }

    public void setMagnificationStep(double magnificationStep) {
        this.magnificationStep = magnificationStep;
    }

    public boolean isRecenter() {
        return recenter;
    }

    public void setRecenter(boolean recenter) {
        this.recenter = recenter;
    }
}
