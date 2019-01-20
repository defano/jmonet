package com.defano.jmonet.transform.pixel;

import com.defano.jmonet.transform.image.PixelTransform;

import java.awt.*;

/**
 * Saturates the alpha channel of any pixel that is not fully transparent or fully opaque (has no effect on fully opaque
 * or fully transparent pixels).
 */
public class RemoveAlphaPixelTransform implements PixelTransform {

    private final boolean makeTransparent;

    /**
     * Creates an alpha-removing transform.
     *
     * @param makeTransparent When true, translucent (but not opaque) pixels will be made fully transparent; when false
     *                        they will be made opaque.
     */
    public RemoveAlphaPixelTransform(boolean makeTransparent) {
        this.makeTransparent = makeTransparent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int apply(int rgb) {
        Color color = new Color(rgb, true);

        int alpha = color.getAlpha();
        if (alpha != 0 && alpha != 0xff) {
            alpha = makeTransparent ? 0x00 : 0xff;
        }

        // Adjust alpha preserving color channel
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
}
