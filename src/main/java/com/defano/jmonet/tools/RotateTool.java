package com.defano.jmonet.tools;

import com.defano.jmonet.algo.transform.image.ApplyAffineTransform;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractSelectionTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Tool for selecting a bounding box and free-rotating the selected image about its centerpoint.
 */
public class RotateTool extends AbstractSelectionTool {

    private Point centerpoint;                  // Point around which image rotates
    private Point dragLocation;                 // Location of the drag handle

    private BufferedImage originalImage;        // Unmodified image being rotated

    private Shape selectionBounds;              // Rotated selection frame
    private Shape originalSelectionBounds;      // Un-rotated selection frame
    private Shape dragHandle;                   // Shape of drag handle (transformed for rotation)
    private Shape originalDragHandle;           // Un-rotated drag angle

    private boolean rotating = false;           // Drag-rotate in progress

    public RotateTool() {
        super(PaintToolType.ROTATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSelection(BufferedImage image, Point location) {
        super.createSelection(image, location);

        originalImage = square(image);
        originalSelectionBounds = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        // User clicked inside drag handle
        if (hasSelection() && dragHandle.contains(imageLocation)) {

            if (centerpoint == null) {
                originalImage = square(getSelectedImage());
                originalSelectionBounds = getSelectionFrame();

                Rectangle selectionBounds = getSelectionFrame().getBounds();
                centerpoint = new Point(selectionBounds.x + selectionBounds.width / 2, selectionBounds.y + selectionBounds.height / 2);
            }

            rotating = true;
        }

        // User did not click inside a drag handle; delegate to superclass
        else {
            rotating = false;
            super.mousePressed(e, imageLocation);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {

        if (hasSelection() && rotating) {
            setDirty();     // Mutating the selected image

            // Calculate the rotation angle
            dragLocation = imageLocation;
            double degrees = Geometry.angle(centerpoint.x, centerpoint.y, dragLocation.x, dragLocation.y);

            if (e.isShiftDown()) {
                degrees = Geometry.round(degrees, getConstrainedAngle());
            }

            double angle = Math.toRadians(degrees);

            // Rotate the marching ants and drag handle
            selectionBounds = AffineTransform.getRotateInstance(angle, originalSelectionBounds.getBounds().x + originalSelectionBounds.getBounds().width / 2, originalSelectionBounds.getBounds().y + originalSelectionBounds.getBounds().height / 2).createTransformedShape(originalSelectionBounds);
            dragHandle = AffineTransform.getRotateInstance(angle, originalSelectionBounds.getBounds().x + originalSelectionBounds.getBounds().width / 2, originalSelectionBounds.getBounds().y + originalSelectionBounds.getBounds().height / 2).createTransformedShape(originalDragHandle);

            // Rotate the selected canvas image
            setSelectedImage(new ApplyAffineTransform(AffineTransform.getRotateInstance(angle, originalImage.getWidth() / 2, originalImage.getHeight() / 2)).apply(originalImage));
        } else {
            super.mouseDragged(e, imageLocation);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetSelection() {
        selectionBounds = null;
        originalSelectionBounds = null;
        centerpoint = null;
        dragLocation = null;
        originalImage = null;
        rotating = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionOutline(Rectangle bounds) {
        selectionBounds = bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPointToSelectionFrame(Point initialPoint, Point newPoint, boolean isShiftKeyDown) {
        int handleSize = 8;

        Rectangle selectionRectangle = new Rectangle(initialPoint);
        selectionRectangle.add(newPoint);

        selectionBounds = selectionRectangle;
        originalDragHandle = dragHandle = new Rectangle(selectionRectangle.x + selectionRectangle.width - handleSize, selectionRectangle.y + selectionRectangle.height / 2 - handleSize / 2, handleSize, handleSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeSelectionFrame(Point finalPoint) {
        if (hasSelection()) {
            originalImage = square(getSelectedImage());
            originalSelectionBounds = getSelectionFrame();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getSelectionFrame() {
        return selectionBounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translateSelection(int xDelta, int yDelta) {
        // Nothing to do; user can't move selection
        selectionBounds = AffineTransform.getTranslateInstance(xDelta, yDelta).createTransformedShape(selectionBounds);
        originalSelectionBounds = AffineTransform.getTranslateInstance(xDelta, yDelta).createTransformedShape(originalSelectionBounds);
        dragHandle = AffineTransform.getTranslateInstance(xDelta, yDelta).createTransformedShape(dragHandle);
        originalDragHandle = AffineTransform.getTranslateInstance(xDelta, yDelta).createTransformedShape(originalDragHandle);

        Rectangle selectionBounds = getSelectionFrame().getBounds();
        centerpoint = new Point(selectionBounds.x + selectionBounds.width / 2, selectionBounds.y + selectionBounds.height / 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Point getSelectedImageLocation() {

        if (dragLocation == null || originalImage == null) {
            return getSelectionFrame().getBounds().getLocation();
        } else {
            Rectangle enlargedBounds = originalImage.getRaster().getBounds();
            Geometry.center(enlargedBounds, originalSelectionBounds.getBounds());
            return enlargedBounds.getLocation();
        }
    }

    /**
     * Square the bounds of a given image so that the resulting image has an equal height and width whose value is
     * equal to the diagonal of the original image. The original image will be drawn centered inside the enlarged bounds
     * of the result.
     * <p>
     * For example, if the provided image is 10x30, the resulting image will be 32x32 with the contents of the original
     * drawn at (16,1) inside of it.
     *
     * @param image The image whose bounds should be squared
     * @return A square image whose height and width are equal to the diagonal of the original image.
     */
    private BufferedImage square(BufferedImage image) {

        if (image == null) {
            throw new IllegalArgumentException("Image to square cannot be null.");
        }

        int diagonal = (int) Math.ceil(Math.sqrt(image.getHeight() * image.getHeight() + image.getWidth() * image.getWidth()));

        int deltaX = diagonal - image.getWidth();
        int deltaY = diagonal - image.getHeight();

        BufferedImage enlarged = new BufferedImage(diagonal, diagonal, image.getType());

        Graphics2D g = enlarged.createGraphics();
        g.drawImage(image, AffineTransform.getTranslateInstance(deltaX / 2, deltaY / 2), null);
        g.dispose();

        return enlarged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void redrawSelection(boolean includeFrame) {
        super.redrawSelection(includeFrame);

        // Draw the drag handle on the selection
        Graphics2D g = getCanvas().getScratch().getAddScratchGraphics();
        g.setColor(Color.black);
        g.fill(dragHandle);
        g.dispose();
    }

}
