package com.defano.jmonet.canvas.observable;

import com.defano.jmonet.canvas.ChangeSet;

/**
 * An observer of modifications to a {@link com.defano.jmonet.canvas.ChangeSet}.
 */
public interface ChangeSetObserver {

    /**
     * Fired to indicate a {@link com.defano.jmonet.canvas.ChangeSet} was modified.
     * @param modified The ChangeSet that was modified.
     */
    void onChangeSetModified(ChangeSet modified);
}
