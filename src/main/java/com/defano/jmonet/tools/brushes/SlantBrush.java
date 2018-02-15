package com.defano.jmonet.tools.brushes;

import com.defano.jmonet.model.FixedQuadrilateral;

import java.awt.*;

public class SlantBrush extends QuadrilateralBrush {

    public SlantBrush(int height, int width, double angleRadians) {
        super(new FixedQuadrilateral(
                new Point((int)(0 + height * Math.sin(angleRadians)), 0),
                new Point((int)(width + height * Math.sin(angleRadians)), 0),
                new Point(0, height),
                new Point(width, height)
        ));
    }
}
