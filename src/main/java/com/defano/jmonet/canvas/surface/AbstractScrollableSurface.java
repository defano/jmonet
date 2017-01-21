package com.defano.jmonet.canvas.surface;


import javax.swing.*;

/**
 * A scrollable pane containing a {@link PaintableSurface}.
 */
public abstract class AbstractScrollableSurface extends JScrollPane implements Scrollable {

    private PaintableSurface surface;
    private double scrollPercentX, scrollPercentY = 0;

    public AbstractScrollableSurface() {
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    protected void setSurface(PaintableSurface surface) {
        this.surface = surface;

        setBorder(BorderFactory.createEmptyBorder());

        getViewport().removeAll();
        getViewport().add(surface);
        revalidate();
    }

    public PaintableSurface getSurface() {
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
}
