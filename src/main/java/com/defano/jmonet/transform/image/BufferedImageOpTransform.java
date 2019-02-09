package com.defano.jmonet.transform.image;

import com.defano.jmonet.tools.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Transforms an image by applying a {@link BufferedImageOp} to it.
 */
public class BufferedImageOpTransform implements StaticImageTransform {

    private final BufferedImageOp operation;

    public BufferedImageOpTransform(BufferedImageOp operation) {
        this.operation = operation;
    }

    @Override
    public BufferedImage apply(BufferedImage source) {
        BufferedImage destination = ImageUtils.newArgbOfSize(source);
        operation.filter(source, destination);
        return destination;
    }
}
