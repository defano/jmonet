package com.defano.jmonet.tools;

import com.defano.jmonet.algo.transform.image.SlantTransform;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.tools.base.TransformTool;
import com.defano.jmonet.tools.base.TransformToolDelegate;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

/**
 * Tool for drawing a rectangular selection boundary with drag-handles to shear/slant the image from the top or bottom.
 */
public class SlantTool extends TransformTool implements TransformToolDelegate {

    public SlantTool() {
        super(PaintToolType.SLANT);
        setTransformToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.getTopRight().x += newPosition.x - quadrilateral.getTopLeft().x;
        quadrilateral.getTopLeft().x = newPosition.x;

        int xTranslation = (quadrilateral.getTopLeft().x - getSelectionFrame().getBounds().x) / 2;
        setSelectedImage(new SlantTransform(getTheta(quadrilateral), xTranslation).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.getTopLeft().x += newPosition.x - quadrilateral.getTopRight().x;
        quadrilateral.getTopRight().x = newPosition.x;

        int xTranslation = (quadrilateral.getTopLeft().x - getSelectionFrame().getBounds().x) / 2;
        setSelectedImage(new SlantTransform(getTheta(quadrilateral), xTranslation).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.getBottomRight().x += newPosition.x - quadrilateral.getBottomLeft().x;
        quadrilateral.getBottomLeft().x = newPosition.x;

        int xTranslation = ((getSelectionFrame().getBounds().x + getSelectionFrame().getBounds().width) - quadrilateral.getBottomRight().x) / 2;
        setSelectedImage(new SlantTransform(getTheta(quadrilateral), xTranslation).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        quadrilateral.getBottomLeft().x += newPosition.x - quadrilateral.getBottomRight().x;
        quadrilateral.getBottomRight().x = newPosition.x;

        int xTranslation = ((getSelectionFrame().getBounds().x + getSelectionFrame().getBounds().width) - quadrilateral.getBottomRight().x) / 2;
        setSelectedImage(new SlantTransform(getTheta(quadrilateral), xTranslation).apply(getOriginalImage()));
    }

    private double getTheta(FlexQuadrilateral quadrilateral) {
        Point p = new Point(quadrilateral.getBottomLeft().x, quadrilateral.getTopLeft().y);
        return Geometry.theta(quadrilateral.getBottomLeft(), p, quadrilateral.getTopLeft());
    }
}
