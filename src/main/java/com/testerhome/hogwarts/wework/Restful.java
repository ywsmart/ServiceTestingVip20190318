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

    // 模板读取。读取的json数据用map来改，放在父类，方便调用，或者放在util里也可以
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Restful.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }

    public Response templateFromHar( String path, String patten, HashMap<String, Object> map){
        // todo: 支持从har文件读写接口定义并发送
        // 从har读取请求，进行更新
        DocumentContext documentContext = JsonPath.parse(Restful.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });

        //示意一下，未完成
        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return requestSpecification.when().request(method,url);
    }

    public Response templateFromSwagger( String path, String patten, HashMap<String, Object> map){
        // todo: 支持从swagger自动生成接口定义并发送
        // 从Swagger读取请求，//示意一下，未完成
        DocumentContext documentContext = JsonPath.parse(Restful.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return requestSpecification.when().request(method,url);
    }

    public Response templateFromYaml(String path,HashMap<String ,Object> map){
        // todo: 根据yaml生成接口定义并发送
        return null;
    }

    // todo：支持wsdl soap

    public Response api(String path, HashMap<String,Object> map){
        // todo: 动态调用各
        return null;
    }
}
