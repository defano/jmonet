package com.defano.jmonet.tools;

import com.defano.jmonet.tools.attributes.MarkPredicate;
import com.defano.jmonet.tools.base.MockitoToolTest;
import com.defano.jmonet.tools.cursors.CursorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
    public void testDefaultCursor() {
        Mockito.verify(mockCursorManager).setToolCursor(argThat(matchesCursor(CursorFactory.makePencilCursor())), eq(mockCanvas));
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
        Mockito.verify(mockScratch).getAddScratchGraphics(eq(uut), eq(new BasicStroke(1)), argThat(matchesShape(new Line2D.Float(initialPoint, thisPoint))));
        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockAddScratchGraphics).draw(argThat(matchesShape(new Line2D.Float(initialPoint, thisPoint))));
    }


    @Test
    public void testStartPathInDrawMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);

        // Assume no pixels are marked
        Mockito.when(mockToolAttributes.getMarkPredicate()).thenReturn(new MarkPredicate() {
            @Override
            public boolean isMarked(Color pixel, Color eraseColor) {
                return false;
            }
        });

        // Run the test
        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);

        // Verify the results
        Mockito.verify(mockScratch).getAddScratchGraphics(eq(uut), eq(new BasicStroke(1)), argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))));
        Mockito.verify(mockAddScratchGraphics).setStroke(new BasicStroke(1));
        Mockito.verify(mockAddScratchGraphics).setPaint(fillPaint);
        Mockito.verify(mockAddScratchGraphics).draw(argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))));
    }

    @Test
    public void testStartPathInEraseMode() {
        // Setup
        final Stroke stroke = new BasicStroke(1);
        final Paint fillPaint = Color.blue;
        final Point initialPoint = new Point(0,0);

        // Assume all pixels are marked
        Mockito.when(mockToolAttributes.getMarkPredicate()).thenReturn(new MarkPredicate() {
            @Override
            public boolean isMarked(Color pixel, Color eraseColor) {
                return true;
            }
        });

        // Run the test
        uut.startPath(mockScratch, stroke, fillPaint, initialPoint);

        // Verify the results
        Mockito.verify(mockScratch).erase(eq(uut), argThat(matchesShape(new Line2D.Float(initialPoint, initialPoint))), eq(new BasicStroke(1)));
        Mockito.verifyZeroInteractions(mockAddScratchGraphics);
    }
}
