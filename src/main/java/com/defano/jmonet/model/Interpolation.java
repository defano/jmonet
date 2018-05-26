package com.defano.jmonet.model;

/**
 * An enumeration of interpolation algorithms.
 */
public enum Interpolation {

    /**
     * Specifies that no interpolation should be used.
     */
    NONE,

    /**
     * Specifies the platform-default interpolation mode should be used.
     */
    DEFAULT,

    /**
     * Specifies the nearest neighbor interpolation algorithm.
     */
    NEAREST_NEIGHBOR,

    /**
     * Specifies the bi-cubic interpolation algorithm.
     */
    BICUBIC,

    /**
     * Specifies the bi-linear interpolation algorithm.
     */
    BILINEAR
}
