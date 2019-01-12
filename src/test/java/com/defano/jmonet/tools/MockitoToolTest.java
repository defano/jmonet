package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.JMonetCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.context.GraphicsContext;
import com.defano.jmonet.tools.base.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Matchers.*;

public class MockitoToolTest<T extends Tool> {

    protected T uut;

    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected Scratch mockScratch;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected GraphicsContext mockAddScratchGraphics;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected GraphicsContext mockRemoveScratchGraphics;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected JMonetCanvas mockCanvas;
    @Mock(answer=Answers.RETURNS_DEEP_STUBS) protected BufferedImage mockCanvasImage;

    public void initialize(T uut) {
        MockitoAnnotations.initMocks(this);

        this.uut = uut;
        this.uut.activate(mockCanvas);

        Mockito.when(mockCanvas.getCanvasImage()).thenReturn(mockCanvasImage);

        // Provide mock add scratch
        Mockito.when(mockScratch.getAddScratchGraphics(any(), any(), any())).thenReturn(mockAddScratchGraphics);
        Mockito.when(mockScratch.getAddScratchGraphics(any(), any())).thenReturn(mockAddScratchGraphics);

        // Provide mock remove scratch
        Mockito.when(mockScratch.getRemoveScratchGraphics(any(), any(), any())).thenReturn(mockRemoveScratchGraphics);
        Mockito.when(mockScratch.getRemoveScratchGraphics(any(), any())).thenReturn(mockRemoveScratchGraphics);

    }

    public static ShapeMatcher matchesShape(Shape s) {
        return new ShapeMatcher(s);
    }

}