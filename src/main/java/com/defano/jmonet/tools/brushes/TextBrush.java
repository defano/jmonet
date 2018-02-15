package com.defano.jmonet.tools.brushes;

import javax.swing.*;
import java.awt.*;

/**
 * A brush whose shape is a user-provided string of text. Produces a stroke where every point on the stroked path is
 * "stamped" with text.
 */
public class TextBrush extends ShapeBrush {

    /**
     * Produces a TextBrush of a specified string using the system default font.
     * @param text The text representing the shape of the brush.
     */
    public TextBrush(String text) {
        super(new JLabel().getFont().createGlyphVector(new JLabel().getFontMetrics(new JLabel().getFont()).getFontRenderContext(), text).getOutline(), true);
    }

    /**
     * Produces a TextBrush of a specified string using a given font.
     * @param font The font in which the text should be rendered.
     * @param text The text representing the shape of the brush.
     */
    public TextBrush(Font font, String text) {
        super(font.createGlyphVector(new JLabel().getFontMetrics(font).getFontRenderContext(), text).getOutline(), true);
    }
}
