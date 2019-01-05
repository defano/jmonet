package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BasicTool;

import java.awt.*;

/**
 * A no-op tool; does not modify the canvas in any way.
 */
public class ArrowTool extends BasicTool {

    public ArrowTool() {
        super(PaintToolType.ARROW);
        setToolCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public SurfaceInteractionObserver getSurfaceInteractionObserver() {
        return null;
    }
}
