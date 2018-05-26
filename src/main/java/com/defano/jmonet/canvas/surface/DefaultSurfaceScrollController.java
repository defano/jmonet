package com.defano.jmonet.canvas.surface;

import javax.swing.*;

import java.awt.*;

import static javax.swing.ScrollPaneConstants.*;

/**
 * Provides basic scroll behavior when a {@link AbstractSurface} is the viewport of a {@link JScrollPane}.
 *
 * This default implementation is sufficient for cases when the surface is added to a {@link JScrollPane} as the view
 * port. If the surface (canvas) is embedded is some container with additional components or decorations a custom
 * implementation may be required to properly adjust viewport scroll position taking into account the size and layout
 * of the surface's sibling components.
 */
public class DefaultSurfaceScrollController implements SurfaceScrollController {

    private final AbstractSurface surface;
    private double scrollPercentX, scrollPercentY;

    public DefaultSurfaceScrollController(AbstractSurface surface) {
        this.surface = surface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScrollPosition(double percentX, double percentY) {
        this.scrollPercentX = percentX;
        this.scrollPercentY = percentY;

        resetScrollPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetScrollPosition() {
        JScrollPane scrollPane = getScrollPane();

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getViewRect() {
        JScrollPane scrollPane = getScrollPane();
        return scrollPane == null ? new Rectangle() : scrollPane.getViewport().getViewRect();
    }

    /**
     * Returns the {@link JScrollPane} that is the parent of the surface, or null, if the surface is not the viewport
     * of a scroll pane (or is otherwise embedded in a container that is the viewport).
     *
     * @return The scroll pane object the surface is directly embedded in, or null.
     */
    private JScrollPane getScrollPane() {
        if (surface.getParent() != null &&
                surface.getParent() instanceof JViewport &&
                surface.getParent().getParent() != null &&
                surface.getParent().getParent() instanceof JScrollPane) {

            return (JScrollPane) surface.getParent().getParent();
        }

        return null;
    }
}
