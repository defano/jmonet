package com.defano.jmonet.canvas.surface;

import java.awt.*;

/**
 * A surface that supports painting "scan lines", that is, a thin line separating rows and columns of pixels that,
 * when visible, produces a matrix effect.
 * <p>
 * Scan lines are typically visible only once the scale factor of an image exceeds some threshold (as rendering
 * scanlines with without scaling results in all scan lines and no image pixels).
 */
public interface ScanlineSurface extends ScalableSurface {

    /**
     * Gets the minimum scale factor that must be met before scanlines are rendered on the image.
     *
     * @return The minimum scale factor at which scanlines become visible.
     */
    double getScanlineScaleThreadhold();

    /**
     * Sets the minimum scale factor that must be met before scanlines are rendered on the image. Set to
     * {@link Integer#MAX_VALUE} to prevent scanlines from ever being visible.
     *
     * @param scanlineThreadhold The minimum scale factor at which scanlines become visible.
     */
    void setScanlineScaleThreadhold(double scanlineThreadhold);

    /**
     * Gets the color used to paint scanlines.
     *
     * @return The scanline color
     */
    Color getScanlineColor();

    /**
     * Sets the color used to paint scanlines.
     *
     * @param scanlineColor The scanline color
     */
    void setScanlineColor(Color scanlineColor);

    /**
     * Gets the alpha composite mode used when drawing scanlines; impacts how the scanlines affect the underlying
     * graphic and background.
     *
     * @return The scanline alpha composite mode.
     */
    AlphaComposite getScanlineComposite();

    /**
     * Sets the alpha composite mode used when drawing scanlines; impacts how the scanlines affect the underlying
     * graphic and background.
     *
     * @param scanlineComposite The scanline alpha composite mode.
     */
    void setScanlineComposite(AlphaComposite scanlineComposite);

    /**
     * Paints scanlines on the given graphics context.
     *
     * @param g    The graphics context
     * @param size The size of region onto which scanlines should extend
     */
    default void paintScanlines(Graphics2D g, Dimension size) {
        double scale = getScale();

        if (scale > getScanlineScaleThreadhold()) {
            g.setPaint(getScanlineColor());
            g.setComposite(getScanlineComposite());
            g.setStroke(new BasicStroke(1));

            for (int scanLine = 0; scanLine < size.height; scanLine += scale) {
                g.fillRect(0, scanLine, size.width, 1);
            }

            for (int scanLine = 0; scanLine < size.width; scanLine += scale) {
                g.fillRect(scanLine, 0, 1, size.height);
            }
        }
    }

}
