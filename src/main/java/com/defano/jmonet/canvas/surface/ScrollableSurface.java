package com.defano.jmonet.canvas.surface;

import java.awt.*;

public interface ScrollableSurface {

    SurfaceScrollController getSurfaceScrollController();

    void setSurfaceScrollController(SurfaceScrollController surfaceScrollController);

    Point getScrollError();
}
