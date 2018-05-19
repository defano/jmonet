package com.defano.jmonet.tools.selection;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a selection that can be transformed in a way that also modifies the committed canvas image.
 */
public interface TransformableCanvasSelection extends MutableSelection {

    /**
     * Makes image bounded by the selection frame appear to have been deleted by filling the selection frame with paint
     * in the remove-scratch buffer.
     *
     * This method does not mark the selection as dirty, nor does it commit this change to the canvas. Callers are
     * expected to invoke {@link #setDirty()} or commit the scratch buffer to the canvas if this change is intended to
     * be permanent.
     */
    void eraseSelectedPixelsFromCanvas();

    /**
     * Add the graphics underneath the selection to the currently selected image. Has no effect if a selection is not
     * active or has not been dirtied.
     *
     * This method "picks up" the paint underneath the selection (that wasn't part of the paint initially
     * picked up when the selection bounds were defined).
     */
    default void pickupSelection() {

        if (hasSelection()) {
            Shape selectionBounds = getSelectionFrame();
            redrawSelection(false);
            BufferedImage maskedSelection = crop(getCanvas().render());

            BufferedImage trimmedSelection = maskedSelection.getSubimage(
                    Math.max(0, selectionBounds.getBounds().x),
                    Math.max(0, selectionBounds.getBounds().y),
                    Math.min(selectionBounds.getBounds().width, maskedSelection.getWidth() - selectionBounds.getBounds().x),
                    Math.min(selectionBounds.getBounds().height, maskedSelection.getHeight() - selectionBounds.getBounds().y)
            );

            BufferedImage currentSelection = getSelectedImage();
            BufferedImage newSelection = new BufferedImage(currentSelection.getWidth(), currentSelection.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = (Graphics2D) newSelection.getGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.drawImage(trimmedSelection, 0, 0, null);
            g2d.drawImage(getSelectedImage(), 0, 0, null);
            g2d.dispose();

            setSelectedImage(newSelection);

            eraseSelectedPixelsFromCanvas();
            redrawSelection(true);
        }
    }

}
