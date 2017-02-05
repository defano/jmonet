package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractSelectionTool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Selection tool allowing the user to draw a free-form selection path on the canvas.
 */
public class LassoTool extends AbstractSelectionTool {

    private Path2D selectionBounds;

    public LassoTool() {
        super(PaintToolType.LASSO);
    }

    @Override
    protected void resetSelection() {
        selectionBounds = null;
    }

    @Override
    protected void setSelectionBounds(Rectangle bounds) {
        selectionBounds = new Path2D.Double(bounds);
    }

    @Override
    protected void addSelectionPoint(Point initialPoint, Point newPoint, boolean isShiftKeyDown) {
        if (selectionBounds == null) {
            selectionBounds = new Path2D.Double();
            selectionBounds.moveTo(initialPoint.getX(), initialPoint.getY());
        }

        selectionBounds.lineTo(newPoint.x, newPoint.y);
    }

    @Override
    protected void completeSelection(Point finalPoint) {
        selectionBounds.closePath();
    }

    @Override
    public Shape getSelectionOutline() {
        return selectionBounds;
    }

    @Override
    protected void adjustSelectionBounds(int xDelta, int yDelta) {
        selectionBounds.transform(AffineTransform.getTranslateInstance(xDelta, yDelta));
    }
}
