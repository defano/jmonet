package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class PolylineTool extends BasicTool implements SurfaceInteractionObserver {

    private final List<Point> points = new ArrayList<>();
    private Point currentPoint = null;

    public PolylineTool(PaintToolType toolType) {
        super(toolType);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /**
     * Draws one or more sides (edges) of a polygon which is not filled and may not be closed.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The current stroke context.
     * @param strokePaint The current paint context.
     * @param xPoints An array of x points, see {@link Graphics2D#drawPolyline(int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#drawPolyline(int[], int[], int)}
     */
    public abstract void strokePolyline(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints);

    /**
     * Draws one or more sides (edges) of a polygon, closing the shape as needed.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param stroke The current stroke context.
     * @param strokePaint The current paint context.
     * @param xPoints An array of x points, see {@link Graphics2D#drawPolygon(int[], int[], int)} (int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#drawPolygon(int[], int[], int)} (int[], int[], int)}
     */
    public abstract void strokePolygon(Scratch scratch, Stroke stroke, Paint strokePaint, int[] xPoints, int[] yPoints);

    /**
     * Draws a filled polygon.
     *
     * @param scratch The scratch buffer on which to draw.
     * @param fillPaint The paint with which to fill the polyfon
     * @param xPoints An array of x points, see {@link Graphics2D#fillPolygon(int[], int[], int)} (int[], int[], int)}
     * @param yPoints An array of y points, see {@link Graphics2D#fillPolygon(int[], int[], int)} (int[], int[], int)}
     */
    public abstract void fillPolygon(Scratch scratch, Paint fillPaint, int[] xPoints, int[] yPoints);

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
            currentPoint = Geometry.line(lastPoint, e.getPoint(), getToolAttributes().getConstrainedAngle());
            points.add(currentPoint);
        } else {
            currentPoint = imageLocation;
            points.add(currentPoint);
        }

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        getScratch().clear();
        strokePolyline(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), xs, ys);
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

        if (getToolAttributes().getFillPaint().isPresent()) {
            fillPolygon(getScratch(), getToolAttributes().getFillPaint().get(), xs, ys);
        }

        strokePolygon(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    private void commitPolyline() {
        getScratch().clear();

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        points.clear();
        currentPoint = null;

        strokePolyline(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), xs, ys);
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
