package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.surface.SurfaceScrollController;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BasicTool;
import com.defano.jmonet.tools.util.CursorFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Tool that changes the scale factor of the canvas and adjusts the scroll position to re-center the scaled image
 * in the scrollable viewport.
 */
public class MagnifierTool extends BasicTool implements SurfaceInteractionObserver {

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

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
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

    /**
     * Multiplies the scale of the canvas this tool is currently active on by the value returned from
     * {@link #getMagnificationStep()}. Does not change the scroll position of the canvas.
     */
    public void zoomIn() {
        getCanvas().setScale(Math.min(maximumScale, getCanvas().getScale() * magnificationStep));
    }

    /**
     * Divides the scale of the canvas this tool is currently active on by the value returned from
     * {@link #getMagnificationStep()}. Does not change the scroll position of the canvas.
     */
    public void zoomOut() {
        getCanvas().setScale(Math.max(minimumScale, getCanvas().getScale() / magnificationStep));
    }

    /**
     * Gets the cursor displayed when the user presses the control or meta key (indicating they wish to reset the canvas
     * scale to 1.0).
     *
     * @return The reset-zoom cursor.
     */
    public Cursor getZoomCursor() {
        return zoomCursor;
    }

    /**
     * Sets the cursor displayed when the user presses the control or meta key (indicating they wish to reset the canvas
     * scale to 1.0)
     *
     * @param zoomCursor The reset-zoom cursor.
     */
    public void setZoomCursor(Cursor zoomCursor) {
        this.zoomCursor = zoomCursor;
    }

    /**
     * Gets the cursor displayed when the user is pressing no modifier keys (indicating they wish to zoom-in on the
     * canvas).
     *
     * @return The zoom-in cursor.
     */
    public Cursor getZoomInCursor() {
        return zoomInCursor;
    }

    /**
     * Sets the cursor displayed when the user is pressing no modifier keys (indicating they wish to zoom-in on the
     * canvas).
     *
     * @param zoomInCursor The zoom-in cursor.
     */
    public void setZoomInCursor(Cursor zoomInCursor) {
        this.zoomInCursor = zoomInCursor;
    }

    /**
     * Gets the cursor that is displayed when the user is pressing the shift key (indicating they wish to zoom out from
     * the canvas).
     *
     * @return The zoom-out cursor.
     */
    public Cursor getZoomOutCursor() {
        return zoomOutCursor;
    }

    /**
     * Sets the cursor displayed when the user is pressing the shift key (indicating they wish to zoom out from the
     * canvas).
     *
     * @param zoomOutCursor The zoom-out cursor.
     */
    public void setZoomOutCursor(Cursor zoomOutCursor) {
        this.zoomOutCursor = zoomOutCursor;
    }

    /**
     * Gets the value by which the canvas scale factor is multiplied or divided when zooming in or zooming out. For
     * example, when this value is 2.0, zooming in causes the scale factor to be multiplied by 2 thus scaling the
     * canvas from its original size to 2x, then 4x, then 8x and so forth.
     *
     * @return The zoom in/out scale multiple
     */
    public double getMagnificationStep() {
        return magnificationStep;
    }

    /**
     * Sets the value by which the canvas scale factor is multiplied or divided when zooming in or zooming out. For
     * example, when this value is 2.0, zooming in causes the scale factor to be multiplied by 2 thus scaling the
     * canvas from its original size to 2x, then 4x, then 8x and so forth.
     *
     * @param magnificationStep The zoom in/out scale multiple
     */
    public void setMagnificationStep(double magnificationStep) {
        this.magnificationStep = magnificationStep;
    }

    /**
     * Gets whether the canvas scroll position should be updated when zooming in or out to recenter the pixel that was
     * clicked with the magnifier tool. Has no effect when the active canvas is not embedded in a scroll pane and
     * managed via a {@link SurfaceScrollController}.
     *
     * @return True if the clicked pixel will be centered in the scroll pane when zooming in or out; false otherwise.
     */
    public boolean isRecenter() {
        return recenter;
    }

    /**
     * Sets whether the canvas scroll position should be updated when zooming in or out to recenter the pixel that was
     * clicked with the magnifier tool. Has no effect when the active canvas is not embedded in a scroll pane and
     * managed via a {@link SurfaceScrollController}.
     *
     * @param recenter True to cause the clicked pixel to be centered in the scroll pane when zooming in or out.
     */
    public void setRecenter(boolean recenter) {
        this.recenter = recenter;
    }

    /**
     * Gets the minimum allowable scale factor that this tool can adjust the active canvas to. For example, when set
     * to 1.0 the magnifier tool will not be able to zoom out (shrink the image) past its normal size.
     *
     * @return The minimum allowable scale factor this tool will adjust to.
     */
    public double getMinimumScale() {
        return minimumScale;
    }

    /**
     * Sets the minimum allowable scale factor that this tool can adjust the active canvas to. For example, when set
     * to 1.0 the magnifier tool will not be able to zoom out (shrink the image) past its normal size.

     * @param minimumScale The minimum allowable scale factor this tool will adjust to.
     */
    public void setMinimumScale(double minimumScale) {
        this.minimumScale = minimumScale;
    }

    /**
     * Gets the maximum allowable scale factor that this tool can adjust the active canvas to. For example, when set
     * to 32.0 the magnifier tool will not be able to magnify the canvas greater than 32x.
     *
     * @return The maximum allowable scale factor this tool will adjust to.
     */
    public double getMaximumScale() {
        return maximumScale;
    }

    /**
     * Gets the maximum allowable scale factor that this tool can adjust the active canvas to. For example, when set
     * to 32.0 the magnifier tool will not be able to magnify the canvas greater than 32x.
     *
     * @param maximumScale The maximum allowable scale factor this tool will adjust to.
     */
    public void setMaximumScale(double maximumScale) {
        this.maximumScale = maximumScale;
    }
}
