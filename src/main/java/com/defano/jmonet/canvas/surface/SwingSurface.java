package com.defano.jmonet.canvas.surface;

import javax.swing.*;
import java.awt.*;

/**
 * A surface to which Swing components can be added and removed.
 */
public interface SwingSurface {

    /**
     * Adds a component to the surface.
     *
     * @param component The component to be added.
     */
    void addComponent(Component component);

    /**
     * Removes a component from the surface; has no effect if the given component is not a child of this surface.
     *
     * @param component The component to remove
     */
    void removeComponent(Component component);

    /**
     * Returns the <code>ActionMap</code> used to determine what
     * <code>Action</code> to fire for particular <code>KeyStroke</code>
     * binding. The returned <code>ActionMap</code>, unless otherwise
     * set, will have the <code>ActionMap</code> from the UI set as the parent.
     *
     * @return the <code>ActionMap</code> containing the key/action bindings
     */
    ActionMap getActionMap();

    Component getComponent();
}
