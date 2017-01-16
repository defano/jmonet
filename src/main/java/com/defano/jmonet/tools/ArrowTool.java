package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;

import java.awt.*;

public class ArrowTool extends PaintTool {

    public ArrowTool() {
        super(PaintToolType.ARROW);
        setToolCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
