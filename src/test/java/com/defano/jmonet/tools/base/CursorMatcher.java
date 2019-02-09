package com.defano.jmonet.tools.base;

import org.mockito.ArgumentMatcher;

import java.awt.*;

public class CursorMatcher extends ArgumentMatcher<Cursor> {

    private final Cursor c1;

    public CursorMatcher(Cursor c1) {
        this.c1 = c1;
    }

    @Override
    public boolean matches(Object o) {

        if (o instanceof Cursor) {
            Cursor c2 = (Cursor) o;

            return c2.getName().equals(c1.getName());
        }

        return false;
    }
}
