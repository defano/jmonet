package com.defano.jmonet.canvas.surface;

import java.awt.*;

/**
 * A surface to which Swing components can be added and removed.
 */
public interface CompositeSurface {
    /**
     * Adds a component to the surface.
     * @param component The component to be added.
     */
    void addComponent(Component component);

    /**
     * Removes a component from the surface; has no effect if the given component is not a child of this surface.
     * @param component The component to remove
     */
    void removeComponent(Component component);
}
