package com.defano.jmonet.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Line2D;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

public class PencilToolTest extends MockitoToolTest<PencilTool> {

    @BeforeEach
    public void setUp() {
        initialize(new PencilTool());
    }

    @Test
    public void testAddPointInDrawMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);
        final Point thisPoint = new Point(10,10);

        // 0-alpha puts pencil in draw mode
        Mockito.when(mockCanvasImage.getRGB(initialPoint.x, initialPoint.y)).thenReturn(new Color(0, 0, 0, 0).getRGB());

        // Run the test
        uut.addPoint(mockScratch, stroke, fillPaint, initialPoint, thisPoint);

        // Verify the results
        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockAddScratchGraphics).draw(argThat(matchesShape(new Line2D.Float(initialPoint, thisPoint))));
        Mockito.verifyZeroInteractions(mockRemoveScratchGraphics);
    }


    @Test
    public void testStartPathInDrawMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);

        // 0-alpha puts pencil in draw mode
        Mockito.when(mockCanvasImage.getRGB(initialPoint.x, initialPoint.y)).thenReturn(new Color(0, 0, 0, 0).getRGB());

        // Run the test
        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);

        // Verify the results
        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockAddScratchGraphics).draw(argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))));
        Mockito.verifyZeroInteractions(mockRemoveScratchGraphics);
    }

    @Test
    public void testStartPathInEraseMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);

        // 255-alpha puts pencil in erase mode
        Mockito.when(mockCanvasImage.getRGB(initialPoint.x, initialPoint.y)).thenReturn(new Color(0, 0, 0, 255).getRGB());

        // Run the test
        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);

        // Verify the results
        Mockito.verify(mockScratch).erase(eq(uut), argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))), eq(new BasicStroke(1)));
        Mockito.verifyZeroInteractions(mockAddScratchGraphics);
    }
}
