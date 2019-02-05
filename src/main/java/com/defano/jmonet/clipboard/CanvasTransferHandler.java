package com.defano.jmonet.clipboard;

import com.defano.jmonet.canvas.AbstractPaintCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A {@link TransferHandler} for JMonet canvas images.
 */
public class CanvasTransferHandler extends TransferHandler {

    private final CanvasTransferDelegate delegate;

    public CanvasTransferHandler(AbstractPaintCanvas canvas, CanvasTransferDelegate delegate) {
        this.delegate = delegate;

        // Register canvas for cut/copy/paste actions
        ActionMap map = canvas.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
    }

    /** {@inheritDoc} */
    @Override
    protected Transferable createTransferable(JComponent c) {
        return TransferableImage.from(delegate.copySelection());
    }

    /** {@inheritDoc} */
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /** {@inheritDoc} */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == MOVE) {
            delegate.deleteSelection();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        try {
            delegate.pasteSelection(toBufferedImage((Image) info.getTransferable().getTransferData(DataFlavor.imageFlavor)));
            return true;
        } catch (UnsupportedFlavorException | IOException ignored) {
            // Nothing to do
        }
        return false;
    }

    /**
     * Converts an image to a BufferedImage of type ARGB.
     * @param source The image to convert
     * @return The resulting BufferedImage
     */
    private BufferedImage toBufferedImage(Image source) {
        if (source instanceof BufferedImage) {
            return (BufferedImage) source;
        }

        // Create a buffered image with transparency
        BufferedImage dest = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D g = dest.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        return dest;
    }


}
