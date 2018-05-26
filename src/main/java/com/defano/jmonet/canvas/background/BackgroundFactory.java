package com.defano.jmonet.canvas.background;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackgroundFactory {

    public static Paint panelColor() {
        Color panel = (Color) UIManager.getLookAndFeelDefaults().get("Panel.background");
        return panel == null ? Color.WHITE : panel;
    }

    public static Paint makeTransparent() {
        return new Color(0, 0, 0, 0);
    }

    public static Paint makeCheckerboard(int checkSize) {
        return makeCheckerboard(checkSize, Color.WHITE, Color.LIGHT_GRAY);
    }

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