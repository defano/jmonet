package com.defano.jmonet.canvas.observable;

/**
 * Represents an AWT component that can contain other components and report on keyboard and mouse interactions taking
 * place inside of it.
 *
 * A surface has no layout manager, so elements added to are displayed in a fixed location based on their bounds.
 */
public interface ObservableSurface {

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
    @SuppressWarnings("UnusedReturnValue")
    boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener);
}
