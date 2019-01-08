package com.defano.jmonet.tools;

import com.defano.jmonet.algo.fill.*;
import com.defano.jmonet.algo.transform.image.FloodFillTransform;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BasicTool;
import com.defano.jmonet.tools.util.CursorFactory;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Tool that performs a flood-fill of all transparent pixels.
 */
@SuppressWarnings("unused")
public class FillTool extends BasicTool implements SurfaceInteractionObserver {

    private Cursor fillCursor = CursorFactory.makeBucketCursor();
    private BoundaryFunction boundaryFunction = new DefaultBoundaryFunction();
    private FillFunction fillFunction = new DefaultFillFunction();

    public FillTool() {
        super(PaintToolType.FILL);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        // Nothing to do if no fill paint is specified
        if (getToolAttributes().getFillPaint().isPresent()) {
            getScratch().clear();

            BufferedImage filled = new FloodFillTransform(getToolAttributes().getFillPaint().get(), imageLocation, fillFunction, boundaryFunction).apply(getCanvas().getCanvasImage());
            getScratch().setAddScratch(filled, new Rectangle(getCanvas().getCanvasSize()));

            getCanvas().commit();
            getCanvas().repaint();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getFillCursor());
    }

    /**
     * Gets the cursor associated with this tool.
     * @return The cursor associated with this tool.
     */
    public Cursor getFillCursor() {
        return fillCursor;
    }

    /**
     * Sets the cursor associated with this tool.
     * @param fillCursor The cursor to be displayed when the fill tool is active.
     */
    public void setFillCursor(Cursor fillCursor) {
        this.fillCursor = fillCursor;
        setToolCursor(fillCursor);
    }

    /**
     * Gets the function used to detect when paint flooding a region has reached a boundary. See
     * BoundaryFunction for details.
     *
     * @return The current boundary function in use.
     */
    public BoundaryFunction getBoundaryFunction() {
        return boundaryFunction;
    }

    /**
     * Sets the function used to detect when paint flooding a region has reached a boundary. See
     * BoundaryFunction for details.
     *
     * @param boundaryFunction The boundary function to use.
     */
    public void setBoundaryFunction(BoundaryFunction boundaryFunction) {
        this.boundaryFunction = boundaryFunction;

        if (boundaryFunction == null) {
            this.boundaryFunction = new DefaultBoundaryFunction();
        }
    }

    /**
     * Gets the function used to color the canvas with paint flooding a region. See {@link FillFunction} for details.
     * @return The fill function being used.
     */
    public FillFunction getFillFunction() {
        return fillFunction;
    }

    /**
     * Sets the function used to color the canvas with paint flooding a region. See {@link FillFunction} for details.
     * @param fillFunction The fill function to use
     */
    public void setFillFunction(FillFunction fillFunction) {
        this.fillFunction = fillFunction;

        if (fillFunction == null) {
            this.fillFunction = new DefaultFillFunction();
        }
    }

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }
}
