package com.defano.jmonet.tools;


import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MagnifierTool extends PaintTool {

    private double scale = 1.0;
    private double magnificationStep = 2;

    public MagnifierTool() {
        super(PaintToolType.MAGNIFIER);
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
    public void activate(com.defano.jmonet.canvas.Canvas canvas) {
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
}
