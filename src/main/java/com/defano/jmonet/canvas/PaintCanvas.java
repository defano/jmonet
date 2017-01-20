package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.surface.*;
import com.defano.jmonet.model.Provider;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A canvas that can be painted upon by the paint tools.
 */
public interface PaintCanvas extends ScalableScratchDrawable, Surface {
    void setVisible(boolean visible);
    boolean isVisible();

    void setCursor(Cursor cursor);
    Cursor getCursor();

    void addCanvasCommitObserver(CanvasCommitObserver observer);
    boolean removeCanvasCommitObserver(CanvasCommitObserver observer);

    void invalidateCanvas();

    void commit();
    void commit(ChangeSet changeSet);

    void setScrollPosition(double percentX, double percentY);

    void setScale(double scale);
    Provider<Double> getScaleProvider();

    void setGridSpacing(int grid);
    Provider<Integer> getGridSpacingProvider();

    void setCanvasImage(BufferedImage image);
    void setScratchImage(BufferedImage image);

    void clearScratch();

    void setSize(int width, int height);

    Rectangle getBounds();
}
