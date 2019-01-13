package com.defano.jmonet.tools;

import com.defano.jmonet.tools.base.MockitoToolTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Line2D;

import static org.mockito.Matchers.argThat;

public class AirbrushToolTest extends MockitoToolTest<AirbrushTool> {

    @BeforeEach
    public void setUp() {
        initialize(new AirbrushTool());
    }

    @Test
    public void testAddPoint() {
        // Setup
        final Stroke stroke = new BasicStroke(10);
        final Paint fillPaint = Color.BLUE;
        final Point lastPoint = new Point(10, 10);
        final Point thisPoint = new Point(20, 20);

        Mockito.when(mockToolAttributes.getIntensity()).thenReturn(1.0);

        // Run the test
        uut.addPoint(mockScratch, stroke, fillPaint, lastPoint, thisPoint);

        // Verify the results
        Mockito.verify(mockAddScratchGraphics).setStroke(stroke);
        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockAddScratchGraphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        Mockito.verify(mockAddScratchGraphics).draw(argThat(matchesShape(new Line2D.Float(lastPoint, thisPoint))));
    }

}
