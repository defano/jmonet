package com.defano.jmonet.canvas.surface;

import java.awt.image.BufferedImage;

public interface ScratchDrawableSurface {
    BufferedImage getCanvasImage();
    BufferedImage getScratchImage();

    void setCanvasImage(BufferedImage image);
    void setScratchImage(BufferedImage image);

    void clearScratch();
}
