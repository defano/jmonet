package com.defano.jmonet.tools;

import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScaleTool extends AbstractTransformTool {

    public ScaleTool() {
        super(PaintToolType.SCALE);
    }

    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.getTopLeft().setLocation(newPosition);

        if (quadrilateral.getTopLeft().x >= quadrilateral.getTopRight().x) {
            quadrilateral.getTopLeft().x = quadrilateral.getTopRight().x - 1;
        }

        if (quadrilateral.getTopLeft().y >= quadrilateral.getBottomLeft().y) {
            quadrilateral.getTopLeft().y = quadrilateral.getBottomLeft().y - 1;
        }

        quadrilateral.getTopRight().y = quadrilateral.getTopLeft().y;
        quadrilateral.getBottomLeft().x = quadrilateral.getTopLeft().x;

        setSelectedImage(resize(quadrilateral));
    }

    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.getTopRight().setLocation(newPosition);

        if (quadrilateral.getTopRight().x <= quadrilateral.getTopLeft().x) {
            quadrilateral.getTopRight().x = quadrilateral.getTopLeft().x + 1;
        }

        if (quadrilateral.getTopRight().y >= quadrilateral.getBottomRight().y) {
            quadrilateral.getTopRight().y = quadrilateral.getBottomRight().y - 1;
        }

        quadrilateral.getTopLeft().y = quadrilateral.getTopRight().y;
        quadrilateral.getBottomRight().x = quadrilateral.getTopRight().x;

        setSelectedImage(resize(quadrilateral));
    }

    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.getBottomLeft().setLocation(newPosition);

        if (quadrilateral.getBottomLeft().x >= quadrilateral.getBottomRight().x) {
            quadrilateral.getBottomLeft().x = quadrilateral.getBottomRight().x - 1;
        }

        if (quadrilateral.getBottomLeft().y <= quadrilateral.getTopLeft().y) {
            quadrilateral.getBottomLeft().y = quadrilateral.getTopLeft().y + 1;
        }

        quadrilateral.getTopLeft().x = quadrilateral.getBottomLeft().x;
        quadrilateral.getBottomRight().y = quadrilateral.getBottomLeft().y;

        setSelectedImage(resize(quadrilateral));
    }

    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.getBottomRight().setLocation(newPosition);

        if (quadrilateral.getBottomRight().x <= quadrilateral.getBottomLeft().x) {
            quadrilateral.getBottomRight().x = quadrilateral.getBottomLeft().x + 1;
        }

        if (quadrilateral.getBottomRight().y <= quadrilateral.getTopRight().y) {
            quadrilateral.getBottomRight().y = quadrilateral.getTopRight().y + 1;
        }

        quadrilateral.getBottomLeft().y = quadrilateral.getBottomRight().y;
        quadrilateral.getTopRight().x = quadrilateral.getBottomRight().x;

        setSelectedImage(resize(quadrilateral));
    }

    private BufferedImage resize(FlexQuadrilateral quadrilateral) {
        Rectangle resizedBounds = quadrilateral.getShape().getBounds();
        BufferedImage resized = new BufferedImage(resizedBounds.width, resizedBounds.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) resized.getGraphics();
        g.drawImage(getOriginalImage(), 0,0, resizedBounds.width, resizedBounds.height, null);
        g.dispose();

        return resized;
    }
}