package com.defano.jmonet.canvas.surface;

import com.defano.jmonet.model.Provider;

public interface GridSurface {
    void setGridSpacing(int grid);
    Provider<Integer> getGridSpacingProvider();
}
