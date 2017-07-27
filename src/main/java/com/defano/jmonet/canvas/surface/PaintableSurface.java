package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.observable.SurfaceInteractionObserver;
import com.defano.jmonet.canvas.observable.ObservableSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class PaintableSurface extends JComponent implements CompositeSurface, ObservableSurface, KeyListener, MouseListener, MouseMotionListener, KeyEventDispatcher {

    private ScalableLayeredPainting painting;
    private final List<SurfaceInteractionObserver> interactionListeners = new ArrayList<>();

    public PaintableSurface() {
        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);

        // Adding a KeyListener to this component won't always work the way the user expects; this is cheating, but
        // it assures paint tools get all key events, regardless of the component hierarchy we may be embedded in.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public void dispose() {
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
        removeAll();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isVisible()) {
            double scale = painting.getScale();

            Graphics2D g2d = (Graphics2D) g;
            for (BufferedImage thisLayer : painting.getPaintLayers()) {
                g2d.drawImage(thisLayer, 0, 0, (int) (thisLayer.getWidth() * scale), (int) (thisLayer.getHeight() * scale), null);
            }
        }

        // DO NOT dispose the graphics context in this method.
    }

    @Override
    public void addSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        interactionListeners.add(listener);
    }

    @Override
    public boolean removeSurfaceInteractionObserver(SurfaceInteractionObserver listener) {
        return interactionListeners.remove(listener);
    }

    @Override
    public final void keyTyped(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.keyTyped(e);
        }
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.keyPressed(e);
        }
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.keyReleased(e);
        }
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        requestFocus();

        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseClicked(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mousePressed(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseReleased(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseEntered(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseExited(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseDragged(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        for (SurfaceInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseMoved(e, painting.convertPointToImage(e.getPoint()));
        }
    }

    public void setPainting(ScalableLayeredPainting painting) {
        this.painting = painting;
    }

    public ScalableLayeredPainting getPainting() {
        return painting;
    }

    @Override
    public void addComponent(Component component) {
        add(component);

        revalidate();
        repaint();
    }

    @Override
    public void removeComponent(Component component) {
        remove(component);

        revalidate();
        repaint();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_TYPED:
                PaintableSurface.this.keyTyped(e);
                break;
            case KeyEvent.KEY_PRESSED:
                PaintableSurface.this.keyPressed(e);
                break;
            case KeyEvent.KEY_RELEASED:
                PaintableSurface.this.keyReleased(e);
                break;
        }

        return false;
    }
}
