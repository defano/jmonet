package com.defano.jmonet.canvas.surface;

import java.awt.*;

public interface CompositeSurface {

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
}
