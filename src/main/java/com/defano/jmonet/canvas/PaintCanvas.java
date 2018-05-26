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
     * Commits the contents of the scratch buffer to the canvas, using the {@link AlphaComposite#SRC_OVER} composite
     * mode.
     */
    void commit();

    /**
     * Commits the given {@link ImageLayerSet} to the canvas.
     *
     * @param imageLayerSet The {@link ImageLayerSet} to be committed.
     */
    void commit(ImageLayerSet imageLayerSet);

    /**
     * Clears the canvas by filling the remove-scratch buffer and committing the change.
     */
    void clearCanvas();

    /**
     * Gets the Scratch buffer associated with this canvas. The scratch buffer provides a mechanism for tools to draw
     * ephemeral changes (like marching ants, text tool caret, etc.) to the canvas without actually modifying the
     * underlying image.
     *
     * @return The scratch buffer.
     */
    Scratch getScratch();

    /**
     * Gets the image that has been painted on this canvas, not including any ephemeral changes that have been made via
     * the scratch buffer but have not been comitted to the canvas.
     *
     * @return The canvas image.
     */
    BufferedImage getCanvasImage();

    /**
     * Gets the (un-scaled) dimensions of the canvas (that is, the size of the image which can be painted). This
     * dimension is unrelated to the size of the Swing component that displays/encapsulates it.
     *
     * @return The un-scaled dimensions of this surface.
     */
    Dimension getCanvasSize();

    /**
     * Specifies the (un-scaled) size of this painting surface. This determines the size of the image that can be
     * painted, but is unrelated to the size of Swing component that displays/encapsulates it.
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
}
