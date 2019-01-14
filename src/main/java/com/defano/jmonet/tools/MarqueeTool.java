package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.SelectionTool;
import com.defano.jmonet.tools.selection.TransformableCanvasSelection;
import com.defano.jmonet.tools.selection.TransformableSelection;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

/**
 * A tool for drawing a rectangular selection on the canvas.
 */
public class MarqueeTool extends SelectionTool implements TransformableSelection, TransformableCanvasSelection {

    private Rectangle selectionBounds;

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    MarqueeTool() {
        super(PaintToolType.SELECTION);
    }

    /** {@inheritDoc} */
    @Override
    public Shape getSelectionFrame() {
        return selectionBounds;
    }

    /** {@inheritDoc} */
    @Override
    public void addPointToSelectionFrame(Point initialPoint, Point newPoint, boolean isShiftKeyDown) {
        selectionBounds = new Rectangle(initialPoint);
        selectionBounds.add(newPoint);

        // #24: Disallow constraint if it results in selection outside canvas bounds
        Rectangle canvasBounds = new Rectangle(0, 0, getCanvas().getCanvasSize().width, getCanvas().getCanvasSize().height);
        if (isShiftKeyDown && canvasBounds.contains(Geometry.square(initialPoint, newPoint))) {
            selectionBounds = Geometry.square(initialPoint, newPoint);
        } else {
            selectionBounds = Geometry.rectangle(initialPoint, newPoint);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void closeSelectionFrame(Point finalPoint) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void resetSelection() {
        selectionBounds = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setSelectionOutline(Rectangle bounds) {
        selectionBounds = bounds;
    }

    /** {@inheritDoc} */
    @Override
    public void translateSelection(int xDelta, int yDelta) {
        selectionBounds.setLocation(selectionBounds.x + xDelta, selectionBounds.y + yDelta);
    }

}
