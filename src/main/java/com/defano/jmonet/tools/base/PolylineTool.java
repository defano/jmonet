package com.defano.jmonet.tools.base;

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
    private PolylineToolDelegate polylineToolDelegate;

    public PolylineTool(PaintToolType toolType) {
        super(toolType);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        if (polylineToolDelegate == null) {
            throw new IllegalStateException("Polyline tool delegate not set.");
        }

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
        polylineToolDelegate.strokePolyline(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), xs, ys);
        getCanvas().repaint();

        points.remove(points.size() - 1);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        if (polylineToolDelegate == null) {
            throw new IllegalStateException("Polyline tool delegate not set.");
        }

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
            polylineToolDelegate.fillPolygon(getScratch(), getToolAttributes().getFillPaint().get(), xs, ys);
        }

        polylineToolDelegate.strokePolygon(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    private void commitPolyline() {
        getScratch().clear();

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        points.clear();
        currentPoint = null;

        polylineToolDelegate.strokePolyline(getScratch(), getToolAttributes().getStroke(), getToolAttributes().getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        if (polylineToolDelegate == null) {
            throw new IllegalStateException("Polyline tool delegate not set.");
        }

        // Ignore escape unless at least one point has been defined
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && points.size() > 0) {
            points.add(currentPoint);
            commitPolyline();
        }
    }

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return this;
    }

    protected PolylineToolDelegate getPolylineToolDelegate() {
        return polylineToolDelegate;
    }

    protected void setPolylineToolDelegate(PolylineToolDelegate polylineToolDelegate) {
        this.polylineToolDelegate = polylineToolDelegate;
    }
}
