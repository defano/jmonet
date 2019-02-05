package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;

import java.awt.*;

/**
 * A delegate class responsible for rendering shapes drawn by the {@link PathToolDelegate}.
 */
public interface PathToolDelegate {

    /**
     * Begins drawing a path on the given graphics context.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param strokePaint The paint with which to draw
     * @param initialPoint The first point defined on the path
     */
    void startPath(Scratch scratch, Stroke stroke, Paint strokePaint, Point initialPoint);

    /**
     * Adds a point to the path begun via a call to {@link #startPath(Scratch, Stroke, Paint, Point)}.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param strokePaint The paint with which to draw
     * @param lastPoint The last point added to the current path
     * @param thisPoint The new point to add to the current path
     */
    void addPoint(Scratch scratch, Stroke stroke, Paint strokePaint, Point lastPoint, Point thisPoint);

    /**
     * Completes the path begun via a call to {@link #startPath(Scratch, Stroke, Paint, Point)}.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The stroke with which to draw
     * @param strokePaint The paint with which to render the stroke
     * @param fillPaint The paint with which to fill the shape, null to indicate shape should not be filled
     */
    void completePath(Scratch scratch, Stroke stroke, Paint strokePaint, Paint fillPaint);

}
