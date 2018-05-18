package com.defano.jmonet.algo.transform.image;

import com.defano.jmonet.algo.transform.ImageTransform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScaleTransform implements ImageTransform {

    private Dimension size;

    public ScaleTransform(Dimension size) {
        this.size = size;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) resized.getGraphics();
        g.drawImage(image, 0, 0, size.width, size.height, null);
        g.dispose();

        return resized;
    }
}
