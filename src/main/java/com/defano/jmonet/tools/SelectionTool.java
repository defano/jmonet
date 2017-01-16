package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractSelectionTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

public class SelectionTool extends AbstractSelectionTool {

    private Rectangle selectionBounds;

    public SelectionTool() {
        super(PaintToolType.SELECTION);
    }

    @Override
    public void defineSelectionBounds(Point initialPoint, Point currentPoint, boolean constrain) {
        selectionBounds = new Rectangle(initialPoint);
        selectionBounds.add(currentPoint);

        selectionBounds = constrain ?
                Geometry.squareAtAnchor(initialPoint, currentPoint) :
                Geometry.rectangleFromPoints(initialPoint, currentPoint);
    }

    @Override
    public void completeSelectionBounds(Point finalPoint) {
        // Nothing to do
    }

    @Override
    public void resetSelection() {
        selectionBounds = null;
    }

    @Override
    public void setSelectionBounds(Rectangle bounds) {
        selectionBounds = bounds;
    }

    @Override
    public Shape getSelectionOutline() {
        return selectionBounds;
    }

    @Override
    public void adjustSelectionBounds(int xDelta, int yDelta) {
        selectionBounds.setLocation(selectionBounds.x + xDelta, selectionBounds.y + yDelta);
    }

}
