package com.defano.jmonet.tools.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class PathToolTest extends BaseToolTest<PathTool> {

    @Mock private MouseEvent mockEvent;
    @Mock private Point mockPoint;
    @Mock private PathToolDelegate mockDelegate;
    @Mock private Stroke mockStroke;
    @Mock private Paint mockPaint;
    @Mock private Rectangle mockRegion;

    @BeforeEach
    protected void setup() {
        super.setup(new PathTool(null));
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
    void testThatMousePressedPaintsInitialPoint() {
        uut.activate(mockCanvas);
        uut.setDelegate(mockDelegate);

        Mockito.when(mockToolAttributes.getStroke()).thenReturn( mockStroke);
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockPaint);
        Mockito.when(mockScratch.getDirtyRegion()).thenReturn(mockRegion);

        uut.mousePressed(mockEvent, mockPoint);

        Mockito.verify(mockScratch).clear();
        Mockito.verify(mockDelegate).startPath(mockScratch, mockStroke, mockPaint, mockPoint);
        Mockito.verify(mockCanvas).repaint(mockRegion);
    }

    @Test
    void testThatMouseDraggedPaintsPath() {
        Point start = new Point(20, 20);
        Point end = new Point(40, 30);

        Mockito.when(mockToolAttributes.getStroke()).thenReturn( mockStroke);
        Mockito.when(mockToolAttributes.getStrokePaint()).thenReturn(mockPaint);
        Mockito.when(mockScratch.getDirtyRegion()).thenReturn(mockRegion);

        uut.activate(mockCanvas);
        uut.setDelegate(mockDelegate);
        uut.mousePressed(mockEvent, start);
        uut.mouseDragged(mockEvent, end);

        Mockito.inOrder(mockDelegate, mockCanvas);

        Mockito.verify(mockDelegate).addPoint(mockScratch, mockStroke, mockPaint, start, end);
        Mockito.verify(mockCanvas, Mockito.times(2)).repaint(mockRegion);

    }
}