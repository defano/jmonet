package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.observable.ChangeSetObserver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one or more changes that should be applied to the canvas atomically.
 *
 * This is useful for "packaging" multiple changes together. For example, selecting and moving a portion of the canvas
 * is represented by a two-change, {@link ChangeSet}; first, the deletion of the selected image
 * from the canvas, followed by the addition of that selected image back to the canvas in its new location.
 */
public class ChangeSet {

    private final List<ChangeSetObserver> observers = new ArrayList<>();
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

        fireChangeSetObservers();
    }

    public void removeChange(int change) {
        images.remove(change);
        composites.remove(change);

        fireChangeSetObservers();
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

    public void addChangeSetObserver(ChangeSetObserver observer) {
        observers.add(observer);
    }

    public boolean removeChangeSetObserver(ChangeSetObserver observer) {
        return observers.remove(observer);
    }

    private void fireChangeSetObservers() {
        for (ChangeSetObserver observer : observers) {
            observer.onChangeSetModified();
        }
    }
}
