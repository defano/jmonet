package com.defano.jmonet.canvas;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;

/**
 * A trivial wrapper making a PaintCanvas available to JavaFX applications.
 */
public class JFXPaintCanvasNode extends SwingNode {

    private final AbstractPaintCanvas canvas;

    public JFXPaintCanvasNode(AbstractPaintCanvas canvas) {
        this.canvas = canvas;
        Platform.runLater(() -> JFXPaintCanvasNode.super.setContent(canvas));
    }

    public PaintCanvas getCanvas() {
        return canvas;
    }

}
