package com.defano.jmonet.canvas.surface;

import javax.swing.*;
import java.awt.*;

/**
 * An object that can be scrolled within a {@link javax.swing.JScrollPane}.
 *
 * This interface allows tools to advise scroll position changes to the {@link javax.swing.JScrollPane} (or other
 * container) holding the Canvas. This enables tools like the {@link com.defano.jmonet.tools.MagnifierTool} to change
 * the scroll positioning to match the position of the canvas being "zoomed in" on.
 */
public interface SurfaceScrollController {

    /**
     * Scrolls the canvas so that the center of the viewport is displayed at the specified, relative, offset.
     * For example, if the image in the viewport is 1000x1000px and the specified scroll position is .5 and .5,
     * then coordinate (500,500) of the viewport will visible in the center of the view.
     *
     * @param percentX The percent to scroll in the horizontal direction, between 0 and 1.0
     * @param percentY The percent to scroll in the vertical direction, between 0 and 1.0
     */
    void setScrollPosition(double percentX, double percentY);

    /**
     * Resets the scroll position to the last value specified by {@link #setScrollPosition(double, double)}.
     */
    void resetScrollPosition();

    /**
     * Gets the visible rectangle of the surface in the scroll pane's viewport. Equivalent to invoking
     * {@link JViewport#getViewRect()}.
     *
     * @return The visible rectangle of the scroll pane's view port.
     */
    Rectangle getViewRect();
}
