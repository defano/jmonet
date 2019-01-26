package com.defano.jmonet.tools.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundsToolTest extends BaseToolTest<BoundsTool> {

    @Mock private MouseEvent mockEvent;
    @Mock private Point mockPoint;
    @Mock private BoundsToolDelegate mockDelegate;
    @Mock private Stroke mockStroke;
    @Mock private Paint mockStrokePaint;
    @Mock private Paint mockFillPaint;

    @BeforeEach
    protected void setup() {
        super.setup(new BoundsTool(null));
    }

    @Test
    void testThatCrosshairIsDefaultCursor() {
        assertTrue(new CursorMatcher(new Cursor(Cursor.CROSSHAIR_CURSOR)).matches(uut.getDefaultCursor()));
    }

    @Test
    void testThatMousePressedSetsInitialBound() {
        uut.mousePressed(mockEvent, mockPoint);
        assertEquals(mockPoint, uut.getInitialPoint());
    }

    @Test
    void testThatMouseReleasedCommitsScratchToCanvas() {
        uut.activate(mockCanvas);
        uut.mouseReleased(mockEvent, mockPoint);

        Mockito.verify(mockCanvas).commit();
    }

    @Test
    void testThatDrawMultipleDoesNotClearScratch() {
        Mockito.when(mockToolAttributes.getFillPaint()).thenReturn(Optional.empty());
        Mockito.when(mockToolAttributes.isDrawMultiple()).thenReturn(true);

        uut.setDelegate(mockDelegate);
        uut.activate(mockCanvas);
        uut.mousePressed(mockEvent, mockPoint);
        uut.mouseDragged(mockEvent, mockPoint);

        Mockito.verify(mockScratch, Mockito.never()).clear();
    }

    @Test
    void thatMouseDraggedDrawsStrokedBounds() {
        Point startPoint = new Point(10, 10);
        Point endPoint = new Point(100, 100);

        Mockito.when(mockToolAttributes.getFillPaint()).thenReturn(Optional.empty());
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockStrokePaint);
        Mockito.when(mockToolAttributes.getStroke()).thenReturn(mockStroke);

        uut.setDelegate(mockDelegate);
        uut.activate(mockCanvas);
        uut.mousePressed(mockEvent, startPoint);
        uut.mouseDragged(mockEvent, endPoint);

        Mockito.verify(mockDelegate).strokeBounds(
                mockScratch,
                mockStroke,
                mockStrokePaint,
                new Rectangle(startPoint, new Dimension(endPoint.x - startPoint.x, endPoint.y - startPoint.y)),
                false);
    }

    @Test
    void thatMouseDraggedDrawsFilledBounds() {
        Point startPoint = new Point(10, 10);
        Point endPoint = new Point(100, 100);

        Mockito.when(mockToolAttributes.getFillPaint()).thenReturn(Optional.of(mockFillPaint));
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockStrokePaint);
        Mockito.when(mockToolAttributes.getStroke()).thenReturn(mockStroke);

        uut.setDelegate(mockDelegate);
        uut.activate(mockCanvas);
        uut.mousePressed(mockEvent, startPoint);
        uut.mouseDragged(mockEvent, endPoint);

        Mockito.verify(mockDelegate).fillBounds(
                mockScratch,
                mockFillPaint,
                new Rectangle(startPoint, new Dimension(endPoint.x - startPoint.x, endPoint.y - startPoint.y)),
                false
        );

        Mockito.verify(mockDelegate).strokeBounds(
                mockScratch,
                mockStroke,
                mockStrokePaint,
                new Rectangle(startPoint, new Dimension(endPoint.x - startPoint.x, endPoint.y - startPoint.y)),
                false
        );
    }

}