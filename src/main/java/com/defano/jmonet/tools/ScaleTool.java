package com.defano.jmonet.tools;

import com.defano.jmonet.algo.Transform;
import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A tool for scaling and resizing the rectangular bounds of an image.
 */
public class ScaleTool extends AbstractTransformTool {

    public ScaleTool() {
        super(PaintToolType.SCALE);
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getBottomRightTopLeftLine(), quadrilateral.getBottomRight(), newPosition);
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

        setSelectedImage(Transform.resize(getOriginalImage(), quadrilateral));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getBottomLeftTopRightLine(), quadrilateral.getBottomLeft(), newPosition);
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

        setSelectedImage(Transform.resize(getOriginalImage(), quadrilateral));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getTopRightBottomLeftLine(), quadrilateral.getTopRight(), newPosition);
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

        setSelectedImage(Transform.resize(getOriginalImage(), quadrilateral));
    }

    /** {@inheritDoc} */
    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition, boolean isShiftDown) {
        if (isShiftDown) {
            newPosition = Geometry.extrapolate(originalQuad().getTopLeftBottomRightLine(), quadrilateral.getTopLeft(), newPosition);
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

        setSelectedImage(Transform.resize(getOriginalImage(), quadrilateral));
    }

    private FlexQuadrilateral originalQuad() {
        return new FlexQuadrilateral(new Rectangle2D.Double(0, 0, getOriginalImage().getWidth(), getOriginalImage().getHeight()));
    }

}