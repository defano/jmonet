package com.defano.jmonet.tools.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeometryTest {

    @Test
    void testNearestFloor() {
        assertEquals(30, Geometry.nearestFloor(30, 10));
        assertEquals(30, Geometry.nearestFloor(31, 10));
        assertEquals(30, Geometry.nearestFloor(35, 10));
        assertEquals(40, Geometry.nearestFloor(40, 10));
    }

    @Test
    void testThatNearestFloorIgnoresZero() {
        assertEquals(13, Geometry.nearestFloor(13, 0));
        assertEquals(13, Geometry.nearestFloor(13, 1));
    }

    @Test
    void testThatNearestFloorHandlesNegatives() {
        assertEquals(-10, Geometry.nearestFloor(-15, 10));
        assertEquals(-10, Geometry.nearestFloor(-15, -10));
    }
}