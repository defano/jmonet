package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.SurfaceInteractionObserver;

import java.awt.*;

/**
 * Represents an AWT component that can contain other components and report on keyboard and mouse interactions taking
 * place inside of it.
 *
 * A surface has no layout manager, so elements added to are displayed in a fixed location based on their bounds.
 */
public interface Surface {
    /**
     * Adds a component to the surface.
     * @param component The component to be added.
     */
    void addComponent(Component component);

    /**
     * Removes a component from the surface; has no effect if the given component is not a child of this surface.
     * @param component
     */
    void removeComponent(Component component);

    /**
     * Adds an observer to mouse and keyboard events taking place within this surface.
     * @param listener The observer to add
     */
    void addSurfaceInteractionObserver(SurfaceInteractionObserver listener);

    /**
     * Removes an observer that was previously listening for UI events.
     * @param listener The observer to be removed
     * @return True if the listener was previously registered as an observer and was successfully removed
     */
    boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener);
}
