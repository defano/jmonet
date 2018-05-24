package com.defano.jmonet.canvas.surface;

import javax.swing.*;

public interface ScrollableDelegate {

    /**
     * Gets the {@link JScrollPane} that this canvas in embedded in.
     *
     * @return The {@link JScrollPane} that this canvas is embedded in, or null, if this object is not the viewport
     * component of a scroll pane.
     */
    JScrollPane getScrollPane();
}
