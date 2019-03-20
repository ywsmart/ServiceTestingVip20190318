package com.testerhome.hogwarts.wework;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

/**
 * 发送引擎
 *
 * @author ywsmart
 * @date 2019-03-19
 */
public class Restful {
    HashMap<String, Object> query = new HashMap<String, Object>();
    public RequestSpecification requestSpecification = given();


    // 用不到send方法，举例子
    public Response send() {
        // 根据all()的返回类型去定义接收的类型
        requestSpecification = given().log().all();
        query.entrySet().forEach(entry -> {
            requestSpecification.queryParam(entry.getKey(), entry.getValue());
        });
        return requestSpecification.when().request("get", "baidu.com");
    }

    // 读取的json数据用map来改，放在父类，方便调用，或者放在util里也可以
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Restful.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }
}
