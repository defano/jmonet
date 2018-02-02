package com.defano.jmonet.tools;

import com.defano.jmonet.algo.BoundaryFunction;
import com.defano.jmonet.algo.FillFunction;
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
    private BoundaryFunction boundaryFunction = new DefaultBoundaryFunction();
    private FillFunction fillFunction = new DefaultFillFunction();

    public FillTool() {
        super(PaintToolType.FILL);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {

        // Nothing to do if no fill paint is specified
        if (getFillPaint().isPresent()) {
            getCanvas().clearScratch();

            FloodFill.floodFill(
                    getCanvas(),
                    getFillPaint().get(),
                    imageLocation.x,
                    imageLocation.y,
                    fillFunction,
                    boundaryFunction);

            getCanvas().commit();
            getCanvas().invalidateCanvas();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        setToolCursor(getFillCursor());
    }

    public Cursor getFillCursor() {
        return fillCursor;
    }

    public void setFillCursor(Cursor fillCursor) {
        this.fillCursor = fillCursor;
        setToolCursor(fillCursor);
    }

    public BoundaryFunction getBoundaryFunction() {
        return boundaryFunction;
    }

    public void setBoundaryFunction(BoundaryFunction boundaryFunction) {
        this.boundaryFunction = boundaryFunction;

        if (boundaryFunction == null) {
            this.boundaryFunction = new DefaultBoundaryFunction();
        }
    }

    public FillFunction getFillFunction() {
        return fillFunction;
    }

    public void setFillFunction(FillFunction fillFunction) {
        this.fillFunction = fillFunction;

        if (fillFunction == null) {
            this.fillFunction = new DefaultFillFunction();
        }
    }

    /**
     * Default boundary function; fills all fully-transparent pixels.
     */
    private class DefaultBoundaryFunction implements BoundaryFunction {
        @Override
        public boolean shouldFillPixel(BufferedImage canvas, BufferedImage scratch, Point point) {
            Color canvasPixel = new Color(canvas.getRGB(point.x, point.y), true);
            Color scratchPixel = new Color(scratch.getRGB(point.x, point.y), true);
            return canvasPixel.getAlpha() == 0 && scratchPixel.getAlpha() == 0;
        }
    }

    /**
     * Default fill function; fills pixels with solid color or texture.
     */
    private class DefaultFillFunction implements FillFunction {
        @Override
        public void fill(BufferedImage image, Point p, Paint fillPaint) {
            int rgb = getFillPixel(p.x, p.y, fillPaint);
            image.setRGB(p.x, p.y, rgb);
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
    }
}
