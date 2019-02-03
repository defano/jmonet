package com.defano.jmonet.tools.base;

import com.defano.jmonet.tools.util.Geometry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class PolylineToolTest extends BaseToolTest<PolylineTool> {

    @Mock
    private MouseEvent mockEvent;
    @Mock private Point mockPoint;
    @Mock private PolylineToolDelegate mockDelegate;
    @Mock private Stroke mockStroke;
    @Mock private Paint mockPaint;
    @Mock private Rectangle mockRegion;

    @BeforeEach
    protected void setup() {
        super.setup(new PolylineTool(null));
    }

    @Test
    void testThatCrosshairIsDefaultCursor() {
        assertTrue(new CursorMatcher(new Cursor(Cursor.CROSSHAIR_CURSOR)).matches(uut.getDefaultCursor()));
    }

    @Test
    void testThatMouseMovedUpdatesCursor() {
        Mockito.when(mockCursorManager.getToolCursor()).thenReturn(mockCursor);
        uut.activate(mockCanvas);
        uut.mouseMoved(mockEvent, mockPoint);
        Mockito.verify(mockCursorManager).setToolCursor(mockCursor, mockCanvas);
    }

    @Test
    void testThatMouseMoveBeforeMousePressDoesNothing() {
        uut.setDelegate(mockDelegate);
        uut.mouseMoved(mockEvent, mockPoint);

        Mockito.verifyZeroInteractions(mockScratch);
        Mockito.verifyZeroInteractions(mockDelegate);
        Mockito.verifyZeroInteractions(mockCanvas);
    }

    @Test
    void testThatLineIsDrawnBetweenInitialPointAndMouseLoc() {
        Point pressPoint = new Point(10, 10);
        Point movePoint = new Point(20, 20);

        Mockito.when(mockToolAttributes.getStroke()).thenReturn(mockStroke);
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockPaint);

        uut.activate(mockCanvas);
        uut.setDelegate(mockDelegate);
        uut.mousePressed(mockEvent, pressPoint);
        uut.mouseMoved(mockEvent, movePoint);

        Mockito.verify(mockScratch).clear();
        Mockito.verify(mockDelegate).strokePolyline(
                mockScratch,
                mockStroke,
                mockPaint,
                new int[] {pressPoint.x, movePoint.x},
                new int[] {pressPoint.y, movePoint.y});
        Mockito.verify(mockCanvas).repaint();
    }

    @Test
    void testThatLineIsDrawnConstrainedWhenShiftIsDown() {
        Point pressPoint = new Point(10, 10);
        Point movePoint = new Point(20, 20);

        Mockito.when(mockToolAttributes.getStroke()).thenReturn(mockStroke);
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockPaint);
        Mockito.when(mockEvent.isShiftDown()).thenReturn(true);

        uut.activate(mockCanvas);
        uut.setDelegate(mockDelegate);
        uut.mousePressed(mockEvent, pressPoint);
        uut.mouseMoved(mockEvent, movePoint);

        Point constrainedPoint = Geometry.line(pressPoint, movePoint, mockToolAttributes.getConstrainedAngle());

        Mockito.verify(mockScratch).clear();
        Mockito.verify(mockDelegate).strokePolyline(
                mockScratch,
                mockStroke,
                mockPaint,
                new int[] {pressPoint.x, constrainedPoint.x},
                new int[] {pressPoint.y, constrainedPoint.y});
        Mockito.verify(mockCanvas).repaint();
    }


}