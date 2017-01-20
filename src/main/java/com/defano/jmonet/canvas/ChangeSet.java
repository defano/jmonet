package com.defano.jmonet.canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ChangeSet {

    private final List<BufferedImage> images = new ArrayList<>();
    private final List<AlphaComposite> composites = new ArrayList<>();

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
