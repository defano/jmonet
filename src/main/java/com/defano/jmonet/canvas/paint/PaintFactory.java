package com.defano.jmonet.canvas.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A utility for producing {@link Paint} in commonly-needed colors and textures.
 */
@SuppressWarnings("unused")
public class PaintFactory {

    private PaintFactory() {}

    /**
     * Produces {@link Paint} of the same background color of a {@link JPanel} for the current look and feel, or
     * {@link Color#WHITE} if no such color is defined.
     *
     * @return Paint matching the panel background color.
     */
    public static Paint makePanelColor() {
        Color panel = (Color) UIManager.getLookAndFeelDefaults().get("Panel.background");
        return panel == null ? Color.WHITE : panel;
    }

    /**
     * Produces {@link Paint} that's fully transparent (otherwise black).
     *
     * @return Fully transparent paint.
     */
    public static Paint makeTransparent() {
        return new Color(0, 0, 0, 0);
    }

    /**
     * Produces a light-grey and white checkerboard paint pattern, often used as a canvas background to denote areas of
     * an image that are transparent.
     *
     * @param checkSize The square dimension of the checks, in pixels.
     * @return Checkerboard paint
     */
    public static Paint makeCheckerboard(int checkSize) {
        return makeCheckerboard(checkSize, Color.WHITE, Color.LIGHT_GRAY);
    }

    /**
     * Produces a checkerboard paint pattern of specified size and color.
     *
     * @param checkSize The square dimension of the checks, in pixels
     * @param c1        First check's color
     * @param c2        Second check's color
     * @return Checkerboard paint
     */
    public static Paint makeCheckerboard(int checkSize, Color c1, Color c2) {
        BufferedImage checks = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = checks.createGraphics();

        // Fill the field
        g.setPaint(c1);
        g.fillRect(0, 0, 2, 2);

        // Fill the checks
        g.setPaint(c2);
        g.fillRect(0, 0, 1, 1);
        g.fillRect(1, 1, 2, 2);
        g.dispose();

        return new TexturePaint(checks, new Rectangle(0, 0, checkSize * 2, checkSize * 2));
    }
}
