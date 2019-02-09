package com.defano.jmonet.clipboard;

import com.defano.jmonet.canvas.PaintCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * A basic implementation of {@link CanvasFocusDelegate} that returns {@link PaintCanvas} component currently in focus
 * (according to the {@link javax.swing.FocusManager}, or null, if no canvas currently has focus.
 */
public class DefaultCanvasFocusDelegate implements CanvasFocusDelegate {

    @Override
    public PaintCanvas getCanvasInFocus() {
        Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();

        if (focusOwner instanceof PaintCanvas) {
            return (PaintCanvas) focusOwner;
        }

        return null;
    }
}
