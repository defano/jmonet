package com.defano.jmonet.canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one or more changes to an image that should be applied to the canvas atomically.
 *
 * This is useful for "packaging" multiple changes together that should be treated as one. For example, selecting and
 * moving a portion of the canvas is represented by a two-change, ChangeSet; first, the deletion of the selected image
 * from the canvas, and second, the addition of that selected image back to the canvas in its new location.
 */
public class ChangeSet {

    private final List<BufferedImage> images = new ArrayList<>();
    private final List<AlphaComposite> composites = new ArrayList<>();

    public ChangeSet(BufferedImage image) {
        addChange(image, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public ChangeSet(BufferedImage image, AlphaComposite composite) {
        addChange(image, composite);
    }

    public void addChange(BufferedImage image, AlphaComposite composite) {
        images.add(image);
        composites.add(composite);
    }

    public int size() {
        return images.size();
    }

    public BufferedImage getImage(int change) {
        return images.get(change);
    }

    public AlphaComposite getComposite(int change) {
        return composites.get(change);
    }
}
