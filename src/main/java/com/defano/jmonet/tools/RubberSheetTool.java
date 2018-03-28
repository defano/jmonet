package com.defano.jmonet.tools;

import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;
import com.defano.jmonet.algo.transform.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral.getShape().getBounds().getSize());
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopRight(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral.getShape().getBounds().getSize());
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomLeft(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral.getShape().getBounds().getSize());
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomRight(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral.getShape().getBounds().getSize());
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

}
