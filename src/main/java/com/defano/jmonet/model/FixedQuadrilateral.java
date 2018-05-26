package com.defano.jmonet.model;

import java.awt.*;

/**
 * A model of a quadrilateral with fixed dimensions.
 */
public class FixedQuadrilateral implements Quadrilateral {

    private final Point topLeft, topRight, bottomLeft, bottomRight;

    public FixedQuadrilateral(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getTopLeft() {
        return topLeft;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getTopRight() {
        return topRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBottomLeft() {
        return bottomLeft;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getBottomRight() {
        return bottomRight;
    }
}
