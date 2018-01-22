package com.defano.jmonet;

import com.defano.jmonet.canvas.JMonetCanvas;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.brushes.BasicBrush;
import com.defano.jmonet.tools.builder.PaintToolBuilder;

import javax.swing.*;
import java.awt.*;

public class Test {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {

            // Create and show Swing frame
            JFrame frame = new JFrame("My Pretty Picture");
            frame.setPreferredSize(new Dimension(640, 480));
            frame.pack();
            frame.setVisible(true);

            // Create a JMonet canvas and add it to the window
            JMonetCanvas myCanvas = new JMonetCanvas();
            frame.getContentPane().add(myCanvas);

            PaintToolBuilder.create(PaintToolType.ROUND_RECTANGLE)
                    .withFillPaint(Color.RED)
                    .withStroke(BasicBrush.ROUND_8X8.stroke)
                    .makeActiveOnCanvas(myCanvas)
                    .build();

        });
    }


}
