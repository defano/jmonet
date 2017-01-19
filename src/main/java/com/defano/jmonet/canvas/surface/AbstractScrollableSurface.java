package com.defano.jmonet.canvas.surface;


import javax.swing.*;

public abstract class AbstractScrollableSurface extends JScrollPane implements ScrollableSurface {

    private PaintSurface surface;
    private double scrollPercentX, scrollPercentY = 0;

    protected void setSurface(PaintSurface surface) {
        this.surface = surface;

        setBorder(BorderFactory.createEmptyBorder());

        getViewport().removeAll();
        getViewport().add(surface);
        revalidate();
    }

    public PaintSurface getSurface() {
        return surface;
    }

    @Override
    public void setScrollPosition(double percentX, double percentY) {
        this.scrollPercentX = percentX;
        this.scrollPercentY = percentY;

        updateScroll();
    }

    @Override
    public void updateScroll() {
        double x = getHorizontalScrollBar().getMaximum() * scrollPercentX - getBounds().width / 2;
        double y = getVerticalScrollBar().getMaximum() * scrollPercentY - getBounds().height / 2;

        getVerticalScrollBar().setValue((int)y);
        getHorizontalScrollBar().setValue((int)x);
        int scrollBarSize = getVerticalScrollBar().getWidth();

        boolean showHorizScroll = ((getHorizontalScrollBar().getMaximum() - getViewport().getWidth()) > scrollBarSize * 2);
        boolean showVertScroll = ((getVerticalScrollBar().getMaximum() - getViewport().getHeight()) > scrollBarSize * 2);

        setHorizontalScrollBarPolicy(showHorizScroll ? HORIZONTAL_SCROLLBAR_ALWAYS : HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(showVertScroll ? VERTICAL_SCROLLBAR_ALWAYS : VERTICAL_SCROLLBAR_NEVER);
    }

    /**
     * Causes the Swing framework to redraw/refresh the Canvas by calling repaint on the Canvas's parent component.
     */
    public void invalidateCanvas() {
        if (getParent() != null) {
            getParent().repaint();
        }

        revalidate();
    }
}
