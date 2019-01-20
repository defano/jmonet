package com.defano.jmonet.tools;

import com.defano.jmonet.tools.base.MockitoToolTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Line2D;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

public class LineToolTest extends MockitoToolTest<LineTool> {

    @Mock private Stroke mockStroke;
    @Mock private Paint mockPaint;

    @BeforeEach
    public void setup() {
        initialize(new LineTool());
    }

    @Test
    public void testDrawLine() {
        Line2D l = new Line2D.Float(10, 20, 30, 40);

        uut.drawLine(mockScratch, mockStroke, mockPaint, (int)((Line2D.Float) l).x1, (int)((Line2D.Float) l).y1, (int)((Line2D.Float) l).x2, (int)((Line2D.Float) l).y2);

        Mockito.verify(mockScratch).getAddScratchGraphics(eq(uut), eq(mockStroke), argThat(matchesShape(l)));
        Mockito.verify(mockAddScratchGraphics).setPaint(mockPaint);
        Mockito.verify(mockAddScratchGraphics).setStroke(mockStroke);
        Mockito.verify(mockAddScratchGraphics).draw(argThat(matchesShape(l)));
    }

}
