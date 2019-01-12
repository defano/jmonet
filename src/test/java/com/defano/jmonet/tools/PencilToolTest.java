package com.defano.jmonet.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Line2D;

public class PencilToolTest extends MockitoToolTest<PencilTool> {

    @BeforeEach
    public void setUp() {
        initialize(new PencilTool());
    }

    @Test
    public void testStartPathInDrawMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);

        //
        Mockito.when(mockCanvasImage.getRGB(initialPoint.x, initialPoint.y)).thenReturn(new Color(0, 0, 0, 0).getRGB());

        // Run the test
        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);

        // Verify the results
        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockAddScratchGraphics).draw(Mockito.argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))));
        Mockito.verifyZeroInteractions(mockRemoveScratchGraphics);
    }

    @Test
    public void testStartPathInEraseMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);

        //
        Mockito.when(mockCanvasImage.getRGB(initialPoint.x, initialPoint.y)).thenReturn(new Color(0, 0, 0, 255).getRGB());

        // Run the test
        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);

        // Verify the results
        Mockito.verify(mockRemoveScratchGraphics).setStroke(new BasicStroke(1));
//        Mockito.verify(mockRemoveScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockRemoveScratchGraphics).draw(Mockito.argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))));
        Mockito.verifyZeroInteractions(mockAddScratchGraphics);
    }
}
