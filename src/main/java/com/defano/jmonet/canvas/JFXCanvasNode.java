package com.defano.jmonet.canvas;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;

public class JFXCanvasNode extends SwingNode {

    private final BasicPaintCanvas canvas;

    public JFXCanvasNode(BasicPaintCanvas canvas) {
        this.canvas = canvas;
        Platform.runLater(() -> JFXCanvasNode.super.setContent(canvas));
    }

    public PaintCanvas getCanvas() {
        return canvas;
    }

}
