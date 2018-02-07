package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractSelectionTool;
import com.defano.jmonet.tools.selection.TransformableCanvasSelection;
import com.defano.jmonet.tools.selection.TransformableSelection;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Selection tool allowing the user to draw a free-form selection path on the canvas.
 */
public class LassoTool extends AbstractSelectionTool implements TransformableSelection, TransformableCanvasSelection {

    private Path2D selectionBounds;

    public LassoTool() {
        super(PaintToolType.LASSO);
    }

    /** {@inheritDoc} */
    @Override
    public void resetSelection() {
        selectionBounds = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setSelectionOutline(Rectangle bounds) {
        selectionBounds = new Path2D.Double(bounds);
    }

    /** {@inheritDoc} */
    @Override
    protected void addSelectionPoint(Point initialPoint, Point newPoint, boolean isShiftKeyDown) {
        if (selectionBounds == null) {
            selectionBounds = new Path2D.Double();
            selectionBounds.moveTo(initialPoint.getX(), initialPoint.getY());
        }

        selectionBounds.lineTo(newPoint.x, newPoint.y);
    }

    /** {@inheritDoc} */
    @Override
    protected void completeSelection(Point finalPoint) {
        selectionBounds.closePath();
    }

    /** {@inheritDoc} */
    @Override
    public Shape getSelectionOutline() {
        return selectionBounds;
    }

    /** {@inheritDoc} */
    @Override
    public void translateSelection(int xDelta, int yDelta) {
        selectionBounds.transform(AffineTransform.getTranslateInstance(xDelta, yDelta));
    }
}
