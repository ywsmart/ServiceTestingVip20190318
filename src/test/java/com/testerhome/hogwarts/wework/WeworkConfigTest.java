package com.testerhome.hogwarts.wework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ywsmart
 * @date 2019-03-24
 */
class WeworkConfigTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void load() {
        WeworkConfig.load("");
    }

    @Test
    void getInstance(){
        WeworkConfig.getInstance();
    }
}