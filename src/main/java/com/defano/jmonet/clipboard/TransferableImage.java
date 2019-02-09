package com.defano.jmonet.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;

/**
 * A {@link Transferable} wrapper for BufferedImages, used to facilitate cut-copy-paste operations on JMonet images.
 */
class TransferableImage implements Transferable {

    private final BufferedImage image;

    private TransferableImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Creates a TransferableImage from a standard BufferedImage.
     *
     * @param image The BufferedImage for transfer.
     * @return The TransferableImage
     */
    public static TransferableImage from(BufferedImage image) {
        return image == null ? null : new TransferableImage(image);
    }

    /** {@inheritDoc} */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DataFlavor.imageFlavor;
    }

    /** {@inheritDoc} */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor == DataFlavor.imageFlavor) {
            return image;
        }

        throw new UnsupportedFlavorException(flavor);
    }
}
