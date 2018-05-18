package com.defano.jmonet.tools;

import com.defano.jmonet.algo.transform.image.ProjectionTransform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;

import java.awt.*;

/**
 * Tool for making either the left or right side appear closer/further away than the other.
 */
public class PerspectiveTool extends AbstractTransformTool {

    public PerspectiveTool() {
        super(PaintToolType.PERSPECTIVE);
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        int bottomLeft = quadrilateral.getBottomLeft().y - (newPosition.y - quadrilateral.getTopLeft().y);

        quadrilateral.setBottomLeft(new Point(quadrilateral.getBottomLeft().x, bottomLeft));
        quadrilateral.setTopLeft(new Point(quadrilateral.getTopLeft().x, newPosition.y));

        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        int bottomRight = quadrilateral.getBottomRight().y - (newPosition.y - quadrilateral.getTopRight().y);

        quadrilateral.setBottomRight(new Point(quadrilateral.getBottomRight().x, bottomRight));
        quadrilateral.setTopRight(new Point(quadrilateral.getTopRight().x, newPosition.y));

        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        int topLeft = quadrilateral.getTopLeft().y - (newPosition.y - quadrilateral.getBottomLeft().y);

        quadrilateral.setTopLeft(new Point(quadrilateral.getTopLeft().x, topLeft));
        quadrilateral.setBottomLeft(new Point(quadrilateral.getBottomLeft().x, newPosition.y));

        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        int topRight = quadrilateral.getTopRight().y - (newPosition.y - quadrilateral.getBottomRight().y);

        quadrilateral.setTopRight(new Point(quadrilateral.getTopRight().x, topRight));
        quadrilateral.setBottomRight(new Point(quadrilateral.getBottomRight().x, newPosition.y));

        setSelectedImage(new ProjectionTransform(quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)).apply(getOriginalImage()));
    }

}
