package com.defano.jmonet.tools.base;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.util.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Mouse and keyboard handler for tools defining a line drawn between two points. When the shift key is held down, the
 * defined line is constrained to the nearest 15 degree angle.
 */
public abstract class AbstractLineTool extends PaintTool {

    private int snapToDegrees = 15;
    private Point initialPoint;
    private Cursor lineCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

    public AbstractLineTool(PaintToolType type) {
        super(type);
        setToolCursor(lineCursor);
    }

    protected abstract void drawLine(Graphics2D g, Stroke stroke, Paint paint, int x1, int y1, int x2, int y2);

    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        initialPoint = imageLocation;
    }

    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        getCanvas().clearScratch();

        Point currentLoc = imageLocation;

        if (e.isShiftDown()) {
            currentLoc = Geometry.snapLineToNearestAngle(initialPoint, currentLoc, snapToDegrees);
        }

        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        drawLine(g2d, getStroke(), getStrokePaint(), initialPoint.x, initialPoint.y, currentLoc.x, currentLoc.y);
        g2d.dispose();

        getCanvas().invalidateCanvas();
    }

    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit();
    }

    public int getSnapToDegrees() {
        return snapToDegrees;
    }

    public void setSnapToDegrees(int snapToDegrees) {
        this.snapToDegrees = snapToDegrees;
    }

    public Cursor getLineCursor() {
        return lineCursor;
    }

    public void setLineCursor(Cursor lineCursor) {
        this.lineCursor = lineCursor;
        setToolCursor(lineCursor);
    }
}
