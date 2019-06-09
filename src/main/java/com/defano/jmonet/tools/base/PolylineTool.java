package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.MathUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A tool that draws shapes defined by a series of interconnected lines, where the end of the current line denotes the
 * starting point of the next line. Double-clicking completes the sequence, allowing for the shape to be closed and
 * filled.
 *
 * See {@link com.defano.jmonet.tools.CurveTool} and {@link com.defano.jmonet.tools.PolygonTool} as examples.
 */
public class PolylineTool extends BasicTool<PolylineToolDelegate> implements SurfaceInteractionObserver {

    private final List<Point> points = new ArrayList<>();
    private Point currentPoint = null;

    public PolylineTool(PaintToolType toolType) {
        super(toolType);
    }

    /** {@inheritDoc} */
    @Override
    public Cursor getDefaultCursor() {
        return new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point canvasLoc) {
        setToolCursor(getToolCursor());

        // Nothing to do if initial point is not yet established
        if (points.isEmpty()) {
            return;
        }

        if (e.isShiftDown()) {
            Point lastPoint = points.get(points.size() - 1);
            currentPoint = MathUtils.line(lastPoint, canvasLoc, getAttributes().getConstrainedAngle());
            points.add(currentPoint);
        } else {
            currentPoint = canvasLoc;
            points.add(currentPoint);
        }

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        getScratch().clear();
        getDelegate().strokePolyline(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), xs, ys);
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

        getAttributes().getFillPaint().ifPresent(fillPaint ->
                getDelegate().fillPolygon(getScratch(), fillPaint, xs, ys));

        getDelegate().strokePolygon(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    private void commitPolyline() {
        getScratch().clear();

        int[] xs = points.stream().mapToInt(i -> i.x).toArray();
        int[] ys = points.stream().mapToInt(i -> i.y).toArray();

        points.clear();
        currentPoint = null;

        getDelegate().strokePolyline(getScratch(), getAttributes().getStroke(), getAttributes().getStrokePaint(), xs, ys);
        getCanvas().commit();
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        // Ignore escape unless at least one point has been defined
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !points.isEmpty()) {
            points.add(currentPoint);
            commitPolyline();
        }
    }

}
