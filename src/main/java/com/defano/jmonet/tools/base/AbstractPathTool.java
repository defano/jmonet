package com.defano.jmonet.tools.base;

import com.defano.jmonet.model.PaintToolType;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class AbstractPathTool extends PaintTool {

    private Cursor pathCursor = new Cursor(Cursor.MOVE_CURSOR);

    public abstract void startPath(Graphics2D g, Stroke stroke, Paint paint, Point initialPoint);
    public abstract void addPoint(Graphics2D g, Stroke stroke, Paint paint, Point point);

    public AbstractPathTool(PaintToolType type) {
        super(type);
        setToolCursor(pathCursor);
    }

    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        startPath(g2d, getStroke(), getFillPaint(), imageLocation);
        g2d.dispose();

        getCanvas().invalidateCanvas();
    }

    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        addPoint(g2d, getStroke(), getFillPaint(), imageLocation);
        g2d.dispose();

        getCanvas().invalidateCanvas();
    }

    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        getCanvas().commit(getComposite());
    }

    public Cursor getPathCursor() {
        return pathCursor;
    }

    public void setPathCursor(Cursor pathCursor) {
        this.pathCursor = pathCursor;
        setToolCursor(pathCursor);
    }
}
