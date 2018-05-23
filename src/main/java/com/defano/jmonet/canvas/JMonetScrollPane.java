package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.surface.ScrollableDelegate;

import javax.swing.*;

/**
 * A {@link JScrollPane} containing a {@link JMonetCanvas} which acts as a canvas scroll delegate (automatically
 * repositions the scroll when using the {@link com.defano.jmonet.tools.MagnifierTool}.
 */
public class JMonetScrollPane extends JScrollPane {

    private final JMonetCanvas canvas;

    public JMonetScrollPane(JMonetCanvas canvas) {
        this.canvas = canvas;
//        this.canvas.setScrollDelegate(this);

        setViewportView(canvas);
        getViewport().setOpaque(false);
        setBorder(null);
        setOpaque(false);
    }

//    @Override
//    public JScrollPane getScrollPane() {
//        return this;
//    }
//
    public JMonetCanvas getCanvas() {
        return canvas;
    }
}
