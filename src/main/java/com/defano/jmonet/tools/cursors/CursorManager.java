package com.defano.jmonet.tools.cursors;

import com.defano.jmonet.canvas.PaintCanvas;

import java.awt.*;

/**
 * A class which has the ability to get and set the system's mouse cursor when the mouse is hovering over a specified
 * {@link PaintCanvas}.
 */
public interface CursorManager {

    /**
     * Gets the default mouse cursor used when painting with this tool. Note that specific tools may provide methods
     * for getting and setting auxiliary cursors that are active during tool-specific states, too.
     *
     * @return The default tool cursor.
     */
    Cursor getToolCursor();

    /**
     * Sets the default mouse cursor used when painting with this tool.
     *
     * @param toolCursor The default mouse cursor.
     * @param canvas The canvas on which the cursor should be active.
     */
    void setToolCursor(Cursor toolCursor, PaintCanvas canvas);

}
