package com.defano.jmonet.tools.brushes;

import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 * A stroke in which every point on the stroked shape's path can be stamped/marked independently. Useful for producing
 * strokes that emulates a "pen" whose tip is of some arbitrary shape.
 */
public abstract class StampStroke implements Stroke {

    private boolean linearInterpolated = true;

    /**
     * Stamps a given point on a stroked shape's path.
     *
     * This method should append zero or more segments/shapes to given the path, each segment/shape representing the
     * mark that the "pen" should leave at this point on the path.
     *
     * @param path A path of the stroked shape
     * @param point A point in the given path that should be stroked.
     */
    public abstract void stampPoint(GeneralPath path, Point point);

    /** {@inheritDoc} */
    public Shape createStrokedShape(Shape shape) {

        Point thisPoint, lastPoint = null;
        GeneralPath strokedShape = new GeneralPath(shape);

        float[] coords = new float[6];
        for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            switch (i.currentSegment(coords)) {
                case PathIterator.SEG_CUBICTO:
                    thisPoint = new Point((int)coords[4], (int)coords[5]);
                    stampPoint(strokedShape, lastPoint, thisPoint); // falls through
                    lastPoint = thisPoint;
                case PathIterator.SEG_QUADTO:
                    thisPoint = new Point((int)coords[2], (int)coords[3]);
                    stampPoint(strokedShape, lastPoint, thisPoint); // falls through
                    lastPoint = thisPoint;
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    thisPoint = new Point((int)coords[0], (int)coords[1]);
                    stampPoint(strokedShape, lastPoint, thisPoint); // falls through
                    lastPoint = thisPoint;
                case PathIterator.SEG_CLOSE:
                    break;
            }
        }

        return strokedShape;
    }

    private void stampPoint(GeneralPath path, Point lastPoint, Point thisPoint) {

        if (lastPoint != null && linearInterpolated) {
            for (Point interpolated : Geometry.linearInterpolation(lastPoint, thisPoint)) {
                stampPoint(path, interpolated);
            }
        }

        stampPoint(path, thisPoint);
    }

    /**
     * Determines if linear interpolation is enabled. See {@link #setLinearInterpolated(boolean)}.
     * @return True to enable; false to disable
     */
    public boolean isLinearInterpolated() {
        return linearInterpolated;
    }

    /**
     * Enables or disables linear interpolation; when enabled, every pixel on the stroked shape's path is stroked; when
     * disabled, only points returned by the shape's path iterator are stroked (for example, when disabled, stroking a
     * line shape will invoke {@link #stampPoint(GeneralPath, Point)} only twice; the start and end points of the
     * line).
     *
     * @param linearInterpolated True to enable linear interpolation; false to disable.
     */
    public void setLinearInterpolated(boolean linearInterpolated) {
        this.linearInterpolated = linearInterpolated;
    }
}