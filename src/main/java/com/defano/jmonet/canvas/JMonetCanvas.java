package com.defano.jmonet.canvas;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A paint tools canvas with a built-in undo and redo buffer. Extends the capabilities inherent in {@link AbstractPaintCanvas}.
 */
public class JMonetCanvas extends AbstractPaintCanvas {

    private final int maxUndoBufferDepth;

    private int undoBufferPointer = -1;                     // An internal index into the list of changes; moves left and right to denote undo/redo
    private BufferedImage permanent;                        // Image elements that are no longer undoable; null until the undo depth has been exceeded.
    private List<ChangeSet> undoBuffer = new ArrayList<>(); // List of changes as they're committed from the scratch buffer; lower indices are older; higher indices are newer

    private BehaviorSubject<Boolean> isUndoableSubject = BehaviorSubject.createDefault(false);
    private BehaviorSubject<Boolean> isRedoableSubject = BehaviorSubject.createDefault(false);

    /**
     * Creates a new canvas with a given image initially displayed in it with
     * a specified undo buffer depth.
     *
     * @param initialImage The image to be displayed in the canvas.
     * @param undoBufferDepth The depth of the undo buffer (number of undo operations)
     */
    public JMonetCanvas(BufferedImage initialImage, int undoBufferDepth) {
        super();
        this.maxUndoBufferDepth = undoBufferDepth;
        setSize(initialImage.getWidth(), initialImage.getHeight());
        makePermanent(new ChangeSet(initialImage));
    }

    /**
     * Creates a new canvas with a 1x1 transparent image displayed inside it
     * and a specified undo buffer depth.
     *
     * @param undoBufferDepth The depth of the undo buffer (number of undo operations)
     */
    public JMonetCanvas(int undoBufferDepth) {
        this(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), undoBufferDepth);
    }

    public JMonetCanvas(BufferedImage initialImage) {
        this(initialImage, 12);
    }

    public JMonetCanvas() {
        this(12);
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

            isUndoableSubject.onNext(hasUndoableChanges());
            isRedoableSubject.onNext(hasRedoableChanges());

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

            isUndoableSubject.onNext(hasUndoableChanges());
            isRedoableSubject.onNext(hasRedoableChanges());

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
     * Gets an observable of the {@link #hasUndoableChanges()} property.
     * @return An observable of when the undo operation is supported.
     */
    public Observable<Boolean> isUndoableObservable() {
        return isUndoableSubject;
    }

    /**
     * Gets an observable of the {@link #hasRedoableChanges()} property.
     * @return An observable of when the redo operation is supported.
     */
    public Observable<Boolean> isRedoableObservable() {
        return isRedoableSubject;
    }

    /** {@inheritDoc} */
    @Override
    public void commit(ChangeSet changeSet) {

        // Special case: ChangeSet may be modified after it has been committed; listen for this so that we can notify observers of our own
        changeSet.addChangeSetObserver(() -> fireCanvasCommitObservers(JMonetCanvas.this, null, getCanvasImage()));

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

        isUndoableSubject.onNext(hasUndoableChanges());
        isRedoableSubject.onNext(hasRedoableChanges());
    }

    /**
     * Applies a {@link ChangeSet} to the permanent (not-undoable) layer of the canvas. Invoked when a committed change
     * has been evicted from the undo buffer as a result of exceeding its depth, or when applying an initial, base image
     * at construction.
     *
     * If there is no permanent image in place, the given image is made permanent. Otherwise, the given image is
     * drawn atop the permanent image.
     *
     */
    private void makePermanent(ChangeSet changeSet) {
        Dimension changeSetDim = changeSet.getImageSize();

        if (permanent == null) {
            permanent = new BufferedImage(changeSetDim.width, changeSetDim.height, BufferedImage.TYPE_INT_ARGB);
        } else if (changeSetDim.width > permanent.getWidth() || changeSetDim.height > permanent.getHeight()) {
            resizePermanent(changeSetDim);
        }

        applyChangeSet(changeSet, permanent);
    }

    /** {@inheritDoc} */
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

    private void resizePermanent(Dimension dim) {
        BufferedImage newPerm = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        if (permanent != null) {
            Graphics2D g = newPerm.createGraphics();
            g.drawImage(permanent, 0, 0, null);
            g.dispose();
        }
        permanent = newPerm;
    }
}
