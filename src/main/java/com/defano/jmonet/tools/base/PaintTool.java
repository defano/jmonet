package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.model.Provider;
import com.defano.jmonet.canvas.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.CanvasCommitObserver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public abstract class PaintTool implements SurfaceInteractionObserver, CanvasCommitObserver {

    private PaintCanvas canvas;
    private final PaintToolType type;
    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

    private Cursor toolCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

    private Provider<Stroke> strokeProvider = new Provider<>(new BasicStroke(2));
    private Provider<Paint> strokePaintProvider = new Provider<>(Color.BLACK);
    private Provider<Paint> fillPaintProvider = new Provider<>(null);
    private Provider<Integer> shapeSidesProvider = new Provider<>(5);
    private Provider<Font> fontProvider = new Provider<>(new Font("Courier", Font.PLAIN, 14));

    public PaintTool(PaintToolType type) {
        this.type = type;
    }

    public void activate (PaintCanvas canvas) {
        this.canvas = canvas;
        this.canvas.addSurfaceInteractionObserver(this);
        this.canvas.setCursor(toolCursor);
    }

    public void deactivate() {
        if (canvas != null) {
            canvas.removeSurfaceInteractionObserver(this);
            canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void onCommit(PaintCanvas canvas, BufferedImage committedElement, BufferedImage canvasImage) {
        // Nothing to do
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public void setComposite(AlphaComposite composite) {
        this.composite = composite;
    }

    public PaintToolType getToolType() {
        return this.type;
    }

    protected PaintCanvas getCanvas() {
        return canvas;
    }

    public void setStrokePaintProvider(Provider<Paint> strokePaintProvider) {
        if (strokePaintProvider != null) {
            this.strokePaintProvider = strokePaintProvider;
        }
    }

    public void setStrokeProvider(Provider<Stroke> strokeProvider) {
        if (strokeProvider != null) {
            this.strokeProvider = strokeProvider;
        }
    }

    public void setShapeSidesProvider(Provider<Integer> shapeSidesProvider) {
        if (shapeSidesProvider != null) {
            this.shapeSidesProvider = shapeSidesProvider;
        }
    }

    public void setFontProvider(Provider<Font> fontProvider) {
        if (fontProvider != null) {
            this.fontProvider = fontProvider;
        }
    }

    public void setFillPaintProvider(Provider<Paint> fillPaintProvider) {
        if (fillPaintProvider != null) {
            this.fillPaintProvider = fillPaintProvider;
        }
    }

    public Stroke getStroke() {
        return strokeProvider.get();
    }

    public Paint getFillPaint() {
        return fillPaintProvider.get();
    }

    public Font getFont() {
        return fontProvider.get();
    }

    public int getShapeSides() {
        return shapeSidesProvider.get();
    }

    public Paint getStrokePaint() {
        return strokePaintProvider.get();
    }

    public Provider<Paint> getFillPaintProvider() {
        return fillPaintProvider;
    }

    public Provider<Stroke> getStrokeProvider() {
        return strokeProvider;
    }

    public Provider<Paint> getStrokePaintProvider() {
        return strokePaintProvider;
    }

    public Provider<Integer> getShapeSidesProvider() {
        return shapeSidesProvider;
    }

    public Provider<Font> getFontProvider() {
        return fontProvider;
    }

    public Cursor getToolCursor() {
        return toolCursor;
    }

    public void setToolCursor(Cursor toolCursor) {
        this.toolCursor = toolCursor;

        if (this.canvas != null) {
            this.canvas.setCursor(toolCursor);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void mousePressed(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void mouseReleased(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void mouseEntered(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void mouseExited(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void mouseDragged(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void mouseMoved(MouseEvent e, Point imageLocation) {
        // Nothing to do
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing to do
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Nothing to do
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Nothing to do
    }
}
