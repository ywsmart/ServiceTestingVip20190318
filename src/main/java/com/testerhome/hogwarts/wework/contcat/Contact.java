package com.testerhome.hogwarts.wework.contcat;

import com.testerhome.hogwarts.wework.Restful;
import com.testerhome.hogwarts.wework.Wework;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

/**
 * 通讯录的父类初始化发送
 *
 * @author ywsmart
 * @date 2019-03-19
 */
public class Contact extends Restful {
    String random = String.valueOf(System.currentTimeMillis());

    // 初始化就自带log和token
    public Contact() {
        reset();
    }

    // 用了requestSpecification之后参数残留需要重置，不是最好的封装
    public void reset() {

        requestSpecification = given()
                .log().all()
                .queryParam("access_token", Wework.getToken())
                .contentType(ContentType.JSON)
        //先不断言了
//                .expect().log().all().statusCode(200)
        ;
    }
}
