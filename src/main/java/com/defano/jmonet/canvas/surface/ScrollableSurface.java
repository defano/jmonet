package com.defano.jmonet.canvas.surface;

import java.awt.*;

public interface ScrollableSurface {

    /**
     * Gets the delegate responsible for the changing the scroll position of the entity (i.e.,
     * {@link javax.swing.JScrollPane} that holds this canvas as its viewport.
     *
     * @return The current scroll controller
     */
    SurfaceScrollController getSurfaceScrollController();

    void setSurfaceScrollController(SurfaceScrollController surfaceScrollController);

    Point getScrollError();
}
