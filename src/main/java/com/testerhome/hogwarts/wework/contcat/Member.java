package com.testerhome.hogwarts.wework.contcat;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;

/**
 * 成员业务
 *
 * @author ywsmart
 * @date 2019-03-18
 */
public class Member extends Contact {
    public Response create(HashMap<String, Object> map) {
        String body = template("/data/com/testerhome/hogwarts/wework/contcat/member.json", map);
        reset();
        return requestSpecification.body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/create")
                .then().log().all().extract().response();

    }

    public Response get(String userid) {
        reset();
        return requestSpecification.queryParam("userid", userid)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then().log().all().extract().response();
    }

    public Response update(HashMap<String, Object> map) {
        String body = template("/data/com/testerhome/hogwarts/wework/contcat/member.json", map);
        reset();
        return requestSpecification.body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/update")
                .then().log().all().extract().response();
    }

    public Response delete(String userid) {
        reset();
        return requestSpecification.queryParam("userid", userid)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/delete")
                .then().log().all().extract().response();
    }

    public Response batchDelete(List<String> useridList) {
        reset();
        HashMap<String, Object> map = new HashMap<>();
        map.put("useridlist", useridList);
        String body = template("/data/com/testerhome/hogwarts/wework/contcat/useridlist.json", map);
        return requestSpecification.body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete")
                .then().log().all().extract().response();
    }

    public Response simpleList(String department_id, String fetch_child){
        reset();
        return requestSpecification
                .queryParam("department_id",department_id)
                .queryParam("fetch_child",fetch_child)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist")
                .then().log().all().extract().response();
    }

}
