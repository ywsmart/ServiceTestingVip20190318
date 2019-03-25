package com.testerhome.hogwarts.wework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ywsmart
 * @date 2019-03-25
 */
class ApiTest {

    @Test
    void templateFromYaml() {
        Api api = new Api();
        api.templateFromYaml("/api/list.yaml",null).then().statusCode(200);
    }
}