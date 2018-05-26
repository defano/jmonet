package com.defano.jmonet.canvas.surface;

import java.awt.*;

/**
 * A surface needs to control and monitor its scroll position as a viewport view inside of a
 * {@link javax.swing.JScrollPane}.
 * <p>
 * Typically, when scrolling a component, the component being scrolled does not need to be aware of its positioning
 * inside the scroll pane. However, some tools (like the magnifier) need to be able to request scroll position changes
 * to be effective. This layer of indirection allows that to occur without having to embed the
 * {@link javax.swing.JScrollPane} in the canvas itself.
 */
public interface ScrollableSurface {

    /**
     * Gets the delegate responsible for the changing the scroll position of the entity (i.e.,
     * {@link javax.swing.JScrollPane} that holds this surface as its viewport.
     *
     * @return The current scroll controller
     */
    SurfaceScrollController getSurfaceScrollController();

    /**
     * Sets the delegate responsible for changing the scroll position of the entity (i.e.,
     * {@link javax.swing.JScrollPane} that holds this surface as its viewport.
     * <p>
     * Some layouts may require a custom scroll controller to account for the sizing and layout of the surface relative
     * to other components placed alongside it in the viewport.
     *
     * @param surfaceScrollController The scroll controller to use with this surface.
     */
    void setSurfaceScrollController(SurfaceScrollController surfaceScrollController);

    /**
     * Gets the number of pixels horizontal and vertical that have scrolled in the viewport but which have not affected
     * the canvas image.
     * <p>
     * A surface is always drawn such that a full row and column of pixels are aligned with the top-left corner of
     * the surface component. When scale is less than or equal to 1.0, this limitation has no impact on scroll. However,
     * with larger scales this implies that the user can change the scroll position some amount before the image is
     * redrawn.
     * <p>
     * For example, if scale = 64 the first row of pixels will remain aligned to the top of the component when the
     * vertical scroll position is any value less than 64. Thus, if the user scrolls 16 pixels down, the image has not
     * changed but the view coordinate space has. This method provides a mechanism for taking this "error" into account.
     *
     * @return The number of pixels that have scrolled in the viewport but which have not impacted the canvas image
     * rendering. Always returns (0,0) when scale is less than or equal to 1.0.
     */
    Point getScrollError();
}
