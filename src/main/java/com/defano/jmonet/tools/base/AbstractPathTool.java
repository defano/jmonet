package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.ChangeSet;
import com.defano.jmonet.model.PaintToolType;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Mouse and keyboard handler for tools that define a free-form path on the canvas by clicking and dragging.
 */
public abstract class AbstractPathTool extends PaintTool {

    protected abstract void startPath(Graphics2D g, Stroke stroke, Paint paint, Point initialPoint);
    protected abstract void addPoint(Graphics2D g, Stroke stroke, Paint paint, Point point);
    protected void completePath(Graphics2D g, Stroke stroke, Paint paint) {}

    public AbstractPathTool(PaintToolType type) {
        super(type);
        setToolCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getToolCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        startPath(g2d, getStroke(), getFillPaint(), imageLocation);
        g2d.dispose();

        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        addPoint(g2d, getStroke(), getFillPaint(), imageLocation);
        g2d.dispose();

        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        Graphics2D g2d = (Graphics2D) getCanvas().getScratchImage().getGraphics();
        completePath(g2d, getStroke(), getFillPaint());
        g2d.dispose();

        getCanvas().commit(new ChangeSet(getCanvas().getScratchImage(), getComposite()));
    }
}
