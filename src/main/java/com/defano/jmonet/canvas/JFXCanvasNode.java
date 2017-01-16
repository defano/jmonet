package com.defano.jmonet.canvas;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;

public class JFXCanvasNode extends SwingNode {

    private final BasicCanvas canvas;

    public JFXCanvasNode(BasicCanvas canvas) {
        this.canvas = canvas;
        Platform.runLater(() -> JFXCanvasNode.super.setContent(canvas));
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
