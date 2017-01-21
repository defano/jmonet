package com.defano.jmonet.canvas.observable;

/**
 * An observer of modifications to a ChangeSet.
 */
public interface ChangeSetObserver {
    void onChangeSetModified();
}
