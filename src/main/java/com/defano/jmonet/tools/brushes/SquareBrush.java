package com.defano.jmonet.tools.brushes;

public class SquareBrush extends RectangleBrush {

    /**
     * Produces a square brush of a given size.
     * @param size The length, in pixels, of each edge of the square
     */
    public SquareBrush(int size) {
        super(size, size);
    }
}
