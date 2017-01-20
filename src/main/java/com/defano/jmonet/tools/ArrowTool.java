package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;

import java.awt.*;

/**
 * A no-op tool; does not modify the canvas in any way.
 */
public class ArrowTool extends PaintTool {

    private Cursor arrowCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    public ArrowTool() {
        super(PaintToolType.ARROW);
        setToolCursor(getArrowCursor());
    }

    public Cursor getArrowCursor() {
        return arrowCursor;
    }

    public void setArrowCursor(Cursor arrowCursor) {
        this.arrowCursor = arrowCursor;
    }

}
