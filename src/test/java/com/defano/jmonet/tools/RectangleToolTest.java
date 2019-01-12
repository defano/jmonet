package com.defano.jmonet.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

public class RectangleToolTest extends MockitoToolTest {

    private RectangleTool rectangleToolUnderTest;

    @BeforeEach
    public void setUp() {
        rectangleToolUnderTest = new RectangleTool();
    }

    @Test
    public void testStrokeBounds() {
        Rectangle bounds = new Rectangle(1, 2, 3, 4);
        Paint fill = Color.black;
        Stroke stroke = new BasicStroke(1);

        rectangleToolUnderTest.strokeBounds(mockScratch, stroke, fill, bounds, false);

        Mockito.verify(mockAddScratchGraphics).setStroke(stroke);
        Mockito.verify(mockAddScratchGraphics).setPaint(fill);
        Mockito.verify(mockAddScratchGraphics).draw(bounds);
    }

    @Test
    public void testFillBounds() {
        Rectangle bounds = new Rectangle(1, 2, 3, 4);
        Paint fill = Color.black;

        rectangleToolUnderTest.fillBounds(mockScratch, fill, bounds, false);

        Mockito.verify(mockAddScratchGraphics).setPaint(fill);
        Mockito.verify(mockAddScratchGraphics).fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
