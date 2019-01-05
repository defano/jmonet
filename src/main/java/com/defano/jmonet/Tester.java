package com.defano.jmonet;

import com.defano.jmonet.canvas.JMonetCanvas;
import com.defano.jmonet.tools.PencilTool;
import com.defano.jmonet.tools.base.Tool;

import javax.swing.*;
import java.awt.*;

public class Tester {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {

            // Create and show Swing frame
            JFrame frame = new JFrame("My Pretty Picture");
            frame.setPreferredSize(new Dimension(640, 480));
            frame.setLayout(new FlowLayout());
            frame.pack();
            frame.setVisible(true);

            // Create a JMonet canvas and add it to the window
            JMonetCanvas myCanvas = new JMonetCanvas(new Dimension(200, 200));

            JScrollPane scroll = new JScrollPane();
            scroll.setViewportView(myCanvas);

            myCanvas.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            myCanvas.setScale(2);

            frame.getContentPane().add(myCanvas);

            Tool t = new PencilTool();
            t.activate(myCanvas);

//            DefaultToolAttributes activeTool = PaintToolBuilder.create(PaintToolType.PAINTBRUSH)
//                    .withStroke(StrokeBuilder.withShape().ofCircle(8).build())
//                    .withFillPaint(Color.BLUE)
//                    .withStrokePaint(Color.RED)
//                    .withEraseColor(null)
//                    .makeActiveOnCanvas(myCanvas)
//                    .build();

        });
    }

}
