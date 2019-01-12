package com.defano.jmonet.tools.base;

import java.awt.*;

/**
 * An interface for determining if a given pixel constitutes a "mark" on a canvas. Used, for example, when determining
 * if the pencil tool should draw or erase (when starting on a marked pixel the pencil erases, when starting on an
 * unmarked or blank pixel, it draws).
 */
public interface MarkPredicate {

    boolean isMarked(Color pixel, Color eraseColor);
}
