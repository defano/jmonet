package com.defano.jmonet.tools.brushes;

import com.defano.jmonet.tools.util.MathUtils;

import java.awt.*;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 * A stroke in which every point on the stroked shape's path can be stamped/marked independently. Useful for producing
 * strokes that emulates a "pen" whose tip is of some arbitrary shape.
 */
public abstract class StampStroke implements Stroke {

    private int interval = 1;
    private double flatness = 2.0;

    /**
     * Stamps a given point on a stroked shape's path.
     * <p>
     * This method should append zero or more shapes to given the path, each of which represents the mark that the "pen"
     * should leave at this point on the path. Typically, the stamped shape should be translated such that it's center-
     * point is located at the given point.
     *
     * @param path  A path of the stroked shape
     * @param point A point in the given path that should be stroked.
     */
    public abstract void stampPoint(GeneralPath path, Point point);

    /**
     * {@inheritDoc}
     */
    public Shape createStrokedShape(Shape shape) {

        float[] coordinates = new float[6];
        Point thisPoint;
        Point lastPoint = null;

        GeneralPath strokedShape = new GeneralPath(new BasicStroke(0f).createStrokedShape(shape));

        // FlatteningPathIterator removes curved segments from the path
        for (PathIterator i = new FlatteningPathIterator(shape.getPathIterator(null), 1); !i.isDone(); i.next()) {
            switch (i.currentSegment(coordinates)) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    thisPoint = new Point((int) coordinates[0], (int) coordinates[1]);
                    stampLine(strokedShape, lastPoint, thisPoint);
                    lastPoint = thisPoint;
                    break;
                default:
                    // Nothing to do
            }
        }

        return strokedShape;
    }

    /**
     * Stamps the line formed by p1, p2 interpolating points on the line as indicated by
     * {@link #setInterpolationInterval(int)}.
     *
     * @param path  The path onto which stamps should be added
     * @param start The start point of the line
     * @param end   The end point of the line
     */
    private void stampLine(GeneralPath path, Point start, Point end) {
        if (start != null && interval > 0) {
            for (Point interpolated : MathUtils.linearInterpolation(start, end, interval)) {
                stampPoint(path, interpolated);
            }
        }

        stampPoint(path, end);
    }

    /**
     * Gets the linear interpolation interval currently in use; a value less than 1 indicates that interpolation is
     * disabled.
     *
     * @return The interpolation interval.
     */
    @SuppressWarnings("unused")
    public int getInterpolationInterval() {
        return interval;
    }

    /**
     * Defines the linear interpolation interval used when stamping the stroke shape. An interval less than 1 disables
     * interpolation.
     * <p>
     * When interval is positive, every interval pixel on the stroked shape's path is stroked; when less than 1, only
     * points returned by the shape's path iterator are stamped.
     * <p>
     * For example, when disabled (interval=0), stroking a line shape will invoke
     * {@link #stampPoint(GeneralPath, Point)} only twice; at the start and end points of the line). When interval = 1,
     * stroking the same line will invoke {@link #stampPoint(GeneralPath, Point)} at every pixel on the line formed
     * between the two points. When interval = 2, every other pixel will be stamped.
     *
     * @param interval Specifies every nth pixel between control points to stamp; value less than 1 stamps only control
     *                 points.
     */
    public void setInterpolationInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Gets the flatness used to render curved surfaces. See {@link #setFlatness(double)}.
     *
     * @return The current flatness; default is 1
     */
    @SuppressWarnings("unused")
    public double getFlatness() {
        return flatness;
    }

    /**
     * Defines the flatness of curved segments of the stamped path. Smaller values produce smoother curves; larger
     * values produce flatter, approximated curves. Smaller values produce a more complex stroked shape that will take
     * longer to render.
     * <p>
     * Curved portions of a stroked path are recursively "flattened" into linear sub-segments, such that the final path
     * is comprised only of linear segments. This value defines the maximum length of any linear sub-segment on the
     * final path.
     * <p>
     * See {@link FlatteningPathIterator}.
     *
     * @param flatness The flatness, in pixels. Value must be greater than or equal to 0.
     */
    @SuppressWarnings("unused")
    public void setFlatness(double flatness) {
        this.flatness = flatness;
    }
}