package com.defano.jmonet.tools.base;

import com.defano.jmonet.tools.util.MathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LinearToolTest extends BaseToolTest<LinearTool> {

    @Mock private MouseEvent mockEvent;
    @Mock private Point mockPoint;
    @Mock private LinearToolDelegate mockDelegate;
    @Mock private Stroke mockStroke;
    @Mock private Paint mockPaint;

    @BeforeEach
    protected void setup() {
        super.setup(new LinearTool(null));
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
    void testThatMousePressedSetsInitialPoint() {
        uut.mousePressed(mockEvent, mockPoint);
        assertEquals(mockPoint, uut.getInitialPoint());
    }

    @Test
    void testThatMouseReleasedCommitsLine() {
        uut.activate(mockCanvas);
        uut.mouseReleased(mockEvent, mockPoint);

        Mockito.verify(mockCanvas).commit();
    }

    @Test
    void testThatMouseDraggedDrawsLine() {
        Point start = new Point(10, 10);
        Point end = new Point(100, 100);

        Mockito.when(mockToolAttributes.getStroke()).thenReturn(mockStroke);
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockPaint);

        uut.setDelegate(mockDelegate);
        uut.activate(mockCanvas);
        uut.mousePressed(mockEvent, start);
        uut.mouseDragged(mockEvent, end);

        Mockito.inOrder(mockScratch, mockDelegate, mockCanvas);

        Mockito.verify(mockScratch).clear();
        Mockito.verify(mockDelegate).drawLine(mockScratch, mockStroke, mockPaint, start.x, start.y, end.x, end.y);
        Mockito.verify(mockCanvas).repaint();
    }

    @Test
    void testThatMouseDraggedDrawsConstrainedLineWhenShiftIsDown() {
        Point start = new Point(10, 10);
        Point end = new Point(100, 100);

        Mockito.when(mockToolAttributes.getStroke()).thenReturn(mockStroke);
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockPaint);
        Mockito.when(mockEvent.isShiftDown()).thenReturn(true);
        Mockito.when(mockToolAttributes.getConstrainedAngle()).thenReturn(17);

        uut.setDelegate(mockDelegate);
        uut.activate(mockCanvas);
        uut.mousePressed(mockEvent, start);
        uut.mouseDragged(mockEvent, end);

        Point expectedEnd = MathUtils.line(start, end, mockToolAttributes.getConstrainedAngle());

        Mockito.inOrder(mockScratch, mockDelegate, mockCanvas);

        Mockito.verify(mockScratch).clear();
        Mockito.verify(mockDelegate).drawLine(mockScratch, mockStroke, mockPaint, start.x, start.y, expectedEnd.x, expectedEnd.y);
        Mockito.verify(mockCanvas).repaint();
    }

}