package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.observable.CanvasCommitObserver;
import com.defano.jmonet.canvas.observable.ObservableSurface;
import com.defano.jmonet.canvas.surface.*;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A canvas that can be painted upon by the paint tools.
 */
public interface PaintCanvas extends ScaledLayeredImage, Scrollable, ObservableSurface, CompositeSurface {

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
     * Causes the Swing framework to redraw/refresh the canvas by calling {@link Component#repaint()}.
     */
    void invalidateCanvas();

    /**
     * Commits the contents of the scratch buffer to the canvas, using the {@link AlphaComposite#SRC_OVER} composite
     * mode.
     */
    void commit();

    /**
     * Commits the given {@link ChangeSet} to the canvas.
     * @param changeSet The {@link ChangeSet} to be committed.
     */
    void commit(ChangeSet changeSet);

    /**
     * Sets the scale factor of the canvas. Values greater than 1 result in the canvas image appearing enlarged; values
     * less than 1 result in the canvas image appearing shrunken.
     *
     * @param scale The scale factor
     */
    void setScale(double scale);

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
     * Gets the "scratch" image associated with this drawable. The "scratch" image is a temporary buffer allowing
     * tools to draw on the canvas in a way that doesn't affect the underlying graphic.
     *
     * @return The scratch buffer image.
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
     * Sets the size of the canvas component (i.e., the size of the paintable image not including any scale). As such,
     * the size of the canvas will not match necessarily match the size of the image returned by
     * {@link #getCanvasImage()} when the scale factor is a value other than 1.0.
     *
     * @param width The desired width of the paintable image
     * @param height The desired height of the paintable image
     */
    void setSize(int width, int height);

    /**
     * Gets the bounds of the canvas component. This value will not necessarily match the size of the paintable image
     * when a scale factor other than 1.0 has been applied.
     * @return The bounds of the canvas component.
     */
    Rectangle getBounds();

    /**
     * Gets the background color of the canvas, typically the panel color specified by the current Swing look-and-feel's
     * UIManager.
     * @return The canvas color.
     */
    Color getCanvasColor();
}
