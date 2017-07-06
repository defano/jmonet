package com.defano.jmonet.tools.base;

import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Mouse and keyboard handler for tools that define a bounding box with flexible corners that can be dragged into
 * a desired position and shape.
 */
public abstract class AbstractTransformTool extends AbstractSelectionTool {

    private final static int HANDLE_SIZE = 8;

    private Cursor transformCursor = new Cursor(Cursor.MOVE_CURSOR);
    private BufferedImage originalImage;
    private Rectangle selectionBounds;
    private FlexQuadrilateral transformBounds;

    private Rectangle topLeftHandle, topRightHandle, bottomRightHandle, bottomLeftHandle;
    private boolean dragTopLeft, dragTopRight, dragBottomRight, dragBottomLeft;

    protected abstract void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition);

    protected abstract void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition);

    protected abstract void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition);

    protected abstract void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition);

    public AbstractTransformTool(PaintToolType type) {
        super(type);
        setToolCursor(transformCursor);
    }

    public void createSelection(Rectangle bounds) {
        super.createSelection(bounds);
        originalImage = getSelectedImage();
    }

    @Override
    public void createSelection(BufferedImage image, Point location) {
        super.createSelection(image, location);
        originalImage = image;
    }

    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        // User has already made selection; we'll handle the mouse press
        if (hasSelection()) {

            // User is clicking a drag handle
            dragTopLeft = topLeftHandle.contains(imageLocation);
            dragTopRight = topRightHandle.contains(imageLocation);
            dragBottomLeft = bottomLeftHandle.contains(imageLocation);
            dragBottomRight = bottomRightHandle.contains(imageLocation);
        }

        super.mousePressed(e, imageLocation);
    }

    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {

        // Selection exists, see if we're dragging a handle
        if (hasSelection()) {

            if (dragTopLeft || dragTopRight || dragBottomLeft || dragBottomRight) {
                setDirty();
            }

            if (dragTopLeft) {
                moveTopLeft(transformBounds, imageLocation);
                drawSelection();
            } else if (dragTopRight) {
                moveTopRight(transformBounds, imageLocation);
                drawSelection();
            } else if (dragBottomLeft) {
                moveBottomLeft(transformBounds, imageLocation);
                drawSelection();
            } else if (dragBottomRight) {
                moveBottomRight(transformBounds, imageLocation);
                drawSelection();
            } else {
                super.mouseDragged(e, imageLocation);
            }

        }

        // No selection, delegate to selection tool to define selection
        else {
            super.mouseDragged(e, imageLocation);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {

        // User is completing selection
        if (!hasSelection()) {
            super.mouseReleased(e, imageLocation);

            // Grab a copy of the selected image before we begin transforming it
            originalImage = getSelectedImage();
        } else {
            super.mouseReleased(e, imageLocation);
        }
    }

    @Override
    protected void addSelectionPoint(Point initialPoint, Point newPoint, boolean isShiftKeyDown) {
        selectionBounds = new Rectangle(initialPoint);
        selectionBounds.add(newPoint);

        int width = selectionBounds.width;
        int height = selectionBounds.height;

        if (isShiftKeyDown) {
            width = height = Math.max(width, height);
        }

        selectionBounds = new Rectangle(selectionBounds.x, selectionBounds.y, width, height);
    }

    @Override
    protected void completeSelection(Point finalPoint) {
        transformBounds = new FlexQuadrilateral(selectionBounds);
    }

    @Override
    protected void resetSelection() {
        selectionBounds = null;
        transformBounds = null;

        topLeftHandle = topRightHandle = bottomLeftHandle = bottomRightHandle = null;
    }

    @Override
    protected void setSelectionBounds(Rectangle bounds) {
        transformBounds = new FlexQuadrilateral(bounds);
    }

    @Override
    public Shape getSelectionOutline() {
        return transformBounds != null ? transformBounds.getShape() : selectionBounds;
    }

    @Override
    protected void adjustSelectionBounds(int xDelta, int yDelta) {
        selectionBounds.setLocation(selectionBounds.x + xDelta, selectionBounds.y + yDelta);
        transformBounds.getBottomLeft().x += xDelta;
        transformBounds.getBottomLeft().y += yDelta;
        transformBounds.getBottomRight().x += xDelta;
        transformBounds.getBottomRight().y += yDelta;
        transformBounds.getTopLeft().x += xDelta;
        transformBounds.getTopLeft().y += yDelta;
        transformBounds.getTopRight().x += xDelta;
        transformBounds.getTopRight().y += yDelta;
    }

    protected BufferedImage getOriginalImage() {
        return originalImage;
    }

    protected void drawSelectionOutline() {
        super.drawSelectionOutline();

        if (hasSelection() && transformBounds != null) {

            // Render drag handles on selection bounds
            Graphics2D g = (Graphics2D) getCanvas().getScratchImage().getGraphics();
            g.setPaint(Color.BLACK);

            topLeftHandle = new Rectangle(transformBounds.getTopLeft().x, transformBounds.getTopLeft().y, HANDLE_SIZE, HANDLE_SIZE);
            topRightHandle = new Rectangle(transformBounds.getTopRight().x - HANDLE_SIZE, transformBounds.getTopRight().y, HANDLE_SIZE, HANDLE_SIZE);
            bottomRightHandle = new Rectangle(transformBounds.getBottomRight().x - HANDLE_SIZE, transformBounds.getBottomRight().y - HANDLE_SIZE, HANDLE_SIZE, HANDLE_SIZE);
            bottomLeftHandle = new Rectangle(transformBounds.getBottomLeft().x, transformBounds.getBottomLeft().y - HANDLE_SIZE, HANDLE_SIZE, HANDLE_SIZE);

            g.fill(topLeftHandle);
            g.fill(topRightHandle);
            g.fill(bottomRightHandle);
            g.fill(bottomLeftHandle);

            g.dispose();
        }
    }

    public Cursor getTransformCursor() {
        return transformCursor;
    }

    public void setTransformCursor(Cursor transformCursor) {
        this.transformCursor = transformCursor;
        setToolCursor(transformCursor);
    }
}
