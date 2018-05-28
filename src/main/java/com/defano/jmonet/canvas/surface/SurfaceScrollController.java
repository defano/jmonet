package com.defano.jmonet.canvas.surface;

import javax.swing.*;
import java.awt.*;

/**
 * A mechanism enabling a surface to request that its scrolled position (within a {@link javax.swing.JScrollPane}) be
 * changed.
 *
 * This interface allows tools to advise scroll position changes to the {@link javax.swing.JScrollPane} (or other
 * container) holding the surface. This enables changes to scale to intelligently "zoom in" on the area being scaled and
 * for tools like the {@link com.defano.jmonet.tools.MagnifierTool} to recenter the scroll position.
 */
public interface SurfaceScrollController {

    /**
     * Sets the top-left position of the surface that appears in the viewport of the controlled {@link JScrollPane}.
     * This coordinate is specified in scaled dimensions (that is, the point (20,20) makes pixel (10,10) of the
     * displayed surface visible in the top-left corner when scale is 2.0). Has no effect if the surface is not
     * embedded in a {@link JScrollPane} managed by this surface controller.
     *
     * @param position The position of the surface image to be displayed in the top-left corner of the viewport, in
     *                 scaled dimensions.
     */
    void setScrollPosition(Point position);

    /**
     * Gets the visible rectangle of the surface in the scroll pane's viewport, represented in scaled coordinates.
     * Returns an empty rectangle (0, 0, 0, 0) if this surface is not within a {@link JScrollPane} or is not being
     * managed by this scroll controller.
     *
     * @return The visible rectangle of the scroll pane's view port.
     */
    Rectangle getScrollRect();

}
