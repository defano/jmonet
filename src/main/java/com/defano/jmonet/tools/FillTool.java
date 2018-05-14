package com.defano.jmonet.tools;

import com.defano.jmonet.algo.*;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Tool that performs a flood-fill of all transparent pixels.
 */
public class FillTool extends PaintTool {

    private Cursor fillCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private BoundaryFunction boundaryFunction = new DefaultBoundaryFunction();
    private FillFunction fillFunction = new DefaultFillFunction();

    public FillTool() {
        super(PaintToolType.FILL);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        // Nothing to do if no fill paint is specified
        if (getFillPaint().isPresent()) {
            getScratch().clear();

            FloodFill.floodFill(
                    getCanvas(),
                    getFillPaint().get(),
                    imageLocation.x,
                    imageLocation.y,
                    fillFunction,
                    boundaryFunction);

            getCanvas().commit();
            getCanvas().invalidateCanvas();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getFillCursor());
    }

    public Cursor getFillCursor() {
        return fillCursor;
    }

    public void setFillCursor(Cursor fillCursor) {
        this.fillCursor = fillCursor;
        setToolCursor(fillCursor);
    }

    public BoundaryFunction getBoundaryFunction() {
        return boundaryFunction;
    }

    public void setBoundaryFunction(BoundaryFunction boundaryFunction) {
        this.boundaryFunction = boundaryFunction;

        if (boundaryFunction == null) {
            this.boundaryFunction = new DefaultBoundaryFunction();
        }
    }

    public FillFunction getFillFunction() {
        return fillFunction;
    }

    public void setFillFunction(FillFunction fillFunction) {
        this.fillFunction = fillFunction;

        if (fillFunction == null) {
            this.fillFunction = new DefaultFillFunction();
        }
    }

}
