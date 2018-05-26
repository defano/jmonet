package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.canvas.layer.ScaledLayeredImage;
import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.surface.*;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A canvas that can be painted upon by the paint tools.
 */
public interface PaintCanvas extends Surface, ScaledLayeredImage {

    /**
     * Sets whether the canvas is visible. When invisible, the component hierarchy will be drawn as though this
     * component does not exist.
     * @param visible True to make this canvas invisible; false for visible.
     */
    void setVisible(boolean visible);

    /**
     * Determines if the canvas is visible.
     * @return True if visible; false otherwise.
     */
    boolean isVisible();

    /**
     * Sets the mouse cursor that is displayed when the mouse is within the bounds of this component.
     * @param cursor The active cursor to display
     */
    void setCursor(Cursor cursor);

    /**
     * Gets the mouse cursor that is displayed when the mouse is within the bounds of this component.
     * @return The active cursor
     */
    Cursor getCursor();

    /**
     * Adds an observer to be notified of scratch buffer commits.
     *
     * @param observer The observer to be registered.
     */
    void addCanvasCommitObserver(CanvasCommitObserver observer);

    /**
     * Removes an existing observer.
     *
     * @param observer The observer to be removed.
     * @return True if the given observer was successfully unregistered; false otherwise.
     */
    boolean removeCanvasCommitObserver(CanvasCommitObserver observer);

    /**
     * Causes the canvas to be repainted by Swing.
     */
    void repaint();

    /**
     * Commits the contents of the scratch buffer to the canvas, using the {@link AlphaComposite#SRC_OVER} composite
     * mode.
     */
    void commit();

    /**
     * Commits the given {@link ImageLayerSet} to the canvas.
     * @param imageLayerSet The {@link ImageLayerSet} to be committed.
     */
    void commit(ImageLayerSet imageLayerSet);

    /**
     * Gets an observable scale factor.
     * @return The scale factor {@link BehaviorSubject}
     */
    Observable<Double> getScaleObservable();

    /**
     * Sets a grid spacing on which the mouse coordinates provided to the paint tools will be "snapped to".
     * @param grid The grid spacing
     */
    void setGridSpacing(int grid);

    /**
     * Gets an observable grid spacing property.
     * @return The grid spacing {@link BehaviorSubject}
     */
    Observable<Integer> getGridSpacingObservable();

    /**
     * Clears the canvas by filling the scratch buffer with all white pixels, and then committing this change with
     * a DST_OUT composite mode.
     */
    void clearCanvas();

    /**
     * Gets the Scratch object associated with this canvas.
     *
     * @return The scratch buffer.
     */
    Scratch getScratch();

    /**
     * Gets the image represented by this drawable; not including any ephemeral changes that have been made--but not
     * committed--to the canvas.
     *
     * @return The canvas image.
     */
    BufferedImage getCanvasImage();

    /**
     * Gets the un-scaled dimensions of the canvas (that is, the size of the image which can be painted). This dimension
     * is unrelated to the size of the Swing component that displays/encapsulates it.
     *
     * @return The un-scaled dimensions of this surface.
     */
    Dimension getCanvasSize();

    /**
     * Specifies the un-scaled size of this painting surface. This determines the size of the image that can be painted,
     * but is unrelated to the size of Swing component that displays/encapsulates it.
     *
     * @param surfaceDimensions The dimensions of the painting surface
     */
    void setCanvasSize(Dimension surfaceDimensions);

    /**
     * Gets the background color of the canvas, that is, the {@link Paint} which is displayed behind transparent pixels
     * in the painted image.
     *
     * @return The canvas background.
     */
    Paint getCanvasBackground();

    /**
     * Specifies the canvas background paint, that is, the color or pattern displayed behind transparent pixels in the
     * painted image (like a checkerboard pattern or a solid color).
     *
     * @param paint The canvas background
     */
    void setCanvasBackground(Paint paint);

    /**
     * Gets the delegate responsible for the changing the scroll position of the entity (i.e.,
     * {@link javax.swing.JScrollPane} that holds this canvas as its viewport.
     *
     * @return The current scroll controller
     */
    SurfaceScrollController getSurfaceScrollController();

}
