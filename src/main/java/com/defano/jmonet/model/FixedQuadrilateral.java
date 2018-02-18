package com.defano.jmonet.model;

import java.awt.*;

public class FixedQuadrilateral implements Quadrilateral {

    private final Point topLeft, topRight, bottomLeft, bottomRight;

    public FixedQuadrilateral(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public Point getTopLeft() {
        return topLeft;
    }

    @Override
    public Point getTopRight() {
        return topRight;
    }

    @Override
    public Point getBottomLeft() {
        return bottomLeft;
    }

    @Override
    public Point getBottomRight() {
        return bottomRight;
    }
}
