package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.CanvasInteractionObserver;

public interface InteractiveSurface {
    void addCanvasInteractionListener(CanvasInteractionObserver listener);
    boolean removeCanvasInteractionListener(CanvasInteractionObserver listener);

}
