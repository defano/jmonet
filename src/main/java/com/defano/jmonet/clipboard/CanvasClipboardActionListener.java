package com.defano.jmonet.clipboard;

import com.defano.jmonet.canvas.AbstractPaintCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener of clipboard-related actions (cut, copy and paste).
 */
@SuppressWarnings("unused")
public class CanvasClipboardActionListener implements ActionListener {

    private final CanvasFocusDelegate delegate;

    public CanvasClipboardActionListener(CanvasFocusDelegate delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractPaintCanvas focusedCanvas = delegate == null ? null : delegate.getCanvasInFocus();

        if (focusedCanvas != null) {
            Action a = focusedCanvas.getActionMap().get(e.getActionCommand());
            if (a != null) {
                try {
                    a.actionPerformed(new ActionEvent(focusedCanvas, ActionEvent.ACTION_PERFORMED, null));
                } catch (Throwable ignored) {
                    // Nothing to do
                }
            }
        }
    }

}
