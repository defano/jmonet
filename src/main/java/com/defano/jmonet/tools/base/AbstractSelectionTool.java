package com.defano.jmonet.tools.base;

import com.defano.jmonet.algo.Transform;
import com.defano.jmonet.canvas.ChangeSet;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.RotateTool;
import com.defano.jmonet.tools.builder.PaintTool;
import com.defano.jmonet.tools.selection.TransformableImageSelection;
import com.defano.jmonet.tools.util.Geometry;
import com.defano.jmonet.tools.util.MarchingAnts;
import com.defano.jmonet.tools.util.MarchingAntsObserver;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Mouse and keyboard handler for drawing selections (free-form or bounded) on the canvas.
 */
public abstract class AbstractSelectionTool extends PaintTool implements MarchingAntsObserver, TransformableImageSelection {

    private final BehaviorSubject<Optional<BufferedImage>> selectedImage = BehaviorSubject.createDefault(Optional.empty());

    private Point initialPoint, lastPoint;
    private Cursor movementCursor = Cursor.getDefaultCursor();
    private Cursor boundaryCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private ChangeSet selectionChange;

    private boolean isMovingSelection = false;
    private boolean dirty = false;

    /**
     * Invoked to indicate that the user has defined a new point on the selection path.
     *
     * @param initialPoint   The first point defined by the user (i.e., where the mouse was initially pressed)
     * @param newPoint       A new point to append to the selection path (i.e., where the mouse is now)
     * @param isShiftKeyDown When true, indicates user is holding the shift key down
     */
    protected abstract void addSelectionPoint(Point initialPoint, Point newPoint, boolean isShiftKeyDown);

    /**
     * Invoked to indicate that the given point should be considered the last point in the selection path.
     *
     * @param finalPoint The final point on the selection path.
     */
    protected abstract void completeSelection(Point finalPoint);

    public AbstractSelectionTool(PaintToolType type) {
        super(type);
    }

    /**
     * Creates a selection bounded by the given rectangle. Equivalent to the user clicking and dragging from the top-
     * left to the bottom-right of bounds.
     *
     * @param bounds The selection rectangle to create.
     */
    public void createSelection(Rectangle bounds) {
        if (hasSelection()) {
            finishSelection();
        }

        addSelectionPoint(bounds.getLocation(), new Point(bounds.x + bounds.width, bounds.y + bounds.height), false);
        getSelectionFromCanvas();
        completeSelection(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
    }

    /**
     * Creates a selection containing the given image at the requested location on the canvas.
     *
     * @param image The image contained by the selection. Note that the selection bounds will equal the bounds of this
     *              image.
     * @param location The location on the canvas where the selection (and image) will initially appear.
     */
    public void createSelection(BufferedImage image, Point location) {
        if (hasSelection()) {
            finishSelection();
        }

        // Make an ARGB copy of the image (input may not have alpha channel)
        BufferedImage argbImage = Transform.argbCopy(image);

        Graphics2D g = getCanvas().getScratchImage().createGraphics();
        g.drawImage(argbImage, location.x, location.y, null);
        g.dispose();

        addSelectionPoint(location.getLocation(), new Point(location.x + argbImage.getWidth(), location.y + argbImage.getHeight()), false);
        completeSelection(new Point(location.x + argbImage.getWidth(), location.y + argbImage.getHeight()));
        selectedImage.onNext(Optional.of(argbImage));

        // Don't call setDirty(), doing so will remove underlying pixels from the canvas
        dirty = true;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        if (hasSelectionBounds() && getSelectionOutline().contains(imageLocation)) {
            setToolCursor(getMovementCursor());
        } else {
            setToolCursor(getBoundaryCursor());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        isMovingSelection = getSelectionOutline() != null && getSelectionOutline().contains(imageLocation);

        // User clicked inside selection bounds; start moving selection
        if (isMovingSelection) {
            lastPoint = imageLocation;
        }

        // User clicked outside current selection bounds
        else {
            if (isDirty()) {
                finishSelection();
            } else {
                clearSelection();
            }

            initialPoint = imageLocation;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {

        // User is moving an existing selection
        if (hasSelection() && isMovingSelection) {
            setDirty();
            translateSelection(imageLocation.x - lastPoint.x, imageLocation.y - lastPoint.y);
            redrawSelection();
            lastPoint = imageLocation;
        }

        // User is defining a new selection rectangle
        else {
            addSelectionPoint(initialPoint, Geometry.constrainToBounds(imageLocation, getCanvas().getBounds()), e.isShiftDown());

            getCanvas().clearScratch();
            drawSelectionOutline();
            getCanvas().invalidateCanvas();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        // User released mouse after defining a selection
        if (!hasSelection() && hasSelectionBounds()) {
            getSelectionFromCanvas();
            completeSelection(imageLocation);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);

        getCanvas().addCanvasCommitObserver(this);
        MarchingAnts.getInstance().addObserver(this);
    }

    /** {@inheritDoc} */
    @Override
    public void deactivate() {
        super.deactivate();

        // Need to remove selection frame when tool is no longer active
        finishSelection();

        getCanvas().removeCanvasCommitObserver(this);
        MarchingAnts.getInstance().removeObserver(this);
    }

    /**
     * Takes the current selection represented by this tool and transfers it over to the given tool. Note that invoking
     * this method clears the selection from this tool, but does not deactivate the tool from the canvas. In most uses,
     * the caller will want to deactivate this tool immediately after calling this method.
     *
     * For example, this allows the user to draw a selection with the lasso tool and then transform it with a transform
     * tool (i.e., {@link com.defano.jmonet.tools.PerspectiveTool} without having to redraw the selection bounds.
     *
     * @param to The tool that the current selection should be transferred to.
     */
    public void morphSelection(AbstractSelectionTool to) {
        if (hasSelection()) {
            setDirty();
            to.createSelection(getSelectedImage(), getSelectedImageLocation());
            clearSelection();
        }

        // Nothing to do if not holding a selection
    }

    /**
     * Gets an ImmuatableProvider containing the selected image, useful for observing changes made to the selected
     * image.
     *
     * @return The provider.
     */
    public Observable<Optional<BufferedImage>> getSelectedImageObservable() {
        return selectedImage;
    }

    /** {@inheritDoc} */
    @Override
    public BufferedImage getSelectedImage() {
        return selectedImage.getValue().orElse(null);
    }

    /** {@inheritDoc} */
    @Override
    public void setSelectedImage(BufferedImage selectedImage) {
        this.selectedImage.onNext(Optional.of(selectedImage));
        redrawSelection();
    }

    /**
     * Clears the current selection frame (removes any "marching ants" from the canvas), but does not "erase" the
     * selected image, nor does it commit the selected image.
     */
    public void clearSelection() {
        selectedImage.onNext(Optional.empty());
        dirty = false;
        resetSelection();

        getCanvas().clearScratch();
        getCanvas().invalidateCanvas();
    }

    /**
     * Deletes the selection image from the canvas and clears the selection (removes marching ants)
     */
    public void deleteSelection() {
        setDirty();
        clearSelection();
        getCanvas().commit();
    }

    /** {@inheritDoc} */
    public boolean hasSelection() {
        return hasSelectionBounds() && selectedImage.getValue().isPresent();
    }

    /**
     * Determines if the user has an active selection boundary (i.e., a rectangle of marching ants)
     * <p>
     * Differs from {@link #hasSelection()} in that when a user is dragging the selection rectangle, a selection
     * boundary will exist but a selection will not. The selection is not made until the user releases the mouse.
     *
     * @return True if a selection boundary exists, false otherwise.
     */
    public boolean hasSelectionBounds() {
        return getSelectionOutline() != null && getSelectionOutline().getBounds().width > 0 && getSelectionOutline().getBounds().height > 0;
    }

    /**
     * Make the canvas image bounded by the given selection shape the current selected image.
     */
    protected void getSelectionFromCanvas() {
        getCanvas().clearScratch();

        Shape selectionBounds = getSelectionOutline();
        BufferedImage maskedSelection = maskSelection(getCanvas().getCanvasImage());
        BufferedImage trimmedSelection = maskedSelection.getSubimage(selectionBounds.getBounds().x, selectionBounds.getBounds().y, selectionBounds.getBounds().width, selectionBounds.getBounds().height);

        selectedImage.onNext(Optional.of(trimmedSelection));
        redrawSelection();
    }

    /** {@inheritDoc} */
    @Override
    public Point getSelectionLocation() {
        if (!hasSelection()) {
            return null;
        }

        return getSelectionOutline().getBounds().getLocation();
    }

    /**
     * Removes the image bounded by the selection outline from the canvas by replacing bounded pixels with
     * fully transparent pixels.
     */
    public void eraseSelectionFromCanvas() {
        if (hasSelectionBounds()) {
            getCanvas().clearScratch();

            // Clear image underneath selection
            Graphics2D scratch = (Graphics2D) getCanvas().getScratchImage().getGraphics();
            scratch.setColor(Color.WHITE);
            scratch.fill(getSelectionOutline());
            scratch.dispose();

            selectionChange = new ChangeSet(getCanvas().getScratchImage(), AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
            getCanvas().commit(selectionChange);
            redrawSelection();
        }
    }

    /**
     * Drops the selected image onto the canvas (committing the change) and clears the selection outline. This has the
     * effect of completing a select-and-move operation.
     */
    protected void finishSelection() {
        if (hasSelection()) {
            Point selectedLocation = getSelectedImageLocation();

            // Make a "local" scratch buffer and copy the selected image onto it. Why bother? Because using the canvas'
            // scratch buffer results in a race condition... the onAntsMarched method may fire while we're preparing
            // change set and draw marching ants onto our committed image (persisting the dotted-line border).
            BufferedImage localScratch = new BufferedImage(getCanvas().getScratchImage().getWidth(), getCanvas().getScratchImage().getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = localScratch.createGraphics();
            g2d.drawImage(selectedImage.getValue().get(), selectedLocation.x, selectedLocation.y, null);
            g2d.dispose();

            // Nothing to commit/change if user hasn't moved (dirtied) the selection
            if (selectionChange != null) {
                selectionChange.addChange(localScratch, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            } else {
                getCanvas().commit(new ChangeSet(localScratch));
            }

            clearSelection();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void redrawSelection() {
        getCanvas().clearScratch();

        if (isDirty() && hasSelection()) {
            Graphics2D g = (Graphics2D) getCanvas().getScratchImage().getGraphics();
            g.drawImage(selectedImage.getValue().get(), getSelectedImageLocation().x, getSelectedImageLocation().y, null);
            g.dispose();
        }
        
        drawSelectionOutline();

        getCanvas().invalidateCanvas();
    }

    /**
     * Returns the location (top-left x,y coordinates) on the canvas where the selected image should be drawn.
     * Typically, this is the location of the selection shape.
     * <p>
     * However, for tools that mutate the selection shape (i.e., {@link RotateTool}), this location may need to be
     * adjusted to account for changes to the selection shape's bounds.
     *
     * @return The x,y coordinate where the selected image should be drawn on the canvas.
     */
    protected Point getSelectedImageLocation() {
        return getSelectionOutline().getBounds().getLocation();
    }

    /**
     * Renders the selection outline (marching ants) on the canvas.
     */
    protected void drawSelectionOutline() {
        Graphics2D g = (Graphics2D) getCanvas().getScratchImage().getGraphics();

        g.setColor(Color.WHITE);
        g.draw(getSelectionOutline());

        g.setStroke(MarchingAnts.getInstance().getMarchingAnts());
        g.setColor(Color.BLACK);
        g.draw(getSelectionOutline());

        g.dispose();
    }

    /** {@inheritDoc} */
    @Override
    public void setDirty() {

        // First time we attempt to modify the selection, clear it from the canvas (so that we don't duplicate it)
        if (!dirty) {
            eraseSelectionFromCanvas();
        }

        dirty = true;
    }

    /**
     * Determines if the current selection has been changed or moved in any way since the selection outline was
     * defined.
     *
     * @return True if the selection was changed, false otherwise.
     */
    protected boolean isDirty() {
        return dirty;
    }

    /** {@inheritDoc} */
    @Override
    public void onCommit(PaintCanvas canvas, ChangeSet changeSet, BufferedImage canvasImage) {
        // Clear selection if user invokes undo/redo
        if (hasSelection() && changeSet == null) {
            clearSelection();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {

        if (hasSelection()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                case KeyEvent.VK_BACK_SPACE:
                    deleteSelection();
                    break;

                case KeyEvent.VK_ESCAPE:
                    finishSelection();
                    break;

                case KeyEvent.VK_LEFT:
                    setDirty();
                    translateSelection(-1, 0);
                    redrawSelection();
                    break;

                case KeyEvent.VK_RIGHT:
                    setDirty();
                    translateSelection(1, 0);
                    redrawSelection();
                    break;

                case KeyEvent.VK_UP:
                    setDirty();
                    translateSelection(0, -1);
                    redrawSelection();
                    break;

                case KeyEvent.VK_DOWN:
                    setDirty();
                    translateSelection(0, 1);
                    redrawSelection();
                    break;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onAntsMoved(Stroke ants) {
        if (hasSelection()) {
            redrawSelection();
        }
    }

    /**
     * Gets the cursor used to drag or move the selection on the canvas.
     * @return The movement cursor
     */
    public Cursor getMovementCursor() {
        return movementCursor;
    }

    /**
     * Sets the cursor used when dragging or moving the selection on the canvas.
     * @param movementCursor The movement cursor
     */
    public void setMovementCursor(Cursor movementCursor) {
        this.movementCursor = movementCursor;
    }

    /**
     * Gets the cursor used to define or adjust selection boundary.
     * @return The boundary cursor
     */
    public Cursor getBoundaryCursor() {
        return boundaryCursor;
    }

    /**
     * Sets the cursor used to define a selection boundary.
     * @param boundaryCursor The boundary cursor
     */
    public void setBoundaryCursor(Cursor boundaryCursor) {
        this.boundaryCursor = boundaryCursor;
    }
}
