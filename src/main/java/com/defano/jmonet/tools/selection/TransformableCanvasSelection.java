package com.defano.jmonet.tools.selection;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a selection that can be transformed in a way that modifies the committed canvas image.
 */
public interface TransformableCanvasSelection extends MutableSelection {

    /**
     * Makes image bounded by the selection frame appear to have been deleted by filling the selection frame with paint
     * in the remove-scratch buffer.
     * <p>
     * This method does not mark the selection as dirty, nor does it commit this change to the canvas. Callers are
     * expected to invoke {@link #setDirty()} or commit the scratch buffer to the canvas if this change is intended to
     * be permanent.
     */
    void eraseSelectedPixelsFromCanvas();

    /**
     * Add the graphics underneath the selection to the currently selected image. Has no effect if a selection is not
     * active or has not been dirtied.
     * <p>
     * This method "picks up" the paint underneath the selection (that wasn't part of the paint initially
     * picked up when the selection bounds were defined).
     */
    default void pickupSelection() {

        if (hasSelection()) {
            setDirty();

            // Draw current selection without marching ants
            redrawSelection(false);

            // Grab pixels from scratch and canvas that are bounded by the selection
            BufferedImage maskedSelection = crop(getCanvas().render());

            // Resize to smallest bounds for performance
            Shape selectionBounds = getSelectionFrame();
            BufferedImage trimmedSelection = maskedSelection.getSubimage(
                    Math.max(0, selectionBounds.getBounds().x),
                    Math.max(0, selectionBounds.getBounds().y),
                    Math.min(selectionBounds.getBounds().width, maskedSelection.getWidth() - selectionBounds.getBounds().x),
                    Math.min(selectionBounds.getBounds().height, maskedSelection.getHeight() - selectionBounds.getBounds().y)
            );

            // Update the current selection
            setSelectedImage(trimmedSelection);
            eraseSelectedPixelsFromCanvas();

            // And redraw once more, this time with ants
            redrawSelection(true);
        }
    }

}
