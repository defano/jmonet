package com.defano.jmonet.canvas;

/**
 * An object that holds resources/memory that cannot be automatically garbage collected by the VM.
 */
public interface Disposable {

    /**
     * Releases memory-holding resources and unregisters any listeners or observables. The disposed object should
     * not be used after this method has been invoked.
     */
    void dispose();
}
