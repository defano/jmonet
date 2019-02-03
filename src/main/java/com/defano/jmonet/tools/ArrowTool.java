package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.BasicTool;

import java.awt.*;

/**
 * A no-op tool; does not modify the canvas in any way.
 */
public class ArrowTool extends BasicTool {

    /**
     * Tool must be constructed via {@link com.defano.jmonet.tools.builder.PaintToolBuilder} to handle dependency
     * injection.
     */
    ArrowTool() {
        super(PaintToolType.ARROW);
    }

    @Override
    public Cursor getDefaultCursor() {
        return Cursor.getDefaultCursor();
    }
}
