package com.defano.jmonet.canvas.surface;


import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A transparent, un-focusable, Swing component that dispatches canvas interaction events to registered observers.
 */
public abstract class AbstractSurface extends JComponent implements Surface, KeyListener, MouseListener,
        MouseMotionListener, KeyEventDispatcher {

    private final List<SurfaceInteractionObserver> interactionListeners = new ArrayList<>();
    private SurfaceScrollController surfaceScrollController = new DefaultSurfaceScrollController(this);

    public AbstractSurface() {
        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);

        // Adding a KeyListener to this component won't always work the way the user expects; this is cheating, but
        // it assures paint tools get all key events, regardless of the component hierarchy we may be embedded in.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        surfaceScrollController = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        interactionListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return interactionListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void keyTyped(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.keyTyped(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void keyPressed(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.keyPressed(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void keyReleased(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.keyReleased(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseClicked(MouseEvent e) {
        requestFocus();

        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseClicked(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mousePressed(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mousePressed(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseReleased(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseReleased(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseEntered(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseEntered(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseExited(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseExited(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseDragged(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseDragged(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void mouseMoved(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners.toArray(new SurfaceInteractionObserver[]{})) {
            thisListener.mouseMoved(e, convertViewPointToModel(e.getPoint()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent(Component component) {
        add(component);

        revalidate();
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent(Component component) {
        remove(component);

        revalidate();
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_TYPED:
                AbstractSurface.this.keyTyped(e);
                break;
            case KeyEvent.KEY_PRESSED:
                AbstractSurface.this.keyPressed(e);
                break;
            case KeyEvent.KEY_RELEASED:
                AbstractSurface.this.keyReleased(e);
                break;
        }

        return false;
    }

    public SurfaceScrollController getSurfaceScrollController() {
        return surfaceScrollController;
    }

    public void setSurfaceScrollController(SurfaceScrollController surfaceScrollController) {
        this.surfaceScrollController = surfaceScrollController;
    }
}
