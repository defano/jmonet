package com.defano.jmonet.clipboard;

import com.defano.jmonet.canvas.AbstractPaintCanvas;

public interface CanvasFocusDelegate {
    /**
     * Invoked to retrieve the canvas currently in focus that should receive cut, copy and paste actions.
     *
     * @return The canvas that should receive cut, copy and paste commands, or null if no canvas is currently in
     * focus.
     */
    AbstractPaintCanvas getCanvasInFocus();
}
