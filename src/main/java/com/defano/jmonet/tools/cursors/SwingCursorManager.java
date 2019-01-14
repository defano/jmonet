package com.defano.jmonet.tools.cursors;

import com.defano.jmonet.canvas.PaintCanvas;

import javax.swing.*;
import java.awt.*;

public class SwingCursorManager implements CursorManager {

    private Cursor toolCursor;

    public Cursor getToolCursor() {
        return toolCursor;
    }

    public void setToolCursor(Cursor toolCursor, PaintCanvas canvas) {
        this.toolCursor = toolCursor;
        if (canvas != null) {
            SwingUtilities.invokeLater(() -> canvas.setCursor(toolCursor));
        }
    }

}
