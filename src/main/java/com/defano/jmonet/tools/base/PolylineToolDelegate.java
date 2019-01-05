package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;

import java.awt.*;

public interface PolylineToolDelegate {
    /**
     * Draws one or more sides (edges) of a polygon which is not filled and may not be closed.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The current stroke context.
     * @param strokePaint The current paint context.
     * @param xPoints An array of x points, see {@link Graphics2D#drawPolyline(int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#drawPolyline(int[], int[], int)}
     */
    void strokePolyline(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints);

    /**
     * Draws one or more sides (edges) of a polygon, closing the shape as needed.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The current stroke context.
     * @param strokePaint The current paint context.
     * @param xPoints An array of x points, see {@link Graphics2D#drawPolygon(int[], int[], int)} (int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#drawPolygon(int[], int[], int)} (int[], int[], int)}
     */
    void strokePolygon(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints);

    /**
     * Draws a filled polygon.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param fillPaint The paint with which to fill the polyfon
     * @param xPoints An array of x points, see {@link Graphics2D#fillPolygon(int[], int[], int)} (int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#fillPolygon(int[], int[], int)} (int[], int[], int)}
     */
    void fillPolygon(Scratch scratch, Paint fillPaint, int[] xPoints, int[] yPoints);
}
