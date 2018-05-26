package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.Disposable;
import com.defano.jmonet.canvas.observable.ObservableSurface;

import java.awt.*;

public interface Surface extends GridSurface, ScalableSurface, Disposable, SwingSurface, ObservableSurface, ScrollableSurface {

    /**
     * Causes the surface to be repainted by Swing.
     */
    void repaint();

    /**
     * Determines if the canvas is visible.
     *
     * @return True if visible; false otherwise.
     */
    boolean isVisible();

    /**
     * Sets whether the canvas is visible. When invisible, the component hierarchy will be drawn as though this
     * component does not exist.
     *
     * @param visible True to make this canvas invisible; false for visible.
     */
    void setVisible(boolean visible);

    /**
     * Gets the mouse cursor that is displayed when the mouse is within the bounds of this component.
     *
     * @return The active cursor
     */
    Cursor getCursor();

    /**
     * Sets the mouse cursor that is displayed when the mouse is within the bounds of this component.
     *
     * @param cursor The active cursor to display
     */
    void setCursor(Cursor cursor);
}
