package com.defano.jmonet.clipboard;

import com.defano.jmonet.canvas.PaintCanvas;

/**
 * A determiner of which JMonet canvas presently has focus.
 */
public interface CanvasFocusDelegate {

    /**
     * Invoked to retrieve the canvas currently in focus, which should receive cut, copy and paste actions.
     *
     * @return The canvas that should receive cut, copy and paste commands, or null if no canvas is currently in
     * focus.
     */
    PaintCanvas getCanvasInFocus();
}
