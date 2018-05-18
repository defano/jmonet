package com.defano.jmonet.tools;

import com.defano.jmonet.algo.transform.image.RubbersheetTransform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;

import java.awt.*;

/**
 * Tool for performing a rubber sheet projection of the image.
 */
public class RubberSheetTool extends AbstractTransformTool {

    public RubberSheetTool() {
        super(PaintToolType.RUBBERSHEET);
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopLeft(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopRight(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomLeft(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomRight(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

}
