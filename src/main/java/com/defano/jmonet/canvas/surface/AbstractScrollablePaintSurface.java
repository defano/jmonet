package com.defano.jmonet.canvas.surface;


import javax.swing.*;

/**
 * A scrollable pane containing a {@link PaintSurface}.
 */
public abstract class AbstractScrollablePaintSurface extends JScrollPane implements Scrollable {

    private PaintSurface surface;
    private double scrollPercentX, scrollPercentY = 0;

    public AbstractScrollablePaintSurface() {
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    /**
     * Sets the {@link PaintSurface} to be scrolled within this component.
     * @param surface The {@link PaintSurface} to scroll.
     */
    protected void setSurface(PaintSurface surface) {
        this.surface = surface;

        setBorder(BorderFactory.createEmptyBorder());

        getViewport().removeAll();
        getViewport().add(surface);
        revalidate();
    }

    /**
     * Gets the {@link PaintSurface} comprising the viewport of this scroll pane.
     * @return The viewport
     */
    public PaintSurface getSurface() {
        return surface;
    }

    /** {@inheritDoc} */
    @Override
    public void setScrollPosition(double percentX, double percentY) {
        this.scrollPercentX = percentX;
        this.scrollPercentY = percentY;

        updateScroll();
    }

    /** {@inheritDoc} */
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
