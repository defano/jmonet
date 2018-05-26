package com.defano.jmonet.tools;

import com.defano.jmonet.algo.transform.image.ProjectionTransform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;

import java.awt.*;

/**
 * Tool for performing a projection of a selected image onto an arbitrary quadrilateral.
 */
public class ProjectionTool extends AbstractTransformTool {

    public ProjectionTool() {
        super(PaintToolType.PROJECTION);
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopLeft(newPosition);
        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setTopRight(newPosition);
        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomLeft(newPosition);
        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.setBottomRight(newPosition);
        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }
}
