package com.defano.jmonet.tools.cursors;

import com.defano.jmonet.canvas.PaintCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * A CursorManager that sets a canvas cursor on the Swing dispatch thread.
 */
public class SwingCursorManager implements CursorManager {

    private Cursor toolCursor;

    /** {@inheritDoc} */
    @Override
    public Cursor getToolCursor() {
        return toolCursor;
    }

    /** {@inheritDoc} */
    @Override
    public void setToolCursor(Cursor toolCursor, PaintCanvas canvas) {
        this.toolCursor = toolCursor;
        if (canvas != null) {
            SwingUtilities.invokeLater(() -> canvas.setCursor(toolCursor));
        }
    }

}
