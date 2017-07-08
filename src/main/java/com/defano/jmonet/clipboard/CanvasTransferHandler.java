package com.defano.jmonet.clipboard;

import com.defano.jmonet.canvas.AbstractPaintCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    @Override
    protected Transferable createTransferable(JComponent c) {
        return TransferableImage.from(delegate.copySelection());
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == MOVE) {
            delegate.deleteSelection();
        }
    }

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


    private static class TransferableImage implements Transferable {

        private final BufferedImage image;

        private TransferableImage(BufferedImage image) {
            this.image = image;
        }

        public static TransferableImage from(BufferedImage image) {
            return image == null ? null : new TransferableImage(image);
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor == DataFlavor.imageFlavor) {
                return image;
            }

            throw new UnsupportedFlavorException(flavor);
        }
    }

}
