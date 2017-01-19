package com.defano.jmonet.tools;

import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.base.PaintTool;

import java.awt.*;

public class ArrowTool extends PaintTool {

    private Cursor arrorCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    public ArrowTool() {
        super(PaintToolType.ARROW);
        setToolCursor(getArrorCursor());
    }

    public Cursor getArrorCursor() {
        return arrorCursor;
    }

    public void setArrorCursor(Cursor arrorCursor) {
        this.arrorCursor = arrorCursor;
    }

}
