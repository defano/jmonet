package com.defano.jmonet.tools;

import com.defano.jmonet.transform.image.FloodFillTransform;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BasicTool;
import com.defano.jmonet.tools.attributes.ToolAttributes;
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

    public FillTool() {
        super(PaintToolType.FILL);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        ToolAttributes attributes = getToolAttributes();

        // Nothing to do if no fill paint is specified
        if (attributes.getFillPaint().isPresent()) {
            getScratch().clear();

            BufferedImage filled = new FloodFillTransform(getToolAttributes().getFillPaint().get(), imageLocation, attributes.getFillFunction(), attributes.getBoundaryFunction()).apply(getCanvas().getCanvasImage());
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

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }
}
