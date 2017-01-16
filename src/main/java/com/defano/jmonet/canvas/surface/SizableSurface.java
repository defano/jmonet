package com.defano.jmonet.canvas.surface;

import java.awt.*;

public interface SizableSurface {
    void setSize(int width, int height);

    Rectangle getBounds();
    int getHeight();
    int getWidth();
}
