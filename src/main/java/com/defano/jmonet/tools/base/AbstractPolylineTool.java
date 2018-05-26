package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Mouse and keyboard handler for tools that define multiple segments/points along a path. Click to define a point,
 * double-click to close/complete the shape. When the shift key is held down, line segments will be constrained to
 * the nearest 15-degree angle.
 */
public abstract class AbstractPolylineTool extends PaintTool {

    private final List<Point> points = new ArrayList<>();
    private Point currentPoint = null;

    /**
     * Draws one or more sides (edges) of a polygon which is not filled and may not be closed.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The current stroke context.
     * @param strokePaint The current paint context.
     * @param xPoints An array of x points, see {@link Graphics2D#drawPolyline(int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#drawPolyline(int[], int[], int)}
     */
    protected abstract void strokePolyline(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints);

    /**
     * Draws one or more sides (edges) of a polygon, closing the shape as needed.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The current stroke context.
     * @param strokePaint The current paint context.
     * @param xPoints An array of x points, see {@link Graphics2D#drawPolygon(int[], int[], int)} (int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#drawPolygon(int[], int[], int)} (int[], int[], int)}
     */
    protected abstract void strokePolygon(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints);

    /**
     * Draws a filled polygon.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param fillPaint The paint with which to fill the polyfon
     * @param xPoints An array of x points, see {@link Graphics2D#fillPolygon(int[], int[], int)} (int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#fillPolygon(int[], int[], int)} (int[], int[], int)}
     */
    protected abstract void fillPolygon(Scratch scratch, Paint fillPaint, int[] xPoints, int[] yPoints);

    public AbstractPolylineTool(PaintToolType type) {
        super(type);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());

        // Nothing to do if initial point is not yet established
        if (points.size() == 0) {
            return;
        }

        if (e.isShiftDown()) {
            Point lastPoint = points.get(points.size() - 1);
            currentPoint = Geometry.line(lastPoint, e.getPoint(), getConstrainedAngle());
            points.add(currentPoint);
        } else {
            currentPoint = imageLocation;
            points.add(currentPoint);
        }

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        getScratch().clear();
        strokePolyline(getScratch(), getStroke(), getStrokePaint(), xs, ys);
        getCanvas().repaint();

        points.remove(points.size() - 1);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        // User double-clicked; complete the polygon
        if (e.getClickCount() > 1 && points.size() > 1) {
            points.add(currentPoint);
            commitPolygon();
        }

        // First click (creating initial point)
        else if (currentPoint == null) {
            points.add(imageLocation);
        }

        // Single click with initial point established
        else {
            points.add(currentPoint);
        }
    }

    private void commitPolygon() {
        getScratch().clear();

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        points.clear();
        currentPoint = null;

        if (getFillPaint().isPresent()) {
            fillPolygon(getScratch(), getFillPaint().get(), xs, ys);
        }

        strokePolygon(getScratch(), getStroke(), getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    private void commitPolyline() {
        getScratch().clear();

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        points.clear();
        currentPoint = null;

        strokePolyline(getScratch(), getStroke(), getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {

        // Ignore escape unless at least one point has been defined
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && points.size() > 0) {
            points.add(currentPoint);
            commitPolyline();
        }
    }
}
