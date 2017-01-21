package com.defano.jmonet.canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A paint tools canvas with a built-in undo and redo buffer. Extends the capabilities inherent in {@link AbstractPaintCanvas}.
 */
public class UndoablePaintCanvas extends AbstractPaintCanvas {

    // TODO: Add constructor arg to set this value
    private final int maxUndoBufferDepth = 20;

    private int undoBufferPointer = -1;                     // An internal index into the list of changes; moves left and right to denote undo/redo
    private BufferedImage permanent;                        // Image elements that are no longer undoable; null until the undo depth has been exceeded.
    private List<ChangeSet> undoBuffer = new ArrayList<>(); // List of changes as they're committed from the scratch buffer; lower indices are older; higher indices are newer

    /**
     * Creates a new PaintCanvas with a given image initially displayed in it.
     * @param initialImage The image to be displayed in the canvas.
     */
    public UndoablePaintCanvas(BufferedImage initialImage) {
        super();
        makePermanent(new ChangeSet(initialImage));
    }

    /**
     * Undoes the previous committed change. Maybe called successively to revert committed changes one-by-one until
     * the undo buffer is exhausted.
     *
     * @return True if the undo was successful, false if there are no unable changes in the buffer.
     */
    public boolean undo() {

        if (hasUndoableChanges()) {
            undoBufferPointer--;
            fireCanvasCommitObservers(this,null, getCanvasImage());
            invalidateCanvas();
            return true;
        }

        return false;
    }

    /**
     * Reverts the previous undo; has no effect if a commit was made following the previous undo.
     * @return True if the redo was successful, false if there is no undo available to revert.
     */
    public boolean redo() {

        if (hasRedoableChanges()) {
            undoBufferPointer++;
            fireCanvasCommitObservers(this,null, getCanvasImage());
            invalidateCanvas();
            return true;
        }

        return false;
    }

    /**
     * Determines if a commit is available to be undone.
     *
     * @return True if {@link #undo()} will succeed; false otherwise.
     */
    public boolean hasUndoableChanges() {
        return undoBufferPointer >= 0;
    }

    /**
     * Determines if an undo is available to revert.
     *
     * @return True if {@link #redo()} will succeed; false otherwise.
     */
    public boolean hasRedoableChanges() {
        return undoBufferPointer < undoBuffer.size() - 1;
    }

    /**
     * Gets the maximum depth of the undo buffer.
     * @return The maximum number of undos that are supported by this PaintCanvas.
     */
    public int getMaxUndoBufferDepth() {
        return maxUndoBufferDepth;
    }

    /**
     * Commits the contents of the scratch buffer to the canvas.
     */
    @Override
    public void commit(ChangeSet changeSet) {

        // Special case: ChangeSet may be modified after it has been committed; listen for this so that we can notify observers of our own
        changeSet.addChangeSetObserver(() -> fireCanvasCommitObservers(UndoablePaintCanvas.this, null, getCanvasImage()));

        // Clear the redo elements from the buffer; can't perform redo after committing a new change
        undoBuffer = undoBuffer.subList(0, undoBufferPointer + 1);

        // Add the change to the redo buffer
        undoBuffer.add(changeSet);

        // If we've exceeded the max undo size, trim the buffer and write the evicted image element to the base canvas
        if (undoBuffer.size() > maxUndoBufferDepth) {
            makePermanent(undoBuffer.remove(0));
        }

        // Finally, move our pointer to the tail of the buffer
        undoBufferPointer = undoBuffer.size() - 1;

        fireCanvasCommitObservers(this, changeSet, getCanvasImage());

        clearScratch();
        invalidateCanvas();
    }

    /**
     * Applies a {@link ChangeSet} to the permanent (not-undoable) layer of the canvas. Invoked when a committed change has been
     * evicted from the undo buffer as a result of exceeding its depth, or when applying an initial, base image at
     * construction.
     *
     * If there is no permanent image in place, the given image is made permanent. Otherwise, the given image is
     * drawn atop the permanent image.
     *
     */
    private void makePermanent(ChangeSet changeSet) {
        if (permanent == null) {
            permanent = new BufferedImage(changeSet.getImage(0).getWidth(), changeSet.getImage(0).getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        applyChangeSet(changeSet, permanent);
    }

    /**
     * Renders the canvas image by successively applying each visible change in the buffer to the base, permanent
     * image.
     *
     * @return An image representing the current state of the canvas.
     */
    @Override
    public BufferedImage getCanvasImage() {
        BufferedImage visibleImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        if (permanent != null) {
            applyImage(permanent, visibleImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        for (int index = 0; index <= undoBufferPointer; index++) {
            applyChangeSet(undoBuffer.get(index), visibleImage);
        }

        return visibleImage;
    }
}
