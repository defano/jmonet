package com.defano.jmonet.tools.base;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public abstract class MockitoTest {

    @BeforeEach
    protected void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
