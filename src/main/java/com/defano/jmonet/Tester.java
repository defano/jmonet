package com.defano.jmonet;

import com.defano.jmonet.canvas.JMonetCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.builder.PaintTool;
import com.defano.jmonet.tools.builder.PaintToolBuilder;
import com.defano.jmonet.tools.builder.StrokeBuilder;

import javax.swing.*;
import java.awt.*;

public class Tester {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Dimension size = new Dimension(640, 480);

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setSize(size);

            // Create and show Swing frame
            JFrame frame = new JFrame("My Pretty Picture");
            frame.setPreferredSize(size);
            frame.pack();
            frame.setVisible(true);
            frame.setSize(size);

            // Create a JMonet canvas and add it to the window
            JMonetCanvas myCanvas = new JMonetCanvas();
            myCanvas.setSize(new Dimension(8000, 6000));
            scrollPane.setViewportView(myCanvas);

            frame.getContentPane().add(scrollPane);

            PaintToolBuilder.create(PaintToolType.PAINTBRUSH)
                    .withStroke(StrokeBuilder.withShape().ofCircle(8).build())
                    .withStrokePaint(Color.RED)
                    .makeActiveOnCanvas(myCanvas)
                    .build();
        });
    }

}
