package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.canvas.CanvasInteractionObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class PaintSurface extends JPanel implements Surface, KeyListener, MouseListener, MouseMotionListener {

    private ScalableScratchDrawable drawable;
    private List<CanvasInteractionObserver> interactionListeners = new ArrayList<>();

    public PaintSurface() {
        setOpaque(false);
        setEnabled(true);
        setFocusable(true);
        setLayout(null);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    public void setDrawable(ScalableScratchDrawable drawable) {
        this.drawable = drawable;
    }

    public ScalableScratchDrawable getDrawable() {
        return drawable;
    }

    public void paintComponent(Graphics g) {
        if (isVisible()) {
            double scale = drawable.getScale();

            Graphics2D g2d = (Graphics2D) g;
            BufferedImage canvasImage = drawable.getCanvasImage();
            BufferedImage scratchImage = drawable.getScratchImage();

            g2d.drawImage(canvasImage, 0, 0, (int) (canvasImage.getWidth() * scale), (int) (canvasImage.getHeight() * scale), null);
            g2d.drawImage(scratchImage, 0, 0, (int) (scratchImage.getWidth() * scale), (int) (scratchImage.getHeight() * scale), null);
        }
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
    public void addCanvasInteractionListener(CanvasInteractionObserver listener) {
        interactionListeners.add(listener);
    }

    @Override
    public boolean removeCanvasInteractionListener(CanvasInteractionObserver listener) {
        return interactionListeners.remove(listener);
    }

    @Override
    public final void keyTyped(KeyEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.keyTyped(e);
        }
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.keyPressed(e);
        }
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.keyReleased(e);
        }
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseClicked(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mousePressed(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseReleased(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseEntered(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseExited(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseDragged(e, drawable.convertPointToImage(e.getPoint()));
        }
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        for(CanvasInteractionObserver thisListener : interactionListeners) {
            thisListener.mouseMoved(e, drawable.convertPointToImage(e.getPoint()));
        }
    }
}
