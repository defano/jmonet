package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.layer.ImageLayerSet;

public interface Undoable {

    /**
     * Undoes the previous committed change. May be called successively to revert committed changes one-by-one until
     * the undo buffer is exhausted.
     *
     * @return The ChangeSet that was undone by this operation, or null if there were no undoable changes.
     */
    ImageLayerSet undo();

    /**
     * Reverts the previous undo; has no effect if a commit was made following the previous undo.
     *
     * @return True if the redo was successful, false if there is no undo available to revert.
     */
    boolean redo();

    /**
     * Determines if a commit is available to be undone.
     *
     * @return True if {@link #undo()} will succeed; false otherwise.
     */
    boolean hasUndoableChanges();

    /**
     * Determines if an undo is available to revert.
     *
     * @return True if {@link #redo()} will succeed; false otherwise.
     */
    boolean hasRedoableChanges();

    /**
     * Gets the maximum depth of the undo buffer.
     *
     * @return The maximum number of undos that are supported by this PaintCanvas.
     */
    int getMaxUndoBufferDepth();

    /**
     * Gets the number of changes that can be "undone".
     *
     * @return The depth of undo buffer.
     */
    int getUndoBufferDepth();

    /**
     * Gets the number of changes that can be "redone".
     *
     * @return The depth of the redo buffer.
     */
    int getRedoBufferDepth();

}
