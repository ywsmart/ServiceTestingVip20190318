package com.testerhome.hogwarts.wework.devtools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ywsmart
 * @date 2019-03-28
 */
class AppTest {

    @Test
    void listApp() {
        App app = new App();
        app.listApp().then().statusCode(200);
    }
}