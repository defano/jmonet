package com.defano.jmonet.tools;

import com.defano.jmonet.tools.base.MockitoToolTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

public class RectangleToolTest extends MockitoToolTest<RectangleTool> {

    @BeforeEach
    public void setUp() {
        initialize(new RectangleTool());
    }

    @Test
    public void testDefaultCursor() {
        Mockito.verify(mockCursorManager).setToolCursor(argThat(matchesCursor(new Cursor(Cursor.CROSSHAIR_CURSOR))), eq(mockCanvas));
    }

    @Test
    public void testStrokeBounds() {
        Rectangle bounds = new Rectangle(1, 2, 3, 4);
        Paint fill = Color.black;
        Stroke stroke = new BasicStroke(1);

        uut.strokeBounds(mockScratch, stroke, fill, bounds, false);

        Mockito.verify(mockScratch).getAddScratchGraphics(eq(uut), eq(stroke), argThat(matchesShape(bounds)));
        Mockito.verify(mockAddScratchGraphics).setStroke(stroke);
        Mockito.verify(mockAddScratchGraphics).setPaint(fill);
        Mockito.verify(mockAddScratchGraphics).draw(bounds);
    }

    @Test
    public void testFillBounds() {
        Rectangle bounds = new Rectangle(1, 2, 3, 4);
        Paint fill = Color.black;

        uut.fillBounds(mockScratch, fill, bounds, false);

        Mockito.verify(mockScratch).getAddScratchGraphics(eq(uut), argThat(matchesShape(bounds)));
        Mockito.verify(mockAddScratchGraphics).setPaint(fill);
        Mockito.verify(mockAddScratchGraphics).fill(bounds);
    }
}
