package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.CanvasInteractionObserver;

public interface InteractiveSurface {

    /**
     * Adds a listener to canvas interaction (mouse and keyboard) events.
     * @param listener The listener to add
     */
    void addCanvasInteractionListener(CanvasInteractionObserver listener);

    /**
     * Removes an existing canvas interaction listener; has no effect if the listener is not currently an observer of
     * this canvas.
     *
     * @param listener The listener to remove.
     * @return True if the listener was successfully removed; false otherwise.
     */
    boolean removeCanvasInteractionListener(CanvasInteractionObserver listener);

}
