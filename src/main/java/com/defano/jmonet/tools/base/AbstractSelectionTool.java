package com.defano.jmonet.tools.base;

import com.defano.jmonet.algo.transform.Transform;
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

    private boolean isMovingSelection = false;
    private boolean dirty = false;

    public AbstractSelectionTool(PaintToolType type) {
        super(type);
    }

    /**
     * Invoked to indicate that the user has defined a new point on the selection path.
     *
     * @param initialPoint   The first point defined by the user (i.e., where the mouse was initially pressed)
     * @param newPoint       A new point to append to the selection path (i.e., where the mouse is now)
     * @param isShiftKeyDown When true, indicates user is holding the shift key down
     */
    protected abstract void addPointToSelectionFrame(Point initialPoint, Point newPoint, boolean isShiftKeyDown);

    /**
     * Invoked to indicate that the given point should be considered the last point in the selection path, and the
     * path shape should be closed.
     *
     * @param finalPoint The final point on the selection path.
     */
    protected abstract void closeSelectionFrame(Point finalPoint);

    /**
     * Creates a selection bounded by the given rectangle. Equivalent to the user clicking and dragging from the top-
     * left to the bottom-right of bounds.
     *
     * @param bounds The selection rectangle to create.
     */
    public void createSelection(Rectangle bounds) {
        if (hasSelection()) {
            completeSelection();
        }

        addPointToSelectionFrame(bounds.getLocation(), new Point(bounds.x + bounds.width, bounds.y + bounds.height), false);
        getSelectionFromCanvas();
        closeSelectionFrame(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
    }

    /**
     * Creates a selection containing the given image at the requested location on the canvas. The size of the selection
     * will equal the bounds of the given image.
     *
     * @param image    The image contained by the selection. Note that the selection bounds will equal the bounds of this
     *                 image.
     * @param location The location on the canvas where the selection (and image) will initially appear.
     */
    public void createSelection(BufferedImage image, Point location) {
        if (hasSelection()) {
            completeSelection();
        }

        // Make an ARGB copy of the image (input may not have alpha channel)
        BufferedImage argbImage = Transform.argbCopy(image);

        Graphics2D g = getCanvas().getScratch().getAddScratchGraphics();
        g.drawImage(argbImage, location.x, location.y, null);

        addPointToSelectionFrame(location.getLocation(), new Point(location.x + argbImage.getWidth(), location.y + argbImage.getHeight()), false);
        closeSelectionFrame(new Point(location.x + argbImage.getWidth(), location.y + argbImage.getHeight()));
        selectedImage.onNext(Optional.of(argbImage));

        // Don't call setDirty(), doing so will remove underlying pixels from the canvas
        dirty = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {

        // Update tool cursor
        if (hasSelectionFrame() && getSelectionFrame().contains(imageLocation)) {
            setToolCursor(getMovementCursor());
        } else {
            setToolCursor(getBoundaryCursor());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        isMovingSelection = getSelectionFrame() != null && getSelectionFrame().contains(imageLocation);

        // User clicked inside selection bounds; start moving selection
        if (isMovingSelection) {
            lastPoint = imageLocation;
        }

        // User clicked outside current selection bounds
        else {
            if (isDirty()) {
                completeSelection();
            } else {
                abortSelection();
            }

            initialPoint = imageLocation;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {

        // User is moving an existing selection
        if (hasSelection() && isMovingSelection) {
            setDirty();
            translateSelection(imageLocation.x - lastPoint.x, imageLocation.y - lastPoint.y);
            redrawSelection(true);
            lastPoint = imageLocation;
        }

        // User is defining a new selection rectangle
        else {
            addPointToSelectionFrame(initialPoint, Geometry.constrainToBounds(imageLocation, getCanvas().getBounds()), e.isShiftDown());

            getScratch().clear();
            drawSelectionFrame();
            getCanvas().invalidateCanvas();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        // User released mouse after defining a selection
        if (!hasSelection() && hasSelectionFrame()) {
            getSelectionFromCanvas();
            closeSelectionFrame(imageLocation);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);

        getCanvas().addCanvasCommitObserver(this);
        MarchingAnts.getInstance().addObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        super.deactivate();

        // Need to remove selection frame when tool is no longer active
        completeSelection();

        getCanvas().removeCanvasCommitObserver(this);
        MarchingAnts.getInstance().removeObserver(this);
    }

    /**
     * Takes the current selection represented by this tool and transfers it over to the given tool. Note that invoking
     * this method clears the selection from this tool, but does not deactivate the tool from the canvas. In most uses,
     * the caller will want to deactivate this tool immediately after calling this method.
     * <p>
     * For example, this allows the user to draw a selection with the lasso tool and then transform it with a transform
     * tool (i.e., {@link com.defano.jmonet.tools.PerspectiveTool} without having to redraw the selection bounds.
     *
     * @param to The tool that the current selection should be transferred to.
     */
    public void morphSelection(AbstractSelectionTool to) {

        // Commit any existing changes before morphing
        if (hasSelection() && isDirty()) {
            commit();
        }

        if (hasSelection()) {

            // Capture selected image and its location on the canvas
            BufferedImage selection = getSelectedImage();
            Point location = getSelectedImageLocation();

            // Drop the current tool's selection on the floor
            abortSelection();

            // Re-create the selection in the new tool
            to.createSelection(selection, location);
            to.dirty = false;       // Required to allow new tool to "pick up" graphics when dirtied
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

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getSelectedImage() {
        return selectedImage.getValue().orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedImage(BufferedImage selectedImage) {
        this.selectedImage.onNext(Optional.of(selectedImage));
        redrawSelection(true);
    }

    /**
     * Clears the current selection frame (removes any "marching ants" from the canvas), but does not "erase" the
     * selected image, nor does it commit the selected image. See {@link #closeSelectionFrame(Point)}
     */
    protected void abortSelection() {
        selectedImage.onNext(Optional.empty());
        dirty = false;
        resetSelection();

        getScratch().clear();
        getCanvas().invalidateCanvas();
    }

    /**
     * Drops the selected image onto the canvas (committing the change) and clears the selection outline. This has the
     * effect of completing a select-and-move operation.
     */
    protected void completeSelection() {
        if (hasSelection()) {
            if (isDirty()) {
                commit();
            }

            abortSelection();
        }
    }

    /**
     * Deletes the selected image from the canvas, commits the change, then clears the selection (removes marching
     * ants). Has the effect of the user pressing 'delete' when an active selection exists.
     */
    public void deleteSelection() {
        setDirty();
        abortSelection();
        getCanvas().commit();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasSelection() {
        return hasSelectionFrame() && selectedImage.getValue().isPresent();
    }

    /**
     * Determines if the user has an active selection boundary (i.e., a rectangle of marching ants)
     * <p>
     * Differs from {@link #hasSelection()} in that when a user is dragging the selection rectangle, a selection
     * boundary will exist but a selection will not. The selection is not made until the user releases the mouse.
     *
     * @return True if a selection boundary exists, false otherwise.
     */
    protected boolean hasSelectionFrame() {
        return getSelectionFrame() != null && getSelectionFrame().getBounds().width > 0 && getSelectionFrame().getBounds().height > 0;
    }

    /**
     * Make the canvas image bounded by the given selection shape the current selected image. Does not modify the canvas
     * in any way (does not "pick up" the image from the canvas).
     */
    protected void getSelectionFromCanvas() {
        Shape selectionBounds = getSelectionFrame();
        BufferedImage maskedSelection = crop(getCanvas().getCanvasImage());
        BufferedImage trimmedSelection = maskedSelection.getSubimage(selectionBounds.getBounds().x, selectionBounds.getBounds().y, selectionBounds.getBounds().width, selectionBounds.getBounds().height);

        selectedImage.onNext(Optional.of(trimmedSelection));
        redrawSelection(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getSelectionLocation() {
        if (!hasSelection()) {
            return null;
        }

        return getSelectionFrame().getBounds().getLocation();
    }

    /**
     * Removes the image bounded by the selection outline from the canvas by replacing bounded pixels with
     * fully transparent pixels.
     */
    public void eraseSelectionFromCanvas() {
        if (hasSelectionFrame()) {

            // Clear image underneath selection
            Graphics2D g = getCanvas().getScratch().getRemoveScratchGraphics();
            g.setColor(Color.WHITE);
            g.fill(getSelectionFrame());

            redrawSelection(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void redrawSelection(boolean includeFrame) {
        getScratch().clearAdd();

        if (hasSelection()) {
            Graphics2D g = getCanvas().getScratch().getAddScratchGraphics();
            g.drawImage(selectedImage.getValue().get(), getSelectedImageLocation().x, getSelectedImageLocation().y, null);
        }

        if (includeFrame) {
            drawSelectionFrame();
        }

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
        return getSelectionFrame().getBounds().getLocation();
    }

    /**
     * Renders the selection outline (marching ants) on the canvas.
     */
    protected void drawSelectionFrame() {
        Graphics2D g = getScratch().getAddScratchGraphics();

        g.setColor(Color.WHITE);
        g.draw(getSelectionFrame());

        g.setStroke(MarchingAnts.getInstance().getMarchingAnts());
        g.setColor(Color.BLACK);
        g.draw(getSelectionFrame());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDirty() {

        // First time we attempt to modify the selection, clear it from the canvas (so that we don't duplicate it)
        if (!dirty) {
            eraseSelectionFromCanvas();
        }

        dirty = true;
    }

    private void commit() {
        if (hasSelection()) {

            // Re-render the scratch buffer without the selection frame (don't want to commit marching ants to canvas)
            getScratch().clearAdd();
            if (hasSelection()) {
                Graphics2D g = getCanvas().getScratch().getAddScratchGraphics();
                g.drawImage(selectedImage.getValue().get(), getSelectedImageLocation().x, getSelectedImageLocation().y, null);
            }

            getCanvas().commit();
            setDirty();
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCommit(PaintCanvas canvas, ChangeSet changeSet, BufferedImage canvasImage) {
        // Clear selection if user invokes undo/redo
        if (hasSelection() && changeSet == null) {
            abortSelection();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (hasSelection()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                case KeyEvent.VK_BACK_SPACE:
                    deleteSelection();
                    break;

                case KeyEvent.VK_ESCAPE:
                    completeSelection();
                    break;

                case KeyEvent.VK_LEFT:
                    setDirty();
                    translateSelection(-1, 0);
                    redrawSelection(true);
                    break;

                case KeyEvent.VK_RIGHT:
                    setDirty();
                    translateSelection(1, 0);
                    redrawSelection(true);
                    break;

                case KeyEvent.VK_UP:
                    setDirty();
                    translateSelection(0, -1);
                    redrawSelection(true);
                    break;

                case KeyEvent.VK_DOWN:
                    setDirty();
                    translateSelection(0, 1);
                    redrawSelection(true);
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAntsMoved(Stroke ants) {
        if (hasSelection()) {
            redrawSelection(true);
        }
    }

    /**
     * Gets the cursor used to drag or move the selection on the canvas.
     *
     * @return The movement cursor
     */
    public Cursor getMovementCursor() {
        return movementCursor;
    }

    /**
     * Sets the cursor used when dragging or moving the selection on the canvas.
     *
     * @param movementCursor The movement cursor
     */
    public void setMovementCursor(Cursor movementCursor) {
        this.movementCursor = movementCursor;
    }

    /**
     * Gets the cursor used to define or adjust selection boundary.
     *
     * @return The boundary cursor
     */
    public Cursor getBoundaryCursor() {
        return boundaryCursor;
    }

    /**
     * Sets the cursor used to define a selection boundary.
     *
     * @param boundaryCursor The boundary cursor
     */
    public void setBoundaryCursor(Cursor boundaryCursor) {
        this.boundaryCursor = boundaryCursor;
    }
}
