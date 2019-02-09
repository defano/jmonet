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
 * A class for animating a dashed border stroke ("marching ants") commonly found in selection tools.
 *
 * This singleton class manages the ant animation by instantiating a single-threaded scheduled executor. Multiple
 * "ants" paths can be drawn with this singleton by registering multiple listeners.
 */
public class MarchingAnts {

    private static final MarchingAnts instance = new MarchingAnts();
    private static final ScheduledExecutorService antsAnimator = Executors.newSingleThreadScheduledExecutor();
    private static final Set<MarchingAntsObserver> observers = new HashSet<>();

    private int animationPeriodMs = 50;             // animation period
    private int antLength = 5;                      // ant dash length, in pixels
    private int antWidth = 1;                       // width of ant dash, in pixels
    private Color antColor = Color.DARK_GRAY;
    private Color pathColor = Color.WHITE;

    private int antsPhase;
    private Future antsAnimation;

    private MarchingAnts() {}

    public static MarchingAnts getInstance() {
        return instance;
    }

    /**
     * Gets a {@link Stroke} representing the current animated phase of the marching ants selection outline.
     *
     * @return The marching ants paint stroke.
     */
    public Stroke getMarchingAnts() {
        return new BasicStroke(
                antWidth,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                antLength,
                new float[]{antLength},
                antsPhase
        );
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

    /**
     * Gets the period of the ants animation, in milliseconds. That is, the number of milliseconds delayed before
     * redrawing the position of the ants. Lower values means faster moving ants.
     *
     * @return The period of the ants animation, in milliseconds.
     */
    @SuppressWarnings("unused")
    public int getAnimationPeriodMs() {
        return animationPeriodMs;
    }

    /**
     * Sets the period of the ants animation, in milliseconds. That is, the number of milliseconds delayed before
     * redrawing the position of the ants. Lower values means faster moving ants.
     *
     * @param animationPeriodMs The period of the ants animation, in milliseconds.
     */
    @SuppressWarnings("unused")
    public void setAnimationPeriodMs(int animationPeriodMs) {
        this.animationPeriodMs = animationPeriodMs;
    }

    /**
     * Gets the length of each ant, in pixels. Note that the length of the ant always is the same as the space between
     * ants.
     *
     * @return The length of each ant, in pixels.
     */
    @SuppressWarnings("unused")
    public int getAntLength() {
        return antLength;
    }

    /**
     * Sets the length of each ant, in pixels. Note that the length of the ant always is the same as the space between
     * ants.
     * @param antLength The length of each ant, in pixels.
     */
    @SuppressWarnings("unused")
    public void setAntLength(int antLength) {
        this.antLength = antLength;
    }

    /**
     * Gets the width of each ant, in pixels. Note that when stroking a selection outline with marching ants, the
     * ants are centered on the selected shape. Therefore, wide ants will partially obscure the selection being made.
     *
     * @return The width of each ant, in pixels.
     */
    @SuppressWarnings("unused")
    public int getAntWidth() {
        return antWidth;
    }

    /**
     * Sets the width of each ant, in pixels. Note that when stroking a selection outline with marching ants, the
     * ants are centered on the selected shape. Therefore, wide ants will partially obscure the selection being made.
     *
     * @param antWidth The width of each ant, in pixels.
     */
    @SuppressWarnings("unused")
    public void setAntWidth(int antWidth) {
        this.antWidth = antWidth;
    }

    /**
     * Returns the color of each ant.
     * @return The ant color
     */
    @SuppressWarnings("unused")
    public Color getAntColor() {
        return antColor;
    }

    /**
     * Sets the color of each ant.
     * @param antColor The ant color
     */
    @SuppressWarnings("unused")
    public void setAntColor(Color antColor) {
        this.antColor = antColor;
    }

    /**
     * Gets the color of space between ants (the ant path).
     * @return The ant path color.
     */
    @SuppressWarnings("unused")
    public Color getPathColor() {
        return pathColor;
    }

    /**
     * Sets the color of the space between ants.
     * @param pathColor The ant path color
     */
    @SuppressWarnings("unused")
    public void setPathColor(Color pathColor) {
        this.pathColor = pathColor;
    }

    private void startMarching() {
        stopMarching();

        antsAnimation = antsAnimator.scheduleAtFixedRate(() -> SwingUtilities.invokeLater(() -> {
            antsPhase = antsPhase + 1 % antLength;
            fireMarchingAntsObservers();
        }), 0, animationPeriodMs, TimeUnit.MILLISECONDS);
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
