package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.observable.ObservableSurface;

import java.awt.*;

/**
 * A component that can be painted and that provides methods for drawing at scale, snap-to-grid behavior, scrolling,
 * observables, and embedding Swing components.
 */
@SuppressWarnings("unused")
public interface PaintSurface
        extends ScanlineSurface, GridSurface, SwingSurface, ObservableSurface, ScrollableSurface, Disposable
{
    /**
     * Specifies the un-scaled size of this painting surface. This determines the size of the image (document) that can
     * be painted by a user. This does not specify the size of the Swing component or otherwise adjust layout or
     * presentation of the canvas.
     *
     * @param dimension The dimensions of the painting surface
     */
    void setSurfaceDimension(Dimension dimension);

    /**
     * Gets the un-scaled size of this painting surface (that is, the size of the image being painted).
     *
     * @return The dimensions of the image being painted
     */
    Dimension getSurfaceDimension();

    /**
     * Causes the entire surface to be repainted by Swing. Note that repainting large regions is computationally
     * expensive, whenever possible tools should repaint the smallest sub-region possible using the
     * {@link #repaint(Rectangle)} method.
     */
    void repaint();

    /**
     * Causes a section of the surface to be repainted by Swing. This is the minimum rectangle that will be repainted;
     * there is no
     *
     * @param r The region of this surface to be repainted.
     */
    void repaint(Rectangle r);

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
