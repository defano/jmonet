package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.DefaultToolAttributes;
import com.defano.jmonet.tools.builder.ToolAttributes;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import javax.swing.*;
import java.awt.*;

public abstract class BasicTool implements Tool {

    private final PaintToolType toolType;

    private PaintCanvas canvas;
    private Cursor toolCursor;
    private Observable<Interpolation> antiAliasingObservable = BehaviorSubject.createDefault(Interpolation.BILINEAR);
    private ToolAttributes toolAttributes = new DefaultToolAttributes();

    public BasicTool(PaintToolType toolType) {
        this.toolType = toolType;
    }

    @Override
    public void activate(PaintCanvas canvas) {
        this.canvas = canvas;

        if (getSurfaceInteractionObserver() != null) {
            this.canvas.addSurfaceInteractionObserver(getSurfaceInteractionObserver());
        }

        SwingUtilities.invokeLater(() -> canvas.setCursor(getToolCursor()));
    }

    @Override
    public void deactivate() {
        if (canvas != null) {

            if (getSurfaceInteractionObserver() != null) {
                canvas.removeSurfaceInteractionObserver(getSurfaceInteractionObserver());
            }

            SwingUtilities.invokeLater(() -> canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)));
        }
    }

    @Override
    public Cursor getToolCursor() {
        return toolCursor;
    }

    @Override
    public void setToolCursor(Cursor toolCursor) {
        this.toolCursor = toolCursor;
        if (this.canvas != null) {
            SwingUtilities.invokeLater(() -> canvas.setCursor(toolCursor));
        }
    }

    @Override
    public PaintCanvas getCanvas() {
        return canvas;
    }

    public void erase(Scratch scratch, Shape shape, Stroke stroke) {
        Paint erasePaint = toolAttributes.getEraseColor();

        Graphics2D g = erasePaint == null ?
                scratch.getRemoveScratchGraphics(this, stroke, shape) :
                scratch.getAddScratchGraphics(this, stroke, shape);

        g.setStroke(stroke);
        g.setPaint(erasePaint == null ? getCanvas().getCanvasBackground() : erasePaint);
        g.draw(shape);
    }

    @Override
    public Scratch getScratch() {
        if (getCanvas() == null) {
            throw new IllegalStateException("Tool is not active on a canvas.");
        }

        Scratch scratch = getCanvas().getScratch();
        applyRenderingHints(scratch.getAddScratchGraphics(this, null));
        applyRenderingHints(scratch.getRemoveScratchGraphics(this, null));

        return scratch;
    }

    @Override
    public void applyRenderingHints(Graphics2D g2d) {
        switch (getInterpolation()) {
            case NONE:
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                break;
            case DEFAULT:
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
                break;
            case NEAREST_NEIGHBOR:
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            case BICUBIC:
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            case BILINEAR:
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
        }
    }

    public Observable<Interpolation> getAntiAliasingObservable() {
        return antiAliasingObservable;
    }

    public void setAntiAliasingObservable(Observable<Interpolation> antiAliasingObservable) {
        this.antiAliasingObservable = antiAliasingObservable;
    }

    @Override
    public Interpolation getInterpolation() {
        return antiAliasingObservable.blockingFirst();
    }

    @Override
    public PaintToolType getToolType() {
        return toolType;
    }

    @Override
    public ToolAttributes getToolAttributes() {
        return toolAttributes;
    }

    @Override
    public void setToolAttributes(ToolAttributes attributes) {
        this.toolAttributes = attributes;
    }
}
