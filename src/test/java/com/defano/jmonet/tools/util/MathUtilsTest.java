package com.defano.jmonet.tools.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void testNearestFloor() {
        assertEquals(30, MathUtils.nearestFloor(30, 10));
        assertEquals(30, MathUtils.nearestFloor(31, 10));
        assertEquals(30, MathUtils.nearestFloor(35, 10));
        assertEquals(40, MathUtils.nearestFloor(40, 10));
    }

    @Test
    void testThatNearestFloorIgnoresZero() {
        assertEquals(13, MathUtils.nearestFloor(13, 0));
        assertEquals(13, MathUtils.nearestFloor(13, 1));
    }

    @Test
    void testThatNearestFloorHandlesNegatives() {
        assertEquals(-10, MathUtils.nearestFloor(-15, 10));
        assertEquals(-10, MathUtils.nearestFloor(-15, -10));
    }
}