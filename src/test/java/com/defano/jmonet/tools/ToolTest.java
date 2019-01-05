package com.defano.jmonet.tools;

import com.defano.jmonet.canvas.PaintCanvas;
import com.defano.jmonet.canvas.Scratch;
import com.defano.jmonet.tools.builder.DefaultToolAttributes;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Matchers.any;

public class ToolTest <T extends DefaultToolAttributes> {

    protected T uut;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected PaintCanvas mockCanvas;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected BufferedImage mockCanvasImage;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected Scratch mockScratch;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected Graphics2D mockAddScratchGraphics;

    public void initialize(T uut) {
        MockitoAnnotations.initMocks(this);

        this.uut = uut;

//        Mockito.when(uut.getCanvas()).thenReturn(mockCanvas);
        Mockito.when(mockCanvas.getCanvasImage()).thenReturn(mockCanvasImage);
        Mockito.when(mockScratch.getAddScratchGraphics(any(), any(), any())).thenReturn(mockAddScratchGraphics);
        Mockito.when(mockScratch.getAddScratchGraphics(any(), any())).thenReturn(mockAddScratchGraphics);

    }

}
