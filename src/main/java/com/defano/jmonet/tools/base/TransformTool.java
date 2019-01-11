package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public abstract class TransformTool extends SelectionTool implements SurfaceInteractionObserver, SelectionToolDelegate {

    private final static int HANDLE_SIZE = 8;

    private TransformToolDelegate transformToolDelegate;
    private BufferedImage originalImage;
    private Rectangle selectionBounds;
    private FlexQuadrilateral transformBounds;

    private Rectangle topLeftHandle, topRightHandle, bottomRightHandle, bottomLeftHandle;
    private boolean dragTopLeft, dragTopRight, dragBottomRight, dragBottomLeft;

    public TransformTool(PaintToolType type) {
        super(type);
        super.setSelectionToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void createSelection(Rectangle bounds) {
        super.createSelection(bounds);
        originalImage = getSelectedImage();
    }

    /** {@inheritDoc} */
    @Override
    public void createSelection(BufferedImage image, Point location) {
        super.createSelection(image, location);
        originalImage = image;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        if (transformToolDelegate == null) {
            throw new IllegalStateException("Transform tool delegate not set.");
        }

        // Selection exists, see if we're dragging a handle
        if (hasSelection()) {

            if (dragTopLeft || dragTopRight || dragBottomLeft || dragBottomRight) {
                setDirty();
            }

            if (dragTopLeft) {
                transformToolDelegate.moveTopLeft(transformBounds, imageLocation, e.isShiftDown());
                redrawSelection(true);
            } else if (dragTopRight) {
                transformToolDelegate.moveTopRight(transformBounds, imageLocation, e.isShiftDown());
                redrawSelection(true);
            } else if (dragBottomLeft) {
                transformToolDelegate.moveBottomLeft(transformBounds, imageLocation, e.isShiftDown());
                redrawSelection(true);
            } else if (dragBottomRight) {
                transformToolDelegate.moveBottomRight(transformBounds, imageLocation, e.isShiftDown());
                redrawSelection(true);
            } else {
                super.mouseDragged(e, imageLocation);
            }

        }

        // No selection, delegate to selection tool to define selection
        else {
            super.mouseDragged(e, imageLocation);
        }
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void addPointToSelectionFrame(Point initialPoint, Point newPoint, boolean isShiftKeyDown) {
        selectionBounds = new Rectangle(initialPoint);
        selectionBounds.add(newPoint);

        int width = selectionBounds.width;
        int height = selectionBounds.height;

        if (isShiftKeyDown) {
            width = height = Math.max(width, height);
        }

        selectionBounds = new Rectangle(selectionBounds.x, selectionBounds.y, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void closeSelectionFrame(Point finalPoint) {
        transformBounds = new FlexQuadrilateral(selectionBounds);
    }

    /** {@inheritDoc} */
    @Override
    public void resetSelection() {
        selectionBounds = null;
        transformBounds = null;

        topLeftHandle = topRightHandle = bottomLeftHandle = bottomRightHandle = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setSelectionOutline(Rectangle bounds) {
        transformBounds = new FlexQuadrilateral(bounds);
    }

    /** {@inheritDoc} */
    @Override
    public Shape getSelectionFrame() {
        return transformBounds != null ? transformBounds.getShape() : selectionBounds;
    }

    /** {@inheritDoc} */
    @Override
    public void translateSelection(int xDelta, int yDelta) {
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

    /** {@inheritDoc} */
    @Override
    protected void drawSelectionFrame() {
        super.drawSelectionFrame();

        if (hasSelection() && transformBounds != null) {

            // Render drag handles on selection bounds
            GraphicsContext g = getCanvas().getScratch().getAddScratchGraphics(this, null);
            g.setPaint(Color.BLACK);

            topLeftHandle = new Rectangle(transformBounds.getTopLeft().x, transformBounds.getTopLeft().y, HANDLE_SIZE, HANDLE_SIZE);
            topRightHandle = new Rectangle(transformBounds.getTopRight().x - HANDLE_SIZE, transformBounds.getTopRight().y, HANDLE_SIZE, HANDLE_SIZE);
            bottomRightHandle = new Rectangle(transformBounds.getBottomRight().x - HANDLE_SIZE, transformBounds.getBottomRight().y - HANDLE_SIZE, HANDLE_SIZE, HANDLE_SIZE);
            bottomLeftHandle = new Rectangle(transformBounds.getBottomLeft().x, transformBounds.getBottomLeft().y - HANDLE_SIZE, HANDLE_SIZE, HANDLE_SIZE);

            g.fill(topLeftHandle);
            g.fill(topRightHandle);
            g.fill(bottomRightHandle);
            g.fill(bottomLeftHandle);
        }
    }

    protected TransformToolDelegate getTransformToolDelegate() {
        return transformToolDelegate;
    }

    protected void setTransformToolDelegate(TransformToolDelegate transformToolDelegate) {
        this.transformToolDelegate = transformToolDelegate;
    }
}
