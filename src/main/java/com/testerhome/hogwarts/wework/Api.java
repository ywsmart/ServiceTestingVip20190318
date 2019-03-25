package com.testerhome.hogwarts.wework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;

/**
 * 发送引擎
 *
 * @author ywsmart
 * @date 2019-03-19
 */
public class Api {
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

    // 模板读取。读取的json数据用map来改，放在父类，方便调用，或者放在util里也可以
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }

    public Response templateFromHar(String path, String patten, HashMap<String, Object> map) {
        // todo: 支持从har文件读写接口定义并发送
        // 从har读取请求，进行更新
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });

        //示意一下，未完成
        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return requestSpecification.when().request(method, url);
    }

    public Response templateFromSwagger(String path, String patten, HashMap<String, Object> map) {
        // todo: 支持从swagger自动生成接口定义并发送
        // 从Swagger读取请求，//示意一下，未完成
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return requestSpecification.when().request(method, url);
    }

    public Response templateFromYaml(String path, HashMap<String, Object> map) {
        // fixed: 根据yaml生成接口定义并发送
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Restful restful = mapper.readValue(WeworkConfig.class.getResourceAsStream(path), Restful.class);
            if (restful.method.toLowerCase().equals("get")) {
                map.entrySet().forEach(entry -> {
                    restful.query.replace(entry.getKey(), entry.getValue().toString());
                });
            }
            restful.query.entrySet().forEach(entry -> {
                this.requestSpecification = this.requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
            return this.requestSpecification.log().all().request(restful.method, restful.url).then().log().all().extract().response();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // todo：支持wsdl soap

    public Response api(String path, HashMap<String, Object> map) {
        // todo: 动态调用各
        return null;
    }
}