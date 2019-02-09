package com.defano.jmonet.canvas.layer;

import com.defano.jmonet.context.GraphicsContext;
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
    @Mock private GraphicsContext mockGraphics;

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
        final Point location = new Point(10, 20);

        ImageLayer uut = new ImageLayer(location, mockImage, mockComposite);

        Mockito.when(mockImage.getWidth()).thenReturn(imageWidth);
        Mockito.when(mockImage.getHeight()).thenReturn(imageHeight);

        assertEquals(new Dimension(location.x + imageWidth, location.y + imageHeight), uut.getDisplayedSize());
    }

    @Test
    void testGetStoredSize() {
        final int imageWidth = 100;
        final int imageHeight = 200;
        final Point location = new Point(10, 20);

        ImageLayer uut = new ImageLayer(location, mockImage, mockComposite);

        Mockito.when(mockImage.getWidth()).thenReturn(imageWidth);
        Mockito.when(mockImage.getHeight()).thenReturn(imageHeight);

        assertEquals(new Dimension(imageWidth, imageHeight), uut.getStoredSize());
    }

    @Test
    void testThatImagePaintsUnscaledWithNoClippingRect() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, 1.0, null);

        final Rectangle source = new Rectangle(0, 0, 100, 200);
        final Rectangle projection = new Rectangle(10, 20, 100, 200);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithNoClippingRect() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 16.0;

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, null);

        final Rectangle source = new Rectangle(0, 0, 100, 200);
        final Rectangle projection = new Rectangle(160, 320, 1600, 3200);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithUnoccludedClippingRect() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 3.0;

        // Clipping rectangle does not drawn occlude image
        final Rectangle clip = new Rectangle(0, 0, 1000, 1000);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, clip);

        final Rectangle source = new Rectangle(0, 0, 100, 200);
        final Rectangle projection = new Rectangle(30, 60, 300, 600);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithOccludedLeftClippingRgn() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 3.0;

        // Clipping rectangle occludes left portion of image
        final Rectangle clip = new Rectangle(100, 0, 1000, 1000);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, clip);

        final Rectangle source = new Rectangle(23, 0, 100, 200);
        final Rectangle projection = new Rectangle(0, 60, 300, 600);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithOccludedTopClippingRgn() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 4.0;

        // Clipping rectangle occludes top portion of image
        final Rectangle clip = new Rectangle(0, 100, 1000, 1000);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, clip);

        final Rectangle source = new Rectangle(0, 5, 100, 200);
        final Rectangle projection = new Rectangle(40, 0, 400, 800);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithOccludedRightClippingRgn() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 4.0;

        // Clipping rectangle occludes top portion of image
        final Rectangle clip = new Rectangle(0, 0, 100, 1000);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, clip);

        final Rectangle source = new Rectangle(0, 0, 25, 200);
        final Rectangle projection = new Rectangle(40, 80, 100, 800);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithOccludedBottomClippingRgn() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 4.0;

        // Clipping rectangle occludes top portion of image
        final Rectangle clip = new Rectangle(0, 0, 1000, 100);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, clip);

        final Rectangle source = new Rectangle(0, 0, 100, 25);
        final Rectangle projection = new Rectangle(40, 80, 400, 100);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

    @Test
    void testThatImagePaintsScaledWithFullyOccludedClippingRgn() {
        final Dimension storedDimension = new Dimension(100, 200);
        final Point imageLocation = new Point(10, 20);
        final double scale = 4.0;

        // No portion of the image appears within the clipping rect
        final Rectangle clip = new Rectangle(0, 0, 1, 1);

        ImageLayer uut = new ImageLayer(imageLocation, mockImage, mockComposite);
        Mockito.when(mockImage.getWidth()).thenReturn(storedDimension.width);
        Mockito.when(mockImage.getHeight()).thenReturn(storedDimension.height);

        uut.paint(mockGraphics, scale, clip);

        final Rectangle source = new Rectangle(0, 0, 0, 0);
        final Rectangle projection = new Rectangle(40, 80, 0, 0);

        Mockito.verify(mockGraphics).setComposite(mockComposite);
        Mockito.verify(mockGraphics).drawImage(mockImage, projection.x, projection.y, projection.x + projection.width, projection.y + projection.height, source.x, source.y, source.x + source.width, source.y + source.height, null);
    }

}
