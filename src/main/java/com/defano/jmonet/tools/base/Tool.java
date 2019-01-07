package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.ToolAttributes;
import io.reactivex.Observable;

import java.awt.*;

public interface Tool {
    /**
     * Activates this tool on a given canvas.
     * <p>
     * A paint tool does not "paint" on the canvas until it is activated. Typically, only one tool is active on
     * a canvas at any given time, but there is no technical limitation preventing multiple tools from being active
     * at once. A single tool may not be active on multiple canvasses at the same time.
     * <p>
     * Use {@link #deactivate()} to stop this tool from painting on the canvas.
     *
     * @param canvas The paint canvas on which to activate the tool.
     */
    void activate(PaintCanvas canvas);

    /**
     * Deactivates the tool on the canvas. A deactivated tool no longer affects the canvas and all listeners / observers
     * are un-subscribed making the tool available for garbage collection.
     */
    void deactivate();

    /**
     * Gets the default mouse cursor used when painting with this tool. Note that specific tools may provide methods
     * for getting and setting auxiliary cursors that are active during tool-specific states, too.
     *
     * @return The default tool cursor.
     */
    Cursor getToolCursor();

    /**
     * Sets the default mouse cursor used when painting with this tool.
     *
     * @param toolCursor The default mouse cursor.
     */
    void setToolCursor(Cursor toolCursor);

    /**
     * Gets the canvas on which this tool is currently painting, or null, if not active.
     *
     * @return The canvas this tool is painting on, or null
     */
    PaintCanvas getCanvas();

    /**
     * Gets the scratch buffer of the canvas that this tool is presently activated on, applying this tool's anti-
     * aliasing mode to scratch buffer's graphics context.
     *
     * @return The active scratch buffer.
     */
    Scratch getScratch();

    /**
     * Gets the type of this tool.
     *
     * @return The tool type.
     */
    PaintToolType getToolType();

    /**
     * Gets the set of tool attributes bound to this tool (like paint color, fill mode, line size, etc.)
     *
     * @return The set of observable tool attributes.
     */
    ToolAttributes getToolAttributes();

    void applyRenderingHints(Graphics2D g2d);

}
