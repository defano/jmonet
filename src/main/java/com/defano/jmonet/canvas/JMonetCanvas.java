package com.defano.jmonet.canvas;

import com.defano.jmonet.context.AwtGraphicsContext;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.transform.image.ApplyPixelTransform;
import com.defano.jmonet.transform.image.PixelTransform;
import com.defano.jmonet.transform.image.StaticImageTransform;
import com.defano.jmonet.transform.image.Transformable;
import com.defano.jmonet.canvas.layer.ImageLayer;
import com.defano.jmonet.canvas.layer.ImageLayerSet;
import com.defano.jmonet.canvas.layer.LayeredImage;
import com.defano.jmonet.canvas.observable.LayerSetObserver;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A paint canvas with a built-in undo and redo buffer.
 */
@SuppressWarnings("unused")
public class JMonetCanvas extends AbstractPaintCanvas implements LayerSetObserver, Transformable, Undoable {

    // Maximum number of allowable undo operations
    private final int maxUndoBufferDepth;

    // An internal index into the list of layer sets; moves left and right to denote undo/redo
    private final BehaviorSubject<Integer> undoBufferPointer = BehaviorSubject.createDefault(-1);

    // Image elements that are no longer undoable; null until the undo depth has been exceeded.
    private BufferedImage permanent;

    // List of changes as they're committed from the scratch buffer; lower indices are older; higher indices are newer
    private List<ImageLayerSet> undoBuffer = new ArrayList<>();

    // Cached copy of last computed canvas image (don't want to have to merge all layers together at each request)
    private BufferedImage cachedCanvasImage;

    // Hash of cachedCanvasImage (for detecting changes)
    private long cachedCanvasImageHash;

    /**
     * Creates a new canvas with a given image initially displayed in it with a specified undo buffer depth.
     *
     * @param initialImage    The image to be displayed in the canvas.
     * @param undoBufferDepth The depth of the undo buffer (number of undo operations)
     */
    @SuppressWarnings("WeakerAccess")
    public JMonetCanvas(BufferedImage initialImage, int undoBufferDepth) {
        super(new Dimension(initialImage.getWidth(), initialImage.getHeight()));
        this.maxUndoBufferDepth = Math.max(1, undoBufferDepth);
        setSize(initialImage.getWidth(), initialImage.getHeight());
        makePermanent(new ImageLayerSet(new ImageLayer(initialImage)));
    }

    /**
     * Creates a new canvas with a transparent image displayed inside it and a specified undo buffer depth.
     *
     * @param dimension       The size of the canvas.
     * @param undoBufferDepth The depth of the undo buffer (number of undo operations)
     */
    @SuppressWarnings("WeakerAccess")
    public JMonetCanvas(Dimension dimension, int undoBufferDepth) {
        this(new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB), undoBufferDepth);
    }

    /**
     * Create a new canvas with a default-sized undo/redo buffer, the size of, and containing the image of, the
     * specified {@link BufferedImage}.
     *
     * @param initialImage The initial image to be displayed.
     */
    public JMonetCanvas(BufferedImage initialImage) {
        this(initialImage, 12);
    }

    /**
     * Create a new canvas of a given dimension.
     *
     * @param dimension The size of the canvas
     */
    public JMonetCanvas(Dimension dimension) {
        this(dimension, 12);
    }

    /** {@inheritDoc} */
    @Override
    public ImageLayerSet undo() {

        if (hasUndoableChanges()) {
            ImageLayerSet undid = undoBuffer.get(undoBufferPointer.blockingFirst());

            undoBufferPointer.onNext(undoBufferPointer.blockingFirst() - 1);
            fireCanvasCommitObservers(this, null, getCanvasImage());
            this.repaint();

            return undid;
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean redo() {

        if (hasRedoableChanges()) {
            undoBufferPointer.onNext(undoBufferPointer.blockingFirst() + 1);
            fireCanvasCommitObservers(this, null, getCanvasImage());
            this.repaint();

            return true;
        }

        return false;
    }

    /**
     * Returns the {@link ImageLayerSet} representing the undo at the given depth in the buffer.
     *
     * @param index The index of the requested undoable change where index 0 is the most recent undoable change. The
     *              maximum legal index is equal to {@link #getUndoBufferDepth()} - 1.
     * @return The requested change set
     * @throws IndexOutOfBoundsException If there are no undoable changes in the buffer, or if the index equals or
     *                                   exceeds the number of undoable changes.
     */
    public ImageLayerSet peek(int index) {
        if (index >= getUndoBufferDepth()) {
            throw new IndexOutOfBoundsException("Index exceeds depth of undo buffer.");
        }

        return undoBuffer.get(undoBufferPointer.blockingFirst() - index);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasUndoableChanges() {
        return undoBufferPointer.blockingFirst() >= 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasRedoableChanges() {
        return undoBufferPointer.blockingFirst() < undoBuffer.size() - 1;
    }

    /** {@inheritDoc} */
    @Override
    public int getMaxUndoBufferDepth() {
        return maxUndoBufferDepth;
    }

    /** {@inheritDoc} */
    @Override
    public int getUndoBufferDepth() {
        return undoBufferPointer.blockingFirst() + 1;
    }

    /** {@inheritDoc} */
    @Override
    public int getRedoBufferDepth() {
        return undoBuffer.size() - undoBufferPointer.blockingFirst() - 1;
    }

    /**
     * Gets an observable of the {@link #hasUndoableChanges()} property.
     *
     * @return An observable of when the undo operation is supported.
     */
    public Observable<Boolean> isUndoableObservable() {
        return undoBufferPointer.map(integer -> hasUndoableChanges());
    }

    /**
     * Gets an observable of the {@link #hasRedoableChanges()} property.
     *
     * @return An observable of when the redo operation is supported.
     */
    public Observable<Boolean> isRedoableObservable() {
        return undoBufferPointer.map(integer -> hasRedoableChanges());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit(ImageLayerSet imageLayerSet) {

        // Special case: ChangeSet may be modified after it has been committed; listen for this so that we can notify observers of our own
        imageLayerSet.addLayerSetObserver(this);

        // Clear the redo elements from the buffer; can't perform redo after committing a new change
        undoBuffer = undoBuffer.subList(0, undoBufferPointer.blockingFirst() + 1);

        // Add the change to the undo buffer
        undoBuffer.add(imageLayerSet);

        // If we've exceeded the max undo size, trim the buffer and write the evicted image element to the base canvas
        if (undoBuffer.size() > maxUndoBufferDepth) {
            makePermanent(undoBuffer.remove(0));
        }

        // Finally, move our pointer to the tail of the buffer
        undoBufferPointer.onNext(undoBuffer.size() - 1);

        fireCanvasCommitObservers(this, imageLayerSet, getCanvasImage());

        getScratch().clear();
        this.repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getCanvasImage() {

        // Creating an image by overlaying/merging ChangeSets is expensive; return cached copy when available
        if (cachedCanvasImageHash != getCanvasImageHash()) {
            cachedCanvasImage = new BufferedImage(getCanvasSize().width, getCanvasSize().height, BufferedImage.TYPE_INT_ARGB);

            if (permanent != null) {
                overlayImage(permanent, cachedCanvasImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            for (int index = 0; index <= undoBufferPointer.blockingFirst(); index++) {
                overlayImage(undoBuffer.get(index), cachedCanvasImage);
            }

            cachedCanvasImageHash = getCanvasImageHash();
        }

        return cachedCanvasImage;
    }

    /** {@inheritDoc} */
    @Override
    public void onLayerSetModified(ImageLayerSet modified) {
        fireCanvasCommitObservers(JMonetCanvas.this, modified, getCanvasImage());
    }

    /** {@inheritDoc} */
    @Override
    public void transform(StaticImageTransform transform) {
        BufferedImage canvasImage = getCanvasImage();
        commit(new ImageLayerSet(transform.apply(canvasImage)));
    }

    /** {@inheritDoc} */
    @Override
    public void transform(PixelTransform transform) {
        transform(new ApplyPixelTransform(transform));
    }

    /**
     * Applies a {@link ImageLayerSet} to the permanent (not-undoable) layer of the canvas. Invoked when a committed change
     * has been evicted from the undo buffer as a result of exceeding its depth, or when applying an initial, base image
     * at construction.
     * <p>
     * If there is no permanent image in place, the given image is made permanent. Otherwise, the given image is
     * drawn atop the permanent image.
     */
    private void makePermanent(ImageLayerSet imageLayerSet) {
        Dimension changeSetDim = imageLayerSet.getSize();

        if (permanent == null) {
            permanent = new BufferedImage(changeSetDim.width, changeSetDim.height, BufferedImage.TYPE_INT_ARGB);
        } else if (changeSetDim.width > permanent.getWidth() || changeSetDim.height > permanent.getHeight()) {
            resizePermanent(changeSetDim);
        }

        overlayImage(imageLayerSet, permanent);
        imageLayerSet.removeLayerSetObserver(this);
    }

    /**
     * Resize the permanent image buffer to the specified dimension, leaving any existing permanent image in place at
     * (0, 0).
     * <p>
     * The "permanent" image represents all the composited changes that have fallen out of the undo/redo buffer and can
     * no longer be undone.
     *
     * @param dim The new dimension of the permanent image buffer.
     */
    private void resizePermanent(Dimension dim) {
        BufferedImage newPerm = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        if (permanent != null) {
            Graphics2D g = newPerm.createGraphics();
            g.drawImage(permanent, 0, 0, null);
            g.dispose();
        }
        permanent = newPerm;
    }

    /**
     * Draws the source image atop the existing destination image using the provided {@link AlphaComposite} mode.
     *
     * @param source      The source image to draw
     * @param destination The destination image on which to draw the source
     * @param composite   The alpha-composite mode to use.
     */
    private void overlayImage(BufferedImage source, BufferedImage destination, AlphaComposite composite) {
        Graphics2D g2d = (Graphics2D) destination.getGraphics();
        g2d.setComposite(composite);
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();
    }

    /**
     * Draws a {@link ImageLayerSet} atop an existing image.
     *
     * @param layeredImage The set of changes to be drawn
     * @param destination  The image on which to draw them
     */
    private void overlayImage(LayeredImage layeredImage, BufferedImage destination) {
        GraphicsContext g = new AwtGraphicsContext((Graphics2D) destination.getGraphics());
        layeredImage.paint(g, 1.0, null);
        g.dispose();
    }

    /**
     * Calculates a hashcode representing the image currently returned by {@link #getCanvasImage()}. Used to determine
     * if the last image generated by {@link #getCanvasImage()} can be reused given the current state of the undo
     * buffer and perm layer.
     *
     * @return A hashcode representing the image produced by {@link #getCanvasImage()}.
     */
    private long getCanvasImageHash() {
        if (undoBufferPointer.blockingFirst() >= 0) {
            return Objects.hash(permanent, undoBuffer.subList(undoBufferPointer.blockingFirst(), undoBuffer.size()));
        } else {
            return Objects.hash(permanent);
        }
    }
}
