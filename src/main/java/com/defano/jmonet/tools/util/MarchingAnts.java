package com.defano.jmonet.tools.util;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A utility class for animating a dashed border stroke ("marching ants") commonly found in selection tools.
 */
public class MarchingAnts {

    private static final int ANIMATION_PERIOD_MS = 50;
    private static final MarchingAnts instance = new MarchingAnts();
    private static final ScheduledExecutorService antsAnimator = Executors.newSingleThreadScheduledExecutor();
    private static final Set<MarchingAntsObserver> observers = new HashSet<>();

    private int antsPhase;
    private Future antsAnimation;

    private MarchingAnts() {
    }

    public static MarchingAnts getInstance() {
        return instance;
    }

    /**
     * Gets a {@link Stroke} representing the current animated phase of the marching ants selection outline.
     *
     * @return The marching ants paint stroke.
     */
    public Stroke getMarchingAnts() {
        return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[]{5.0f}, antsPhase);
    }

    /**
     * Adds a marching ants observer; observers are called on the dispatch thread approximately once every 50ms to
     * indicate they should redraw their ants using the stroke provided by {@link #getMarchingAnts()}.
     * @param observer The observer to add
     */
    public void addObserver(MarchingAntsObserver observer) {
        observers.add(observer);
        startMarching();
    }

    /**
     * Removes a marching ants observer.
     * @param observer The observer to remove.
     */
    public void removeObserver(MarchingAntsObserver observer) {
        observers.remove(observer);
        if (observers.isEmpty()) {
            stopMarching();
        }
    }

    private void startMarching() {
        stopMarching();

        antsAnimation = antsAnimator.scheduleAtFixedRate(() -> SwingUtilities.invokeLater(() -> {
            antsPhase = antsPhase + 1 % 5;
            fireMarchingAntsObservers();
        }), 0, ANIMATION_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    private void stopMarching() {
        if (antsAnimation != null) {
            antsAnimation.cancel(false);
        }
    }

    private void fireMarchingAntsObservers() {
        SwingUtilities.invokeLater(() -> {
            Stroke ants = getMarchingAnts();
            for (Object thisObserver : observers.toArray()) {
                ((MarchingAntsObserver) thisObserver).onAntsMoved(ants);
            }
        });
    }
}
