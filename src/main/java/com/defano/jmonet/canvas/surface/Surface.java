package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.CanvasInteractionObserver;

import java.awt.*;

public interface Surface {
    void addComponent(Component component);

    void removeComponent(Component component);

    void addCanvasInteractionListener(CanvasInteractionObserver listener);

    boolean removeCanvasInteractionListener(CanvasInteractionObserver listener);
}
