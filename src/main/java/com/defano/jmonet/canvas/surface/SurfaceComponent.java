package com.defano.jmonet.canvas.surface;


import javax.swing.*;

import static javax.swing.ScrollPaneConstants.*;

/**
 * A Swing component that .
 */
public abstract class SurfaceComponent extends JComponent implements Scrollable, ScrollableDelegate, Disposable {

    private ScrollableDelegate scrollDelegate = this;
    private double scrollPercentX, scrollPercentY = 0;

    public SurfaceComponent() {
        setOpaque(false);
    }

    @Override
    public void dispose() {
        scrollDelegate = null;
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
        if (scrollDelegate != null) {
            JScrollPane scrollPane = scrollDelegate.getScrollPane();

            if (scrollPane != null) {
                double x = scrollPane.getHorizontalScrollBar().getMaximum() * scrollPercentX - scrollPane.getViewport().getWidth() / 2;
                double y = scrollPane.getVerticalScrollBar().getMaximum() * scrollPercentY - scrollPane.getViewport().getHeight() / 2;

                scrollPane.getVerticalScrollBar().setValue((int) y);
                scrollPane.getHorizontalScrollBar().setValue((int) x);

                int scrollBarSize = scrollPane.getVerticalScrollBar().getWidth();

                boolean showHorizScroll = ((scrollPane.getHorizontalScrollBar().getMaximum() - scrollPane.getViewport().getWidth()) > scrollBarSize * 2);
                boolean showVertScroll = ((scrollPane.getVerticalScrollBar().getMaximum() - scrollPane.getViewport().getHeight()) > scrollBarSize * 2);

                scrollPane.setHorizontalScrollBarPolicy(showHorizScroll ? HORIZONTAL_SCROLLBAR_ALWAYS : HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(showVertScroll ? VERTICAL_SCROLLBAR_ALWAYS : VERTICAL_SCROLLBAR_NEVER);
            }
        }
    }

    @Override
    public JScrollPane getScrollPane() {
        if (this.getParent() != null &&
                this.getParent() instanceof JViewport &&
                this.getParent().getParent() != null &&
                this.getParent().getParent() instanceof JScrollPane) {

            return (JScrollPane) this.getParent().getParent();
        }

        return null;
    }

    public ScrollableDelegate getScrollDelegate() {
        return scrollDelegate;
    }

    public void setScrollDelegate(ScrollableDelegate delegate) {
        this.scrollDelegate = delegate;
    }

}
