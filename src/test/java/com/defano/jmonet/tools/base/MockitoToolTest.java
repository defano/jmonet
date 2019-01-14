package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.JMonetCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.cursors.CursorManager;
import com.defano.jmonet.transform.image.FillTransform;
import com.defano.jmonet.transform.image.FloodFillTransform;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.mockito.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Matchers.*;

public class MockitoToolTest<T extends BasicTool> {

    protected T uut;

    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected Scratch mockScratch;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected GraphicsContext mockAddScratchGraphics;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected GraphicsContext mockRemoveScratchGraphics;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected JMonetCanvas mockCanvas;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected BufferedImage mockCanvasImage;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected ToolAttributes mockToolAttributes;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected CursorManager mockCursorManager;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected FloodFillTransform mockFloodFillTransform;


    public void initialize(T uut) {
        MockitoAnnotations.initMocks(this);

        this.uut = uut;
        Guice.createInjector(new MockToolAssembly()).injectMembers(uut);
        this.uut.activate(mockCanvas);

        Mockito.when(mockCanvas.getCanvasImage()).thenReturn(mockCanvasImage);
        Mockito.when(mockCanvas.getScratch()).thenReturn(mockScratch);

        // Provide mock add scratch
        Mockito.when(mockScratch.getAddScratchGraphics(any(), any(), any())).thenReturn(mockAddScratchGraphics);
        Mockito.when(mockScratch.getAddScratchGraphics(any(), any())).thenReturn(mockAddScratchGraphics);

        // Provide mock remove scratch
        Mockito.when(mockScratch.getRemoveScratchGraphics(any(), any(), any())).thenReturn(mockRemoveScratchGraphics);
        Mockito.when(mockScratch.getRemoveScratchGraphics(any(), any())).thenReturn(mockRemoveScratchGraphics);
        Mockito.when(mockToolAttributes.getAntiAliasing()).thenReturn(Interpolation.NONE);
    }

    public static <T extends Shape> ShapeMatcher<T> matchesShape(T t) {
        return new ShapeMatcher(t);
    }

    public static CursorMatcher matchesCursor(Cursor c) {
        return new CursorMatcher(c);
    }

    private class MockToolAssembly extends AbstractModule {

        @Override
        protected void configure() {
            bind(ToolAttributes.class).toInstance(mockToolAttributes);
            bind(CursorManager.class).toInstance(mockCursorManager);
            bind(FloodFillTransform.class).toInstance(mockFloodFillTransform);
        }
    }


}
