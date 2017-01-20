package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MagnifierTool extends PaintTool {

    private Cursor zoomInCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private Cursor zoomOutCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private double scale = 1.0;
    private final double magnificationStep = 2;

    public MagnifierTool() {
        super(PaintToolType.MAGNIFIER);
        setToolCursor(zoomInCursor);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        setToolCursor(e.isShiftDown() ? zoomOutCursor : zoomInCursor);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        setToolCursor(e.isShiftDown() ? zoomOutCursor : zoomInCursor);
    }

    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        double pX = (double) imageLocation.x / (double) getCanvas().getCanvasImage().getWidth();
        double pY = (double) imageLocation.y / (double) getCanvas().getCanvasImage().getHeight();

        if (e.isControlDown() || e.isAltDown() || e.isMetaDown()) {
            reset();
        }

        else if (e.isShiftDown()) {
            zoomOut(e.getX(), e.getY());
            getCanvas().setScrollPosition(pX, pY);
        }

        else {
            zoomIn();
            getCanvas().setScrollPosition(pX, pY);
        }
    }

    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);
        this.scale = canvas.getScaleProvider().get();
    }

    private void reset() {
        scale = 1.0;
        getCanvas().setScale(scale);
        getCanvas().setScrollPosition(0, 0);
    }

    private void zoomIn() {
        scale += magnificationStep;
        getCanvas().setScale(scale);
    }

    private void zoomOut(int x, int y) {
        scale -= magnificationStep;

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
}
