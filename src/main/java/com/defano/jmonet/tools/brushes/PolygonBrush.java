package com.defano.jmonet.tools.brushes;

import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;

/**
 * A regular polygon-shaped brush. Produces a stroke where every point on the stroked path is "stamped" with a filled
 * regular polygon (i.e., triangle, square, pentagon, hexagon, etc...).
 */
public class PolygonBrush extends ShapeBrush {

    /**
     * Produces a regular polygon-shaped brush.
     * @param sides The number of sides of the polygon
     * @param length The length of each side of the polygon
     * @param rotationRadians The angle of rotation, in radians
     */
    public PolygonBrush(int sides, int length, double rotationRadians) {
        super(Geometry.polygon(new Point(0,0), sides, length, rotationRadians), false);
    }

}
