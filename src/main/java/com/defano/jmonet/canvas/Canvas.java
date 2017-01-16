package com.defano.jmonet.canvas;

import com.defano.jmonet.canvas.surface.*;

import java.awt.*;

public interface Canvas extends SizableSurface, CompositeSurface, InteractiveSurface, ScalableGridSurface, ScratchDrawableSurface {
    boolean isVisible();

    void setCursor(Cursor cursor);

    void addObserver(CanvasCommitObserver observer);
    boolean removeObserver(CanvasCommitObserver observer);

    void invalidateCanvas();

    void commit();
    void commit(AlphaComposite composite);
}
