package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.model.PaintToolType;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.cursors.CursorManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class BasicToolTest extends MockitoTest {

    private BasicTool uut;

    @Mock private Cursor mockCursor;
    @Mock private PaintCanvas mockCanvas;
    @Mock private CursorManager mockCursorManager;
    @Mock private ToolAttributes mockToolAttributes;
    @Mock private Scratch mockScratch;
    @Mock private GraphicsContext mockAddScratch;
    @Mock private GraphicsContext mockRemoveScratch;

    @BeforeEach
    void setup() {
        super.setup();

        uut = new BasicTool(null);
        Guice.createInjector(new BasicToolAssembly()).injectMembers(uut);
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

        Interpolation expectedInterp = Interpolation.BICUBIC;

        Mockito.when(mockToolAttributes.getAntiAliasing()).thenReturn(expectedInterp);
        Mockito.when(mockCanvas.getScratch()).thenReturn(mockScratch);
        Mockito.when(mockScratch.getAddScratchGraphics(uut, null)).thenReturn(mockAddScratch);
        Mockito.when(mockScratch.getRemoveScratchGraphics(uut, null)).thenReturn(mockRemoveScratch);

        uut.activate(mockCanvas);
        assertEquals(mockScratch, uut.getScratch());

        Mockito.verify(mockAddScratch).setAntialiasingMode(expectedInterp);
        Mockito.verify(mockRemoveScratch).setAntialiasingMode(expectedInterp);
    }

    private class BasicToolAssembly extends AbstractModule {
        @Override
        protected void configure() {
            bind(CursorManager.class).toInstance(mockCursorManager);
            bind(ToolAttributes.class).toInstance(mockToolAttributes);
        }
    }

}