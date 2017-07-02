package com.defano.jmonet.tools;

import com.defano.jmonet.model.FlexQuadrilateral;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.AbstractTransformTool;
import com.defano.jmonet.algo.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RubberSheetTool extends AbstractTransformTool {

    public RubberSheetTool() {
        super(PaintToolType.RUBBERSHEET);
    }

    @Override
    protected void moveTopLeft(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setTopLeft(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral);
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    @Override
    protected void moveTopRight(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setTopRight(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral);
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    @Override
    protected void moveBottomLeft(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setBottomLeft(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral);
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

    @Override
    protected void moveBottomRight(FlexQuadrilateral quadrilateral, Point newPosition) {
        quadrilateral.setBottomRight(newPosition);
        BufferedImage scaled = Transform.resize(getOriginalImage(), quadrilateral);
        setSelectedImage(Transform.rubberSheet(scaled, quadrilateral.translate(getSelectedImageLocation().x, getSelectedImageLocation().y)));
    }

}
