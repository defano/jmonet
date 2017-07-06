package com.defano.jmonet.clipboard;

import com.defano.jmonet.tools.base.AbstractSelectionTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface CanvasTransferDelegate {

    /**
     * Invoked when the user issued to "Copy" command. A typical implementation of this method will delegate
     * to {@link AbstractSelectionTool#getSelectedImage()}.
     *
     * @return The image to be copied to the clipboard, or null if there is no selection to be copied.
     */
    BufferedImage copySelection();

    /**
     * Invoked to delete the current selection as a result of completing a "Cut" command. A typical implementation
     * of this method will delegate to {@link AbstractSelectionTool#deleteSelection()}.
     *
     * Note that a "Cut" command is comprised of a {@link #copySelection()} and {@link #deleteSelection()}.
     */
    void deleteSelection();

    /**
     * Invoked to paste the given image onto the canvas. A typical implementation of this method might activate the
     * {@link com.defano.jmonet.tools.SelectionTool} on the canvas, then invoke
     * {@link com.defano.jmonet.tools.SelectionTool#createSelection(BufferedImage, Point)} to make the pasted image
     * the current selection.
     *
     * @param image The image to paste onto the focused canvas.
     */
    void pasteSelection(BufferedImage image);
}
