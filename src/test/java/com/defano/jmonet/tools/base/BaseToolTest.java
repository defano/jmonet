package com.defano.jmonet.tools.base;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.model.Interpolation;
import com.defano.jmonet.tools.attributes.ToolAttributes;
import com.defano.jmonet.tools.cursors.CursorManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;

public class BaseToolTest<ToolType extends Tool> extends MockitoTest {

    protected ToolType uut;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected CursorManager mockCursorManager;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected ToolAttributes mockToolAttributes;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected Cursor mockCursor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected PaintCanvas mockCanvas;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected Scratch mockScratch;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected GraphicsContext mockAddScratch;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected GraphicsContext mockRemoveScratch;

    // Can't mock enums
    protected Interpolation expectedInterpolation = Interpolation.BICUBIC;

    protected void setup(ToolType uut) {
        super.setup();

        this.uut = uut;
        Guice.createInjector(new BaseToolTest.BasicToolAssembly()).injectMembers(this.uut);

        Mockito.when(mockToolAttributes.getAntiAliasing()).thenReturn(expectedInterpolation);
        Mockito.when(mockCanvas.getScratch()).thenReturn(mockScratch);
        Mockito.when(mockScratch.getAddScratchGraphics(uut, null)).thenReturn(mockAddScratch);
        Mockito.when(mockScratch.getRemoveScratchGraphics(uut, null)).thenReturn(mockRemoveScratch);
    }

    private class BasicToolAssembly extends AbstractModule {
        @Override
        protected void configure() {
            bind(CursorManager.class).toInstance(mockCursorManager);
            bind(ToolAttributes.class).toInstance(mockToolAttributes);
        }
    }
}
