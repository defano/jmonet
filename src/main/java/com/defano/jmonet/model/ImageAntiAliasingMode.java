package com.defano.jmonet.model;

/**
 * An enumeration of anti-aliasing rendering modes.
 */
public enum ImageAntiAliasingMode {

    /**
     * Specifies that no anti-aliasing should be used with this tool.
     */
    OFF,

    /**
     * Specifies the platform-default anti-aliasing mode should be used.
     */
    DEFAULT,

    /**
     * Specifies that anti-aliasing should be used with the nearest neighbor interpolation algorithm.
     */
    NEAREST_NEIGHBOR,

    /**
     * Specifies that anti-aliasing should be used with the bi-cubic interpolation algorithm.
     */
    BICUBIC,

    /**
     * Specifies that anti-aliasing should be used with the bi-linear interpolation algorithm.
     */
    BILINEAR
}
