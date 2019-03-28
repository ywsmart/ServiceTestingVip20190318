package com.testerhome.hogwarts.wework;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static io.restassured.RestAssured.given;

/**
 * @author ywsmart
 * @date 2019-03-25
 */
class ApiTest {

    @Test
    void templateFromYaml() {
        Api api = new Api();
        api.getResponseFromYaml("/api/list.yaml",null).then().statusCode(200);
    }

    @Test
    void request(){
        RequestSpecification req = given();
        req.queryParam("id",1);
        req.queryParam("d","xxx");
        req.get("http://www.baidu.com").then().log().all();
    }

    @Test
    void resource(){
        URL url = getClass().getResource("/api/app.har.json");
        System.out.println(url.getFile());
        System.out.println(url.getPath());
    }

    @Test
    void getApiFromHar() {
        Api api = new Api();
        System.out.println(api.getApiFromHar("/api/app.har.json", ".*tid=41.*").url);
        System.out.println(api.getApiFromHar("/api/app.har.json", ".*tid=67.*").url);
        System.out.println(api.getApiFromHar("/api/app.har.json", ".*tid=99.*").url);
    }

    /**
     * 测试matches
     */
    @Test
    void matches(){
        String s = "https://work.weixin.qq.com/api/devtools/devhandler.php?tid=41&access_token=1ZgHhW5b9TXU2rBz9XDc9Fefi_rtGtgbT4x_0xzWCze1EBg7_IkdSImI3TTyDyz_nKwA6jja5NAnX6fLfuwY1Oe6XumBC7jf4V6EYrN9vZD4OGPualaa_s4dMRII4mUNOdf2wNZau23P_eBJ47SVivedgMfvvWNMKvki36veRCQggIyGUdGJXr2TolnL3JeuKAkWfgWhe231i6WE6SlwCA&agentid=1&f=json";
        System.out.println(s.matches(".*tid=41.*"));
    }

    @Test
    void getResponseFromHar() {
        Api api=new Api();
        api.getResponseFromHar("/api/app.har.json",".*tid=67.*",null);
    }
}