package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.base.BasicTool;
import com.defano.jmonet.tools.builder.PaintToolBuilder;
import com.defano.jmonet.tools.cursors.CursorFactory;
import com.defano.jmonet.transform.image.FloodFillTransform;
import com.google.inject.Inject;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Tool that performs a flood-fill of all transparent pixels.
 */
@SuppressWarnings("unused")
public class FillTool extends BasicTool {

    @Inject
    private FloodFillTransform floodFill;

    /**
     * Tool must be constructed via {@link PaintToolBuilder} to handle dependency
     * injection.
     */
    FillTool() {
        super(PaintToolType.FILL);
    }

    @Override
    public Cursor getDefaultCursor() {
        return CursorFactory.makeBucketCursor();
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        ToolAttributes attributes = getToolAttributes();

        // Nothing to do if no fill paint is specified
        if (attributes.getFillPaint().isPresent()) {
            getScratch().clear();

            floodFill.setFillPaint(attributes.getFillPaint().get());
            floodFill.setOrigin(imageLocation);
            floodFill.setFill(attributes.getFillFunction());
            floodFill.setBoundaryFunction(attributes.getBoundaryFunction());

            getScratch().setAddScratch(floodFill.apply(getCanvas().getCanvasImage()), new Rectangle(getCanvas().getCanvasSize()));

            getCanvas().commit();
            getCanvas().repaint();
        }
    }

}
