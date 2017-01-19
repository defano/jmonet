package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.surface.*;
import com.defano.jmonet.model.Provider;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Canvas extends ScalableScratchDrawable, Surface {
    boolean isVisible();

    void setCursor(Cursor cursor);

    void addObserver(CanvasCommitObserver observer);
    boolean removeObserver(CanvasCommitObserver observer);

    void invalidateCanvas();

    void commit();
    void commit(AlphaComposite composite);

    /**
     * Adds an AWT component to the canvas.
     * @param component The component to add.
     */
    void addComponent(Component component);

    /**
     * Removes an AWT component from the canvas
     * @param component The component to remove; has no effect if the component is not a child of this canvas.
     */
    void removeComponent(Component component);

    /**
     * Adds a listener to canvas interaction (mouse and keyboard) events.
     * @param listener The listener to add
     */
    void addCanvasInteractionListener(CanvasInteractionObserver listener);

    /**
     * Removes an existing canvas interaction listener; has no effect if the listener is not currently an observer of
     * this canvas.
     *
     * @param listener The listener to remove.
     * @return True if the listener was successfully removed; false otherwise.
     */
    boolean removeCanvasInteractionListener(CanvasInteractionObserver listener);

    void setScrollPosition(double percentX, double percentY);

    void setScale(double scale);
    Provider<Double> getScaleProvider();

    void setGridSpacing(int grid);
    Provider<Integer> getGridSpacingProvider();

    BufferedImage getCanvasImage();
    BufferedImage getScratchImage();

    void setCanvasImage(BufferedImage image);
    void setScratchImage(BufferedImage image);

    void clearScratch();

    void setSize(int width, int height);

    Rectangle getBounds();
    int getHeight();
    int getWidth();
}
