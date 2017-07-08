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
import com.defano.jmonet.algo.Transform;

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
    protected abstract void resetSelection();

    /**
     * Gets the shape of the current selection outline.
     *
     * @return The selection outline
     */
    public abstract Shape getSelectionOutline();

    /**
     * Invoked to indicate the bounds of the selection has changed as the result of a static transformation being applied.
     *
     * @param bounds The new selection bounds.
     */
    protected abstract void setSelectionBounds(Rectangle bounds);

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

    /**
     * Invoked to indicate that the selection has moved on the canvas. The selection shape's coordinates should be
     * translated by the given amount.
     *
     * @param xDelta Number of pixels to move horizontally.
     * @param yDelta Number of pixels to move vertically.
     */
    protected abstract void adjustSelectionBounds(int xDelta, int yDelta);

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

        Graphics2D g = getCanvas().getScratchImage().createGraphics();
        g.drawImage(image, location.x, location.y, null);
        g.dispose();

        addSelectionPoint(location.getLocation(), new Point(location.x + image.getWidth(), location.y + image.getHeight()), false);
        completeSelection(new Point(location.x + image.getWidth(), location.y + image.getHeight()));
        selectedImage.set(image);

        // Don't call setDirty(), doing so will remove underlying pixels from the canvas
        dirty = true;
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
        if (hasSelection() && isMovingSelection) {
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
            getSelectionFromCanvas();
            completeSelection(imageLocation);
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

    /**
     * Add the graphics underneath the selection to the currently selected image. Has no effect if a selection is not
     * active or has not been dirtied.
     *
     * This method "picks up" the paint underneath the selection (that wasn't part of the paint initially
     * picked up when the selection bounds were defined).
     */
    public void pickupSelection() {

        if (hasSelection()) {
            Shape selectionBounds = getSelectionOutline();
            BufferedImage maskedSelection = maskSelection(getCanvas().getCanvasImage(), selectionBounds);
            BufferedImage trimmedSelection = maskedSelection.getSubimage(
                    Math.max(0, selectionBounds.getBounds().x),
                    Math.max(0, selectionBounds.getBounds().y),
                    Math.min(selectionBounds.getBounds().width, maskedSelection.getWidth() - selectionBounds.getBounds().x),
                    Math.min(selectionBounds.getBounds().height, maskedSelection.getHeight() - selectionBounds.getBounds().y)
            );

            Graphics2D g2d = (Graphics2D) getSelectedImage().getGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.drawImage(trimmedSelection, 0, 0, null);
            g2d.dispose();

            eraseSelectionFromCanvas();
        }
    }

    /**
     * Inverts the color of the selection while preserving the alpha transparency.
     */
    @Override
    public void invert() {
        if (hasSelection()) {
            BufferedImage selectedImage = getSelectedImage();
            for (int x = 0; x < selectedImage.getWidth(); x++) {
                for (int y = 0; y < selectedImage.getHeight(); y++) {

                    int argb = selectedImage.getRGB(x, y);
                    int alpha = 0xff000000 & argb;
                    int rgb = 0x00ffffff & argb;

                    // Invert preserving alpha channel
                    selectedImage.setRGB(x, y, alpha | (~rgb & 0x00ffffff));
                }
            }

            setDirty();
        }
    }

    /**
     * Adjusts the brightness of the selected image by a given amount preserving the alpha transparency.
     *
     * @param delta An amount by which brightness should be adjusted, -256 to +256. Negative numbers darken an image,
     */
    @Override
    public void adjustBrightness(int delta) {
        if (hasSelection()) {
            BufferedImage selectedImage = getSelectedImage();
            for (int x = 0; x < selectedImage.getWidth(); x++) {
                for (int y = 0; y < selectedImage.getHeight(); y++) {

                    int argb = selectedImage.getRGB(x, y);
                    int alpha = 0xff000000 & argb;
                    int r = ((0xff0000 & argb) >> 16) + delta;
                    int g = ((0xff00 & argb) >> 8) + delta;
                    int b = (0xff & argb) + delta;

                    // Saturate at 0 and 256
                    r = r > 0xff ? 0xff : r < 0 ? 0 : r;
                    g = g > 0xff ? 0xff : g < 0 ? 0 : g;
                    b = b > 0xff ? 0xff : b < 0 ? 0 : b;

                    // Adjust preserving alpha channel
                    selectedImage.setRGB(x, y, alpha | (r << 16) | (g << 8) | b);
                }
            }

            setDirty();
        }
    }

    /**
     * Adjusts the alpha transparency of each pixel in the selection while otherwise preserving the hue, saturation
     * and brightness of the pixel.
     *
     * @param delta An amount by which transparency should be adjusted -256 to +256; Negative numbers make the image
     */
    public void adjustTransparency(int delta) {
        if (hasSelection()) {
            BufferedImage selectedImage = getSelectedImage();
            for (int x = 0; x < selectedImage.getWidth(); x++) {
                for (int y = 0; y < selectedImage.getHeight(); y++) {

                    Color c = new Color(selectedImage.getRGB(x, y), true);
                    int alpha = c.getAlpha() + delta;
                    alpha = alpha > 255 ? 255 : alpha < 0 ? 0 : alpha;

                    // Adjust preserving alpha channel
                    selectedImage.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB());
                }
            }

            setDirty();
        }
    }

    @Override
    public void rotateLeft() {
        applyTransform(Transform.rotateLeft(selectedImage.get().getWidth(), selectedImage.get().getHeight()));
    }

    @Override
    public void rotateRight() {
        applyTransform(Transform.rotateRight(selectedImage.get().getWidth(), selectedImage.get().getHeight()));
    }

    @Override
    public void flipHorizontal() {
        applyTransform(Transform.flipHorizontalTransform(selectedImage.get().getWidth()));
    }

    @Override
    public void flipVertical() {
        applyTransform(Transform.flipVerticalTransform(selectedImage.get().getHeight()));
    }

    @Override
    public void applyTransform(AffineTransform transform) {
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

    /**
     * Gets an ImmuatableProvider containing the selected image, useful for observing changes made to the selected
     * image.
     *
     * @return The provider.
     */
    public ImmutableProvider<BufferedImage> getSelectedImageProvider() {
        return ImmutableProvider.from(selectedImage);
    }

    /**
     * Gets the image represented by the selection.
     * @return The selected image
     */
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
     * Deletes the selection image from the canvas and clears the selection (removes marching ants)
     */
    public void deleteSelection() {
        setDirty();
        clearSelection();
        getCanvas().commit();
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
        BufferedImage maskedSelection = maskSelection(getCanvas().getCanvasImage(), selectionBounds);
        BufferedImage trimmedSelection = maskedSelection.getSubimage(selectionBounds.getBounds().x, selectionBounds.getBounds().y, selectionBounds.getBounds().width, selectionBounds.getBounds().height);

        selectedImage.set(trimmedSelection);
        drawSelection();
    }

    /**
     * Determines the location (top-left x,y coordinate) of the selection outline.
     *
     * @return Get the top-left coordinate of the selection boundary
     */
    private Point getSelectionLocation() {
        if (!hasSelection()) {
            return null;
        }

        return getSelectionOutline().getBounds().getLocation();
    }

    /**
     * Removes the image bounded by the selection outline from the canvas by replacing bounded pixels with
     * fully transparent pixels.
     */
    private void eraseSelectionFromCanvas() {
        if (hasSelectionBounds()) {
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
    }

    /**
     * Drops the selected image onto the canvas (committing the change) and clears the selection outline. This has the
     * effect of completing a select-and-move operation.
     */
    protected void finishSelection() {
        if (hasSelection()) {
            getCanvas().clearScratch();
            BufferedImage scratch = getCanvas().getScratchImage();

            Graphics2D g2d = (Graphics2D) scratch.getGraphics();
            g2d.drawImage(selectedImage.get(), getSelectedImageLocation().x, getSelectedImageLocation().y, null);
            g2d.dispose();

            // Nothing to commit/change if user hasn't moved (dirtied) the selection
            if (selectionChange != null) {
                selectionChange.addChange(scratch, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            } else {
                getCanvas().commit();
            }

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
     * @param mask  The shape bounding the subimage to keep
     * @return A BufferedImage in which every pixel not within the selection has been made fully transparent
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

    @Override
    public void onCommit(PaintCanvas canvas, ChangeSet changeSet, BufferedImage canvasImage) {
        // Clear selection if user invokes undo/redo
        if (hasSelection() && changeSet == null) {
            clearSelection();
        }
    }

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
