package com.defano.jmonet.tools;


import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.surface.SurfaceScrollController;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.base.BasicTool;
import com.defano.jmonet.tools.cursors.CursorFactory;

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

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    MagnifierTool() {
        super(PaintToolType.MAGNIFIER);
        SwingUtilities.invokeLater(() -> setToolCursor(zoomInCursor));
    }

    @Override
    public Cursor getDefaultCursor() {
        return zoomInCursor;
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
        if (getAttributes().isRecenterOnMagnify() && scrollController != null) {
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
     * {@link ToolAttributes#getMagnificationStep()}. Does not change the scroll position of the canvas.
     */
    public void zoomIn() {
        double maximumScale = getAttributes().getMaximumScale();
        double magnificationStep = getAttributes().getMagnificationStep();

        getCanvas().setScale(Math.min(maximumScale, getCanvas().getScale() * magnificationStep));
    }

    /**
     * Divides the scale of the canvas this tool is currently active on by the value returned from
     * {@link ToolAttributes#getMagnificationStep()}. Does not change the scroll position of the canvas.
     */
    public void zoomOut() {
        double minimumScale = getAttributes().getMinimumScale();
        double magnificationStep = getAttributes().getMagnificationStep();

        getCanvas().setScale(Math.max(minimumScale, getCanvas().getScale() / magnificationStep));
    }

    /**
     * Gets the cursor displayed when the user presses the control or meta key (indicating they wish to reset the canvas
     * scale to 1.0).
     *
     * @return The reset-zoom cursor.
     */
    @SuppressWarnings("unused")
    public Cursor getZoomCursor() {
        return zoomCursor;
    }

    /**
     * Sets the cursor displayed when the user presses the control or meta key (indicating they wish to reset the canvas
     * scale to 1.0)
     *
     * @param zoomCursor The reset-zoom cursor.
     */
    @SuppressWarnings("unused")
    public void setZoomCursor(Cursor zoomCursor) {
        this.zoomCursor = zoomCursor;
    }

    /**
     * Gets the cursor displayed when the user is pressing no modifier keys (indicating they wish to zoom-in on the
     * canvas).
     *
     * @return The zoom-in cursor.
     */
    @SuppressWarnings("unused")
    public Cursor getZoomInCursor() {
        return zoomInCursor;
    }

    /**
     * Sets the cursor displayed when the user is pressing no modifier keys (indicating they wish to zoom-in on the
     * canvas).
     *
     * @param zoomInCursor The zoom-in cursor.
     */
    @SuppressWarnings("unused")
    public void setZoomInCursor(Cursor zoomInCursor) {
        this.zoomInCursor = zoomInCursor;
    }

    /**
     * Gets the cursor that is displayed when the user is pressing the shift key (indicating they wish to zoom out from
     * the canvas).
     *
     * @return The zoom-out cursor.
     */
    @SuppressWarnings("unused")
    public Cursor getZoomOutCursor() {
        return zoomOutCursor;
    }

    /**
     * Sets the cursor displayed when the user is pressing the shift key (indicating they wish to zoom out from the
     * canvas).
     *
     * @param zoomOutCursor The zoom-out cursor.
     */
    @SuppressWarnings("unused")
    public void setZoomOutCursor(Cursor zoomOutCursor) {
        this.zoomOutCursor = zoomOutCursor;
    }

}
