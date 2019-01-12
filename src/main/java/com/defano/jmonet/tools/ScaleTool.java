package com.defano.jmonet.tools;

import com.defano.jmonet.transform.image.ScaleTransform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.TransformTool;
import com.defano.jmonet.tools.base.TransformToolDelegate;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A tool for scaling and resizing the rectangular bounds of an image.
 */
public class ScaleTool extends TransformTool implements TransformToolDelegate {

    public ScaleTool() {
        super(PaintToolType.SCALE);
        setTransformToolDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getBottomRightTopLeftDiagonal(), quadrilateral.getBottomRight(), newPosition);
        }

        quadrilateral.getTopLeft().setLocation(newPosition);

        if (quadrilateral.getTopLeft().x >= quadrilateral.getTopRight().x) {
            quadrilateral.getTopLeft().x = quadrilateral.getTopRight().x - 1;
        }

        if (quadrilateral.getTopLeft().y >= quadrilateral.getBottomLeft().y) {
            quadrilateral.getTopLeft().y = quadrilateral.getBottomLeft().y - 1;
        }

        quadrilateral.getTopRight().y = quadrilateral.getTopLeft().y;
        quadrilateral.getBottomLeft().x = quadrilateral.getTopLeft().x;

        setSelectedImage(new ScaleTransform(quadrilateral.getShape().getBounds().getSize()).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getBottomLeftTopRightDiagonal(), quadrilateral.getBottomLeft(), newPosition);
        }

        quadrilateral.getTopRight().setLocation(newPosition);

        if (quadrilateral.getTopRight().x <= quadrilateral.getTopLeft().x) {
            quadrilateral.getTopRight().x = quadrilateral.getTopLeft().x + 1;
        }

        if (quadrilateral.getTopRight().y >= quadrilateral.getBottomRight().y) {
            quadrilateral.getTopRight().y = quadrilateral.getBottomRight().y - 1;
        }

        quadrilateral.getTopLeft().y = quadrilateral.getTopRight().y;
        quadrilateral.getBottomRight().x = quadrilateral.getTopRight().x;

        setSelectedImage(new ScaleTransform(quadrilateral.getShape().getBounds().getSize()).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getTopRightBottomLeftDiagonal(), quadrilateral.getTopRight(), newPosition);
        }

        quadrilateral.getBottomLeft().setLocation(newPosition);

        if (quadrilateral.getBottomLeft().x >= quadrilateral.getBottomRight().x) {
            quadrilateral.getBottomLeft().x = quadrilateral.getBottomRight().x - 1;
        }

        if (quadrilateral.getBottomLeft().y <= quadrilateral.getTopLeft().y) {
            quadrilateral.getBottomLeft().y = quadrilateral.getTopLeft().y + 1;
        }

        quadrilateral.getTopLeft().x = quadrilateral.getBottomLeft().x;
        quadrilateral.getBottomRight().y = quadrilateral.getBottomLeft().y;

        setSelectedImage(new ScaleTransform(quadrilateral.getShape().getBounds().getSize()).apply(getOriginalImage()));
    }

    /** {@inheritDoc} */
    @Override
    public void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getTopLeftBottomRightDiagonal(), quadrilateral.getTopLeft(), newPosition);
        }

        quadrilateral.getBottomRight().setLocation(newPosition);

        if (quadrilateral.getBottomRight().x <= quadrilateral.getBottomLeft().x) {
            quadrilateral.getBottomRight().x = quadrilateral.getBottomLeft().x + 1;
        }

        if (quadrilateral.getBottomRight().y <= quadrilateral.getTopRight().y) {
            quadrilateral.getBottomRight().y = quadrilateral.getTopRight().y + 1;
        }

        quadrilateral.getBottomLeft().y = quadrilateral.getBottomRight().y;
        quadrilateral.getTopRight().x = quadrilateral.getBottomRight().x;

        setSelectedImage(new ScaleTransform(quadrilateral.getShape().getBounds().getSize()).apply(getOriginalImage()));
    }

    private FlexQuadrilateral originalQuad() {
        return new FlexQuadrilateral(new Rectangle2D.Double(0, 0, getOriginalImage().getWidth(), getOriginalImage().getHeight()));
    }

}