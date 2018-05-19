package com.defano.jmonet.canvas.observable;

import com.defano.jmonet.canvas.layer.ImageLayerSet;

/**
 * An observer of modifications to a {@link ImageLayerSet}.
 */
public interface LayerSetObserver {

    /**
     * Fired to indicate a {@link ImageLayerSet} was modified.
     * @param modified The ChangeSet that was modified.
     */
    void onLayerSetModified(ImageLayerSet modified);
}
