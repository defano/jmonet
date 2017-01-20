package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.ChangeSet;
import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.ImmutableProvider;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.tools.RotateTool;
import com.defano.jmonet.tools.util.Geometry;
import com.defano.jmonet.tools.util.MarchingAnts;
import com.defano.jmonet.tools.util.MarchingAntsObserver;
import com.defano.jmonet.tools.util.Transform;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Mouse and keyboard handler for drawing selections (free-form or bounded) on the canvas.
 */
public abstract class AbstractSelectionTool extends PaintTool implements MarchingAntsObserver, StaticTransformer {

    private final Provider<BufferedImage> selectedImage = new Provider<>();

    private Point initialPoint, lastPoint;
    private Cursor movementCursor = Cursor.getDefaultCursor();
    private Cursor boundaryCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private ChangeSet selectionChange;

    private boolean isMovingSelection = false;
    private boolean dirty = false;

    /**
     * Reset the selection boundary to its initial, no-selection state. {@link #getSelectionOutline()} should return
     * null following a selection reset, but prior to defining a new selection via {@link #addSelectionPoint(Point, Point, boolean)}
     */
    public abstract void resetSelection();

    /**
     * Gets the shape of the current selection outline.
     * @return The selection outline
     */
    public abstract Shape getSelectionOutline();

    /**
     * Invoked to indicate the bounds of the selection has changed as the result of a static transformation being applied.
     * @param bounds The new selection bounds.
     */
    public abstract void setSelectionBounds(Rectangle bounds);

    /**
     * Invoked to indicate that the user has defined a new point on the selection path.
     * @param initialPoint The first point defined by the user (i.e., where the mouse was initially pressed)
     * @param newPoint A new point to append to the selection path (i.e., where the mouse is now)
     * @param isShiftKeyDown When true, indicates user is holding the shift key down
     */
    public abstract void addSelectionPoint(Point initialPoint, Point newPoint, boolean isShiftKeyDown);

    /**
     * Invoked to indicate that the given point should be considered the last point in the selection path.
     * @param finalPoint The final point on the selection path.
     */
    public abstract void completeSelection(Point finalPoint);

    /**
     * Invoked to indicate that the selection has moved on the canvas. The selection shape's coordinates should be
     * translated by the given amount.
     * @param xDelta Number of pixels to move horizontally.
     * @param yDelta Number of pixels to move vertically.
     */
    public abstract void adjustSelectionBounds(int xDelta, int yDelta);

    public AbstractSelectionTool(PaintToolType type) {
        super(type);
    }

    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        if (hasSelectionBounds() && getSelectionOutline().contains(imageLocation)) {
            setToolCursor(getMovementCursor());
        } else {
            setToolCursor(getBoundaryCursor());
        }
    }

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

    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {

        // User is moving an existing selection
        if (isMovingSelection) {
            setDirty();
            adjustSelectionBounds(imageLocation.x - lastPoint.x, imageLocation.y - lastPoint.y);
            drawSelection();
            lastPoint = imageLocation;
        }

        // User is defining a new selection rectangle
        else {
            addSelectionPoint(initialPoint, Geometry.pointWithinBounds(imageLocation, getCanvas().getBounds()), e.isShiftDown());

            getCanvas().clearScratch();
            drawSelectionOutline();
            getCanvas().invalidateCanvas();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        // User released mouse after defining a selection
        if (!hasSelection() && hasSelectionBounds()) {
            completeSelection(imageLocation);
            getSelectionFromCanvas();
        }
    }

    @Override
    public void activate(PaintCanvas canvas) {
        super.activate(canvas);

        getCanvas().addCanvasCommitObserver(this);
        MarchingAnts.getInstance().addObserver(this);
    }

    @Override
    public void deactivate() {
        super.deactivate();

        // Need to remove selection frame when tool is no longer active
        finishSelection();

        getCanvas().removeCanvasCommitObserver(this);
        MarchingAnts.getInstance().removeObserver(this);
    }

    @Override
    public void rotateLeft() {
        transformSelection(Transform.rotateLeft(selectedImage.get().getWidth(), selectedImage.get().getHeight()));
    }

    @Override
    public void rotateRight() {
        transformSelection(Transform.rotateRight(selectedImage.get().getWidth(), selectedImage.get().getHeight()));
    }

    @Override
    public void flipHorizontal() {
        transformSelection(Transform.flipHorizontalTransform(selectedImage.get().getWidth()));
    }

    @Override
    public void flipVertical() {
        transformSelection(Transform.flipVerticalTransform(selectedImage.get().getHeight()));
    }

    public void transformSelection(AffineTransform transform) {
        if (hasSelection()) {
            setDirty();

            // Get the original location of the selection
            Point originalLocation = getSelectionLocation();

            // Transform the selected image
            selectedImage.set(Transform.transform(selectedImage.get(), transform));

            // Relocate the image to its original location
            Rectangle newBounds = selectedImage.get().getRaster().getBounds();
            newBounds.setLocation(originalLocation);
            setSelectionBounds(newBounds);

            drawSelection();
        }
    }

    public ImmutableProvider<BufferedImage> getSelectedImageProvider() {
        return ImmutableProvider.from(selectedImage);
    }

    public BufferedImage getSelectedImage() {
        return selectedImage.get();
    }

    protected void setSelectedImage(BufferedImage selectedImage) {
        this.selectedImage.set(selectedImage);
        drawSelection();
    }

    /**
     * Clears the current selection frame (removes any "marching ants" from the canvas), but does not "erase" the
     * selected image, nor does it commit the selected image.
     */
    public void clearSelection() {
        selectedImage.set(null);
        dirty = false;
        resetSelection();

        getCanvas().clearScratch();
        getCanvas().invalidateCanvas();
    }

    /**
     * Determines if the user has an active selection.
     * <p>
     * Differs from {@link #hasSelectionBounds()} in that when a user is dragging the selection rectangle, a selection
     * boundary will exist but a selection will not. The selection is not made until the user releases the mouse.
     *
     * @return True is a selection exists, false otherwise.
     */
    public boolean hasSelection() {
        return hasSelectionBounds() && selectedImage.get() != null;
    }

    /**
     * Determines if the user has an active selection boundary (i.e., a rectangle of marching ants)
     * <p>
     * Differs from {@link #hasSelectionBounds()} in that when a user is dragging the selection rectangle, a selection
     * boundary will exist but a selection will not. The selection is not made until the user releases the mouse.
     *
     * @return True if a selection boundary exists, false otherwise.
     */
    public boolean hasSelectionBounds() {
        return getSelectionOutline() != null && getSelectionOutline().getBounds().width > 0 && getSelectionOutline().getBounds().height > 0;
    }

    /**
     * Make the canvas image bounded by the given selection rectangle the current selected image.
     */
    private void getSelectionFromCanvas() {
        getCanvas().clearScratch();

        Shape selectionBounds = getSelectionOutline();
        BufferedImage maskedSelection = maskSelection(getCanvas().getCanvasImage(), selectionBounds);
        BufferedImage trimmedSelection = maskedSelection.getSubimage(selectionBounds.getBounds().x, selectionBounds.getBounds().y, selectionBounds.getBounds().width, selectionBounds.getBounds().height);

        selectedImage.set(trimmedSelection);
        drawSelection();
    }

    /**
     * Determines the location (top-left x,y coordinate) of the selection outline.
     * @return
     */
    private Point getSelectionLocation() {
        if (!hasSelection()) {
            return null;
        }

        return getSelectionOutline().getBounds().getLocation();
    }

    /**
     * Removes the image bounded by the selection outline from the canvas by filling bounded pixels with
     * fully transparent pixels.
     */
    private void eraseSelectionFromCanvas() {
        getCanvas().clearScratch();

        // Clear image underneath selection
        Graphics2D scratch = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        scratch.setColor(Color.WHITE);
        scratch.fill(getSelectionOutline());
        scratch.dispose();

        selectionChange = new ChangeSet(getCanvas().getScratchImage(), AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
        getCanvas().commit(selectionChange);
        drawSelection();
    }

    /**
     * Drops the selected image onto the canvas (committing the change) and clears the selection outline. This has the
     * effect of completing a select-and-move operation.
     */
    protected void finishSelection() {

        if (hasSelection()) {
            getCanvas().clearScratch();

            Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
            g2d.drawImage(selectedImage.get(), getSelectedImageLocation().x, getSelectedImageLocation().y, null);
            g2d.dispose();

            selectionChange.addChange(getCanvas().getScratchImage(), AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            clearSelection();
        }
    }

    /**
     * Draws the provided image and selection frame ("marching ants") onto the scratch buffer at the given location.
     */
    protected void drawSelection() {
        getCanvas().clearScratch();

        Graphics2D g = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        g.drawImage(selectedImage.get(), getSelectedImageLocation().x, getSelectedImageLocation().y, null);
        g.dispose();

        drawSelectionOutline();

        getCanvas().invalidateCanvas();
    }

    /**
     * Returns the location (top-left x,y coordinates) on the canvas where the selected image should be drawn.
     * Typically, this is the location of the selection shape.
     *
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

        g.setStroke(MarchingAnts.getInstance().getMarchingAnts());
        g.setColor(Color.BLACK);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR));
        g.draw(getSelectionOutline());
        g.dispose();
    }

    /**
     * Marks the selection as having been mutated (either by transformation or movement).
     */
    protected void setDirty() {

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

    /**
     * Creates a new image in which every pixel not within the given shape has been changed to fully transparent.
     *
     * @param image The image to mask
     * @param mask The shape bounding the subimage to keep
     * @return
     */
    private BufferedImage maskSelection(BufferedImage image, Shape mask) {
        BufferedImage subimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int clearPixel = new Color(0, 0, 0, 0).getRGB();

        for (int y = 0; y < image.getRaster().getHeight(); y++) {
            for (int x = 0; x < image.getRaster().getWidth(); x++) {
                if (x > image.getWidth() || y > image.getHeight()) continue;

                if (mask.contains(x, y)) {
                    subimage.setRGB(x, y, image.getRGB(x, y));
                } else {
                    subimage.setRGB(x, y, clearPixel);
                }
            }
        }

        return subimage;
    }

    /**
     * Called when a change has been committed to the canvas.
     *
     * @param canvas
     * @param committedElement
     * @param canvasImage
     */
    @Override
    public void onCommit(PaintCanvas canvas, BufferedImage committedElement, BufferedImage canvasImage) {
        // Clear selection if user invokes undo/redo
        if (hasSelection() && committedElement == null) {
            clearSelection();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (hasSelection()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                case KeyEvent.VK_BACK_SPACE:
                    setDirty();
                    clearSelection();
                    break;

                case KeyEvent.VK_ESCAPE:
                    finishSelection();
                    break;

                case KeyEvent.VK_LEFT:
                    setDirty();
                    adjustSelectionBounds(-1, 0);
                    drawSelection();
                    break;

                case KeyEvent.VK_RIGHT:
                    setDirty();
                    adjustSelectionBounds(1, 0);
                    drawSelection();
                    break;

                case KeyEvent.VK_UP:
                    setDirty();
                    adjustSelectionBounds(0, -1);
                    drawSelection();
                    break;

                case KeyEvent.VK_DOWN:
                    setDirty();
                    adjustSelectionBounds(0, 1);
                    drawSelection();
                    break;
            }
        }
    }

    @Override
    public void onAntsMoved() {
        if (hasSelection()) {
            drawSelection();
        }
    }

    public Cursor getMovementCursor() {
        return movementCursor;
    }

    public void setMovementCursor(Cursor movementCursor) {
        this.movementCursor = movementCursor;
    }

    public Cursor getBoundaryCursor() {
        return boundaryCursor;
    }

    public void setBoundaryCursor(Cursor boundaryCursor) {
        this.boundaryCursor = boundaryCursor;
    }
}
