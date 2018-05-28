package com.defano.jmonet.canvas.surface;

import javax.swing.*;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * Provides basic scroll behavior when a {@link AbstractPaintSurface} is the viewport of a {@link JScrollPane}.
 * <p>
 * This default implementation is sufficient for cases when the surface is added to a {@link JScrollPane} as the view
 * port. If the surface (canvas) is embedded is some container with additional components or decorations a custom
 * implementation may be required to properly adjust viewport scroll position taking into account the size and layout
 * of the surface's sibling components.
 */
public class DefaultSurfaceScrollController implements SurfaceScrollController {

    private final AbstractPaintSurface surface;

    public DefaultSurfaceScrollController(AbstractPaintSurface surface) {
        this.surface = surface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScrollPosition(Point position) {
        JScrollPane scrollPane = getScrollPane();

        if (scrollPane != null) {
            scrollPane.getViewport().setViewPosition(position);
            scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

            scrollPane.getTopLevelAncestor().revalidate();
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getScrollRect() {
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
