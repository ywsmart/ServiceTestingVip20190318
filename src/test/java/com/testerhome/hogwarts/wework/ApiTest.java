package com.testerhome.hogwarts.wework;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
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

    @Test
    void request(){
        RequestSpecification req = given();
        req.queryParam("id",1);
        req.queryParam("d","xxx");
        req.get("http://www.baidu.com").then().log().all();
    }
}