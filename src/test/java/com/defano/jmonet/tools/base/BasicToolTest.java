package com.defano.jmonet.tools.base;

import com.defano.jmonet.model.PaintToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class BasicToolTest extends BaseToolTest<BasicTool> {

    @BeforeEach
    protected void setup() {
        super.setup(new BasicTool(null));
    }

    @Test
    void testThatGetToolTypeReturnsToolType() {
        for (PaintToolType thisType : PaintToolType.values()) {
            assertEquals(thisType, new BasicTool(thisType).getPaintToolType());
        }
    }

    @Test
    void testThatNewToolIsNotActive() {
        assertFalse(new BasicTool<>(null).isActive());
    }

    @Test
    void testThatActivatedToolIsActive() {
        uut.activate(mockCanvas);
        assertTrue(uut.isActive());
    }

    @Test
    void testThatDeactivatedToolIsNotActive() {
        uut.activate(mockCanvas);
        assertTrue(uut.isActive());

        uut.deactivate();
        assertFalse(uut.isActive());
    }

    @Test
    void testThatActivationSetsSurfaceInteractionListener() {
        uut.activate(mockCanvas);
        Mockito.verify(mockCanvas).addSurfaceInteractionObserver(uut);
    }

    @Test
    void testThatActivationSetsCursor() {
        uut.activate(mockCanvas);
        Mockito.verify(mockCursorManager).setToolCursor(any(), eq(mockCanvas));
    }

    @Test
    void testThatSetCursorDelegatesToCursorManager() {
        uut.activate(mockCanvas);
        uut.setToolCursor(mockCursor);

        Mockito.verify(mockCursorManager).setToolCursor(mockCursor, mockCanvas);
    }

    @Test
    void testThatGetCursorDelegatesToCursorManager() {
        Mockito.when(mockCursorManager.getToolCursor()).thenReturn(mockCursor);
        assertEquals(mockCursor, uut.getToolCursor());
        Mockito.verify(mockCursorManager).getToolCursor();
    }

    @Test
    void testThatGetCanvasThrowsWhenNotActive() {
        assertThrows(IllegalStateException.class, () -> uut.getCanvas());
    }

    @Test
    void testThatCanvasIsReturnedWhenActive() {
        uut.activate(mockCanvas);
        assertEquals(mockCanvas, uut.getCanvas());
    }

    @Test
    void testThatInjectedAttributesAreReturned() {
        assertEquals(mockToolAttributes, uut.getAttributes());
    }

    @Test
    void testThatProperlyConfiguredScratchIsReturned() {

        uut.activate(mockCanvas);
        assertEquals(mockScratch, uut.getScratch());

        Mockito.verify(mockAddScratch).setAntialiasingMode(expectedInterpolation);
        Mockito.verify(mockRemoveScratch).setAntialiasingMode(expectedInterpolation);
    }

}