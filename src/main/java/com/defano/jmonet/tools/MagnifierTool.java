package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.surface.SurfaceScrollController;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;
import com.defano.jmonet.tools.util.CursorFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Tool that changes the scale factor of the canvas and adjusts the scroll position to re-center the scaled image
 * in the scrollable viewport.
 */
public class MagnifierTool extends PaintTool {

    private Cursor zoomCursor = CursorFactory.makeZoomCursor();
    private Cursor zoomInCursor = CursorFactory.makeZoomInCursor();
    private Cursor zoomOutCursor = CursorFactory.makeZoomOutCursor();

    private double minimumScale = 1.0;
    private double maximumScale = 32.0;
    private double magnificationStep = 2;
    private boolean recenter = true;

    public MagnifierTool() {
        super(PaintToolType.MAGNIFIER);
        SwingUtilities.invokeLater(() -> setToolCursor(zoomInCursor));
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        if (e.isMetaDown() || e.isControlDown() || e.isAltDown()) {
            setToolCursor(zoomCursor);
        } else if (e.isShiftDown()) {
            setToolCursor(zoomOutCursor);
        } else {
            setToolCursor(zoomInCursor);
        }
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
            getCanvas().setScale(1.0);
        } else if (e.isShiftDown()) {
            zoomOut();
        } else {
            zoomIn();
        }

        SurfaceScrollController scrollController = getCanvas().getSurfaceScrollController();
        if (recenter && scrollController != null) {
            recenter(scrollController, clickLoc);
        }
    }

    private void recenter(SurfaceScrollController controller, Point point) {
        Rectangle visibleRect = controller.getScrollRect();

        point = getCanvas().scalePoint(point);
        point.translate(-visibleRect.width / 2, -visibleRect.height / 2);
        point.x = Math.max(0, point.x);
        point.y = Math.max(0, point.y);

        controller.setScrollPosition(point);
    }

    private void zoomIn() {
        getCanvas().setScale(Math.min(maximumScale, getCanvas().getScale() * magnificationStep));
    }

    private void zoomOut() {
        getCanvas().setScale(Math.max(minimumScale, getCanvas().getScale() / magnificationStep));
    }

    public Cursor getZoomCursor() {
        return zoomCursor;
    }

    public void setZoomCursor(Cursor zoomCursor) {
        this.zoomCursor = zoomCursor;
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

    public double getMinimumScale() {
        return minimumScale;
    }

    public void setMinimumScale(double minimumScale) {
        this.minimumScale = minimumScale;
    }

    public double getMaximumScale() {
        return maximumScale;
    }

    public void setMaximumScale(double maximumScale) {
        this.maximumScale = maximumScale;
    }
}
