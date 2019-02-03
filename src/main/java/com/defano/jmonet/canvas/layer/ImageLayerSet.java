package com.defano.jmonet.canvas.layer;

import com.defano.jmonet.canvas.observable.LayerSetObserver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A set of images rendered by layering one atop another. Each layer specifies an {@link AlphaComposite} mode to use
 * during rendering.
 */
public class ImageLayerSet implements LayeredImage {

    private final List<LayerSetObserver> observers = new ArrayList<>();
    private final List<ImageLayer> layers = new ArrayList<>();

    /**
     * Constructs an empty ImageLayerSet.
     */
    public ImageLayerSet() {
    }

    /**
     * Constructs an ImageLayerSet containing a single layer of the given image.
     * @param image The image comprising the single layer of this ImageLayerSet.
     */
    public ImageLayerSet(BufferedImage image) {
        addLayer(new ImageLayer(image));
    }

    /**
     * Constructs a LayerSet containing the specified layer.
     *
     * @param layer The layer comprising the LayerSet
     */
    public ImageLayerSet(ImageLayer layer) {
        addLayer(layer);
    }

    /**
     * Adds a layer to this LayerSet.
     * <p>
     * When producing an image from a ChangeSet, each layer image is drawn atop the previous using the
     * {@link AlphaComposite} associated with it.
     *
     * @param layer The image layer to add to the set.
     */
    public void addLayer(ImageLayer layer) {
        layers.add(layer);
        fireLayerSetObservers();
    }

    /**
     * The number of layers in this LayerSet.
     *
     * @return The size of the LayerSet
     */
    public int size() {
        return layers.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageLayer[] getImageLayers() {
        return layers.toArray(new ImageLayer[0]);
    }

    /**
     * Adds an observer to this ChangeSet to notify listeners of new changes added to it.
     *
     * @param observer The observer to be added.
     */
    public void addLayerSetObserver(LayerSetObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer of this ChangeSet.
     *
     * @param observer The observer to remove.
     * @return True if the observer exists and was removed; false otherwise.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean removeLayerSetObserver(LayerSetObserver observer) {
        return observers.remove(observer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageLayerSet imageLayerSet = (ImageLayerSet) o;
        return imageLayerSet.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(layers.toArray());
    }

    private void fireLayerSetObservers() {
        for (LayerSetObserver observer : observers) {
            observer.onLayerSetModified(this);
        }
    }

}
