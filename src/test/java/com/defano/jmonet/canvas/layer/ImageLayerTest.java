package com.defano.jmonet.canvas.layer;

import com.defano.jmonet.tools.base.MockitoTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageLayerTest extends MockitoTest {

    @Mock private BufferedImage mockImage;
    @Mock private Composite mockComposite;
    @Mock private Point mockLocation;

    @Test
    void testThatConstructorLocatesImageAtOrigin() {
        ImageLayer uut = new ImageLayer(mockImage);

        assertEquals(mockImage, uut.getImage());
        assertEquals(new Point(), uut.getLocation());
        assertEquals(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f), uut.getComposite());
    }

    @Test
    void testThatConstructorDrawsImageWithSpecifiedComposite() {
        ImageLayer uut = new ImageLayer(mockImage, mockComposite);

        assertEquals(mockImage, uut.getImage());
        assertEquals(new Point(), uut.getLocation());
        assertEquals(mockComposite, uut.getComposite());
    }

    @Test
    void testThatConstructorDrawsImageWithSpecifiedCompositeAndLocation() {
        ImageLayer uut = new ImageLayer(mockLocation, mockImage, mockComposite);

        assertEquals(mockImage, uut.getImage());
        assertEquals(mockLocation, uut.getLocation());
        assertEquals(mockComposite, uut.getComposite());
    }

    @Test
    void testGetDisplayedSize() {
        final int imageWidth = 100;
        final int imageHeight = 200;
        final int locationX = 10;
        final int locationY = 20;

        ImageLayer uut = new ImageLayer(mockLocation, mockImage, mockComposite);

        Mockito.when(mockImage.getWidth()).thenReturn(imageWidth);
        Mockito.when(mockImage.getHeight()).thenReturn(imageHeight);
        Mockito.when(mockLocation.x).thenReturn(locationX);
        Mockito.when(mockLocation.y).thenReturn(locationY);

        assertEquals(new Dimension(locationX + imageWidth, locationY + imageHeight), uut.getDisplayedSize());
    }
}
