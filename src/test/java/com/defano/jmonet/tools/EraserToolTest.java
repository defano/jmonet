package com.defano.jmonet.tools;

import com.defano.jmonet.tools.base.MockitoToolTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Line2D;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

public class EraserToolTest extends MockitoToolTest<EraserTool> {

    @BeforeEach
    public void setUp() {
        initialize(new EraserTool());
    }

    @Test
    public void testThatInitialPathIsErased() {
        Stroke stroke = new BasicStroke(1);
        Point initialPoint = new Point(10, 10);

        uut.startPath(mockScratch, stroke, null, initialPoint);
        Mockito.verify(mockScratch).erase(eq(uut), argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))), eq(stroke));
    }

    @Test
    public void testThatSubsequentPointsOnPathAreErased() {
        Stroke stroke = new BasicStroke(1);
        Point initialPoint = new Point(10, 10);
        Point thisPoint = new Point(20, 20);

        uut.addPoint(mockScratch, stroke, null, initialPoint, thisPoint);

        Mockito.verify(mockScratch).erase(eq(uut), argThat(matchesShape(new Line2D.Float(initialPoint, thisPoint))), eq(stroke));
    }

    @Test
    public void testThatCompletePathDoesNothing() {
        uut.completePath(mockScratch, null, null, null);
        Mockito.verifyZeroInteractions(mockScratch);
    }
}
