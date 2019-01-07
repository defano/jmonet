package com.defano.jmonet.canvas.observable;

import com.defano.jmonet.canvas.layer.ScaledLayeredImage;

import java.awt.*;
import java.awt.event.*;

/**
 * An observer of mouse and keyboard events taking place on a {@link ScaledLayeredImage}.
 * Converts mouse events to the coordinate space of the image represented by the ScaledLayeredImage.
 */
@SuppressWarnings({"EmptyMethod", "unused"})
public interface SurfaceInteractionObserver extends KeyListener {

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on the canvas.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mouseClicked(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mousePressed(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mouseReleased(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mouseEntered(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mouseExited(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mouseDragged(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e The mouse event that occurred
     * @param imageLocation the location (relative to the image) where the event occurred
     */
    default void mouseMoved(MouseEvent e, Point imageLocation) {}

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     */
    default void keyTyped(KeyEvent e) {}

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     */
    default void keyPressed(KeyEvent e) {}

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    default void keyReleased(KeyEvent e) {}

}
