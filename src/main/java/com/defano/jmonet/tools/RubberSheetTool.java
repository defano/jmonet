package com.defano.jmonet.tools;

import com.defano.jmonet.tools.base.TransformToolDelegate;
import com.defano.jmonet.transform.image.RubbersheetTransform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.TransformTool;

import java.awt.*;

/**
 * Tool for performing a rubber sheet projection of the image.
 */
public class RubberSheetTool extends TransformTool implements TransformToolDelegate {

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    RubberSheetTool() {
        super(PaintToolType.RUBBERSHEET);
        setTransformToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopLeft(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopRight(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomLeft(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomRight(newPosition);
        setSelectedImage(new RubbersheetTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

}
