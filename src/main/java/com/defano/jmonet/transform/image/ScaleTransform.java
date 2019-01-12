package com.defano.jmonet.transform.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Transforms an image by scaling it to a specified dimension.
 */
public class ScaleTransform implements ImageTransform {

    private final Dimension size;

    /**
     * Creates a scale transform.
     *
     * @param size The dimension to which the image should be resized/scaled
     */
    public ScaleTransform(Dimension size) {
        this.size = size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage apply(BufferedImage source) {
        BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) resized.getGraphics();
        g.drawImage(source, 0, 0, size.width, size.height, null);
        g.dispose();

        return resized;
    }
}
