package com.defano.jmonet.tools;

import com.defano.jmonet.algo.Transform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;

import java.awt.*;

/**
 * Tools for performing a homography projective of the image.
 */
public class ProjectionTool extends AbstractTransformTool {

    public ProjectionTool() {
        super(PaintToolType.PROJECTION);
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setTopLeft(newPosition);
        setSelectedImage(Transform.project(getOriginalImage(), quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setTopRight(newPosition);
        setSelectedImage(Transform.project(getOriginalImage(), quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setBottomLeft(newPosition);
        setSelectedImage(Transform.project(getOriginalImage(), quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setBottomRight(newPosition);
        setSelectedImage(Transform.project(getOriginalImage(), quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }
}
