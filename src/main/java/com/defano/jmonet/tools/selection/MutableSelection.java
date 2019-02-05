package com.defano.jmonet.tools.selection;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class which manages the state or context of a modifiable image selection.
 */
public interface MutableSelection extends Selection {

    /**
     * Transitions to an un-selected state.
     *
     * Reset the selection boundary to its initial, no-selection state. {@link #getSelectionFrame()} should return
     * null following a selection reset, but prior to defining a new selection.
     */
    void resetSelection();

    /**
     * Specifies the selection rectangle (bounds) of the current selection, i.e., the rectangular path on which
     * "marching ants" will be drawn.
     *
     * @param bounds The new selection bounds.
     */
    void setSelectionOutline(Shape bounds);

    /**
     * Marks the selection as having been mutated (either by transformation or movement).
     */
    void setDirty();

    /**
     * Replaces the current selected image with the given image. It is the caller's responsibility to mask the given
     * image to assure it does not exceed the selection bounds.
     *
     * @param image The image with which to replace the selection.
     */
    void setSelectedImage(BufferedImage image);

    /**
     * Invoked to indicate that the selection should be moved on the canvas and therefore the selection shape's
     * coordinates should be translated accordingly.
     *
     * @param xDelta Number of pixels to move selection bounds horizontally.
     * @param yDelta Number of pixels to move selection bounds vertically.
     */
    void translateSelection(int xDelta, int yDelta);

}
