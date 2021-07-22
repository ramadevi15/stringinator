package com.comcast.stringinator;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class BaseTest {

    @BeforeEach
    public void init_mocks() {
        MockitoAnnotations.openMocks(this);
    }
}
