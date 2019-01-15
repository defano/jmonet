package com.defano.jmonet.tools;

import com.defano.jmonet.tools.base.MockitoToolTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

public class ArrowToolTest extends MockitoToolTest<ArrowTool> {

    @BeforeEach
    public void setUp() {
        initialize(new ArrowTool());
    }

    @Test
    public void testDefaultCursor() {
        Mockito.verify(mockCursorManager).setToolCursor(argThat(matchesCursor(Cursor.getDefaultCursor())), eq(mockCanvas));
    }
}
