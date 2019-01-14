package com.defano.jmonet.tools;

import com.defano.jmonet.tools.attributes.BoundaryFunction;
import com.defano.jmonet.tools.attributes.FillFunction;
import com.defano.jmonet.tools.base.MockitoToolTest;
import com.defano.jmonet.tools.cursors.CursorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

public class FillToolTest extends MockitoToolTest<FillTool> {

    @Mock private BoundaryFunction mockBoundaryFunction;
    @Mock private FillFunction mockFillFunction;
    @Mock private Paint mockFillPaint;
    @Mock private BufferedImage mockFilledImage;

    @BeforeEach
    public void setUp() {
        initialize(new FillTool());
    }

    @Test
    public void testGetDefaultCursor() {
        Mockito.verify(mockCursorManager).setToolCursor(argThat(matchesCursor(CursorFactory.makeBucketCursor())), eq(mockCanvas));
    }

    @Test
    public void testMousePressed() {
        Point floodOrigin = new Point();
        Dimension canvasSize = new Dimension();

        Mockito.when(mockToolAttributes.getBoundaryFunction()).thenReturn(mockBoundaryFunction);
        Mockito.when(mockToolAttributes.getFillPaint()).thenReturn(Optional.of(mockFillPaint));
        Mockito.when(mockToolAttributes.getFillFunction()).thenReturn(mockFillFunction);
        Mockito.when(mockFloodFillTransform.apply(mockCanvasImage)).thenReturn(mockFilledImage);
        Mockito.when(mockCanvas.getCanvasSize()).thenReturn(canvasSize);

        uut.mousePressed(null, floodOrigin);

        Mockito.verify(mockFloodFillTransform).setBoundaryFunction(mockBoundaryFunction);
        Mockito.verify(mockFloodFillTransform).setFillPaint(mockFillPaint);
        Mockito.verify(mockFloodFillTransform).setFill(mockFillFunction);
        Mockito.verify(mockFloodFillTransform).setOrigin(floodOrigin);

        Mockito.verify(mockScratch).setAddScratch(eq(mockFilledImage), argThat(matchesShape(new Rectangle(canvasSize))));

        Mockito.verify(mockCanvas).commit();
        Mockito.verify(mockCanvas).repaint();
    }
}