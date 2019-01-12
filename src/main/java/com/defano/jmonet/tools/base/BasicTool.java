package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.DefaultToolAttributes;
import com.defano.jmonet.tools.builder.ToolAttributes;

import javax.swing.*;
import java.awt.*;

public abstract class BasicTool implements Tool {

    private final PaintToolType toolType;
    private final ToolAttributes toolAttributes = new DefaultToolAttributes();

    private PaintCanvas canvas;
    private Cursor toolCursor;

    public BasicTool(PaintToolType toolType) {
        this.toolType = toolType;
    }

    protected abstract SurfaceInteractionObserver getSurfaceInteractionObserver();

    /** {@inheritDoc} */
    @Override
    public void activate(PaintCanvas canvas) {
        deactivate();
        this.canvas = canvas;

        if (getSurfaceInteractionObserver() != null) {
            this.canvas.addSurfaceInteractionObserver(getSurfaceInteractionObserver());
        }

        SwingUtilities.invokeLater(() -> canvas.setCursor(getToolCursor()));
    }

    /** {@inheritDoc} */
    @Override
    public void deactivate() {
        if (canvas != null) {

            if (getSurfaceInteractionObserver() != null) {
                canvas.removeSurfaceInteractionObserver(getSurfaceInteractionObserver());
            }

            SwingUtilities.invokeLater(() -> canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)));
        }
    }

    /** {@inheritDoc} */
    @Override
    public Cursor getToolCursor() {
        return toolCursor;
    }

    /** {@inheritDoc} */
    @Override
    public void setToolCursor(Cursor toolCursor) {
        this.toolCursor = toolCursor;
        if (this.canvas != null) {
            SwingUtilities.invokeLater(() -> canvas.setCursor(toolCursor));
        }
    }

    /** {@inheritDoc} */
    @Override
    public PaintCanvas getCanvas() {
        return canvas;
    }

    public void erase(Scratch scratch, Shape shape, Stroke stroke) {
        Paint erasePaint = toolAttributes.getEraseColor();

        GraphicsContext g = erasePaint == null ?
                scratch.getRemoveScratchGraphics(this, stroke, shape) :
                scratch.getAddScratchGraphics(this, stroke, shape);

        g.setStroke(stroke);
        g.setPaint(erasePaint == null ? getCanvas().getCanvasBackground() : erasePaint);
        g.draw(shape);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc}
     * @param g*/
    @Override
    public void applyRenderingHints(GraphicsContext g) {
        switch (getToolAttributes().getAntiAliasing()) {
            case NONE:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                break;
            case DEFAULT:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
                break;
            case NEAREST_NEIGHBOR:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            case BICUBIC:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            case BILINEAR:
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
        }
    }

    /** {@inheritDoc} */
    @Override
    public PaintToolType getToolType() {
        return toolType;
    }

    /** {@inheritDoc} */
    @Override
    public ToolAttributes getToolAttributes() {
        return toolAttributes;
    }
}
