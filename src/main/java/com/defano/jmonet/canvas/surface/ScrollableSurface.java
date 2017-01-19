package com.defano.jmonet.canvas.surface;

public interface ScrollableSurface {

    /**
     * Scrolls the canvas so that the center of the viewport is displayed at the specified, relative, offset.
     * For example, if the image in the viewport is 1000x1000px and the specified scroll position is .5 and .5,
     * then coordinate (500,500) of the viewport will visible in the center of the view.
     *
     * @param percentX The percent to scroll in the horizontal direction, between 0 and 1.0
     * @param percentY The percent to scroll in the vertical direction, between 0 and 1.0
     */
    void setScrollPosition(double percentX, double percentY);

    /**
     * Resets the scroll position to the last value specified by {@link #setScrollPosition(double, double)}.
     */
    void updateScroll();

}
