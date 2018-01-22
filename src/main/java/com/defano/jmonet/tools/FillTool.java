package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;
import com.defano.jmonet.algo.FloodFill;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Tool that performs a flood-fill of all transparent pixels.
 */
public class FillTool extends PaintTool {

    private Cursor fillCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

    public FillTool() {
        super(PaintToolType.FILL);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        BufferedImage canvasImage = getCanvas().getCanvasImage();
        Rectangle canvasBounds = getCanvas().getBounds();

        // Nothing to do if no fill is specified
        if (!getFillPaint().isPresent()) {
            return;
        }

        getCanvas().clearScratch();

        FloodFill.floodFill(imageLocation.x, imageLocation.y, canvasBounds, p -> {
            int rgb = getFillPixel(p.x, p.y, getFillPaint().get());
            getCanvas().getScratchImage().setRGB(p.x, p.y, rgb);
        }, (Point point) -> {
            Color canvasPixel = new Color(canvasImage.getRGB(point.x, point.y), true);
            Color scratchPixel = new Color(getCanvas().getScratchImage().getRGB(point.x, point.y), true);
            return canvasPixel.getAlpha() == 0 && scratchPixel.getAlpha() == 0;
        });

        getCanvas().commit();
        getCanvas().invalidateCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getFillCursor());
    }

    private int getFillPixel(int x, int y, Paint paint) {

        if (paint instanceof Color) {
            return ((Color) paint).getRGB();
        } else if (paint instanceof TexturePaint) {
            BufferedImage texture = ((TexturePaint) paint).getImage();
            return texture.getRGB(x % texture.getWidth(), y % texture.getHeight());
        }

        throw new IllegalArgumentException("Don't know how to fill with paint " + paint);
    }

    public Cursor getFillCursor() {
        return fillCursor;
    }

    public void setFillCursor(Cursor fillCursor) {
        this.fillCursor = fillCursor;
        setToolCursor(fillCursor);
    }
}
