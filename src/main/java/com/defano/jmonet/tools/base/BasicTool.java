package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.cursors.CursorManager;
import com.google.inject.Inject;

import java.awt.*;

/**
 * A base tool class providing configuration-specific attributes (like stroke and fill), activation state management
 * (which canvas is the tool active on, if any) and a reference to the tool's delegate.
 *
 * @param <DelegateType> The object type of the delegate class in use by this tool.
 */
public class BasicTool<DelegateType> implements Tool, SurfaceInteractionObserver {

    private final PaintToolType toolType;
    private PaintCanvas canvas;
    private DelegateType delegate;

    @Inject private ToolAttributes toolAttributes;
    @Inject private CursorManager cursorManager;

    public BasicTool(PaintToolType toolType) {
        this.toolType = toolType;
    }

    /** {@inheritDoc} */
    @Override
    public void activate(PaintCanvas canvas) {
        deactivate();
        this.canvas = canvas;
        this.canvas.addSurfaceInteractionObserver(this);

        setToolCursor(getDefaultCursor());
    }

    /** {@inheritDoc} */
    @Override
    public void deactivate() {
        if (canvas != null) {
            canvas.removeSurfaceInteractionObserver(this);
        }

        canvas = null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive() {
        return canvas != null;
    }

    /** {@inheritDoc} */
    @Override
    public Cursor getToolCursor() {
        return cursorManager == null ? null : cursorManager.getToolCursor();
    }

    /** {@inheritDoc} */
    @Override
    public void setToolCursor(Cursor toolCursor) {
        if (cursorManager != null) {
            cursorManager.setToolCursor(toolCursor, canvas);
        }
    }

    /** {@inheritDoc} */
    @Override
    public PaintCanvas getCanvas() {
        if (canvas == null) {
            throw new IllegalStateException("Tool is not active on a canvas. Please call activate() first or use the makeActiveOnCanvas() builder option.");
        }

        return canvas;
    }

    /** {@inheritDoc} */
    @Override
    public Scratch getScratch() {
        Scratch scratch = getCanvas().getScratch();

        Interpolation antialiasing = getAttributes().getAntiAliasing();
        scratch.getAddScratchGraphics(this, null).setAntialiasingMode(antialiasing);
        scratch.getRemoveScratchGraphics(this, null).setAntialiasingMode(antialiasing);

        return scratch;
    }

    /** {@inheritDoc} */
    @Override
    public PaintToolType getPaintToolType() {
        return toolType;
    }

    /** {@inheritDoc} */
    @Override
    public ToolAttributes getAttributes() {
        return toolAttributes;
    }

    @SuppressWarnings("WeakerAccess")
    public DelegateType getDelegate() {
        if (delegate == null) {
            throw new IllegalStateException("Bug! Must invoke setDelegate() before activating the tool.");
        }

        return delegate;
    }

    public void setDelegate(DelegateType delegate) {
        this.delegate = delegate;
    }
}
