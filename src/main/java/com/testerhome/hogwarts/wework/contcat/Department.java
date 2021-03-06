package com.testerhome.hogwarts.wework.contcat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.testerhome.hogwarts.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * 部门业务
 *
 * @author ywsmart
 * @date 2019-03-18
 */
public class Department extends Contact {


    /**
     * 从yaml读取api参数，重构list方法
     *
     * @param id
     * @return
     */
    public Response list(String id) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id",id);
        return getResponseFromYaml("/api/list.yaml",map);
    }


    /**
     * 发送引擎封装后，优化代码
     *
     * @param id
     * @return
     */
    public Response list1(String id) {
        Response response = getDefaultRequestSpecification()
                .param("id", id)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
                .then().extract().response();
        return response;
    }

    /**
     * 对标获取部门列表的接口，获取的数据比较复杂，最好创建方法把Response存下来，再用方法读，临时返回Response，以后重构
     */
    public Response list2(String id) {
        return given().log().all()
                .param("access_token", Wework.getToken())
                .param("id", id)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
                .then().log().all().statusCode(200).extract().response();
    }

    /**
     * 对标创建部门的接口，注意网页里开通通讯录同步功能及打开api编辑通讯录的权限
     * 临时字符串拼接可以，但是很臃肿，解决办法两个：
     * 模型化or模板化
     * 模型化是为每一个字段的post方法创建一个对象，创建一个对象去初始化它
     * 模板化是保存模板，改造模板，往模板里传参数，搞定参数的替换
     *
     * @return 创建部门接口的返回
     */
    public Response creat1(String name, String parentid) {
        return given().log().all()
                .queryParam("access_token", Wework.getToken())
                .body("{\n" +
                        "   \"name\": \"" + name + "\",\n" +
                        "   \"parentid\": " + parentid + ",\n" +
                        "   \"order\": 1,\n" +
//                        "   \"id\": \"\"\n" +
                        "}")
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().statusCode(200).extract().response();
    }

    /**
     * 创建部门请求的body模板化，读json，后放入即可
     *
     * @param name     部门名字
     * @param parentid 父类ID
     * @return 请求返回
     */
    public Response creat2(String name, String parentid) {
        // 利用JsonPath来读取json并替换参数，模板也需要对于包名
        String body = JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/com/testerhome/hogwarts/wework/contcat/create.json"))
                .set("$.name", name)
                .set("parentid", parentid)
                // 自带方法直接转jsonString
                .jsonString();
        return given().log().all().contentType(ContentType.JSON)
                .queryParam("access_token", Wework.getToken())
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().statusCode(200).extract().response();
    }

    /**
     * 发送引擎封装后，优化代码
     *
     * @param name     部门名字
     * @param parentid 父类ID
     * @return 请求返回
     */
    public Response create1(String name, String parentid) {
        String body = JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/com/testerhome/hogwarts/wework/contcat/create.json"))
                .set("$.name", name)
                .set("parentid", parentid)
                .jsonString();
        return getDefaultRequestSpecification()
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().extract().response();
    }

    /**
     * 优化代码，从yaml读取api文件
     * 通过hashmap来添加参数，模板里的非必填参数都可是null
     *
     * @return 请求的返回
     */
    public Response create(HashMap<String, Object> map){
        map.put("_file","/data/com/testerhome/hogwarts/wework/contcat/create.json");
        return getResponseFromYaml("/api/create.yaml",map);
    }

    /**
     * 通过hashmap来添加参数，模板里的非必填参数都可是null
     *
     * @return 请求的返回
     */
    public Response create1(HashMap<String, Object> map){
        DocumentContext documentContext = JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/com/testerhome/hogwarts/wework/contcat/create.json"));
        map.entrySet().forEach(entry->{
            documentContext.set(entry.getKey(),entry.getKey());
        });
        return getDefaultRequestSpecification()
                .body(documentContext.jsonString())
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().extract().response();
    }

    /**
     * 创建部门请求的body模板化，读json，后放入即可
     *
     * @param name     部门名字
     * @param parentid 父类ID
     * @param id       部门id
     * @return 请求返回
     */
    public Response create(String name, String parentid, String id) {
        // 利用JsonPath来读取json并替换参数，模板也需要对于包名
        String body = JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/com/testerhome/hogwarts/wework/contcat/create.json"))
                .set("$.name", name)
                .set("parentid", parentid)
                .set("id", id)
                // 自带方法直接转jsonString
                .jsonString();
        return given().log().all()
                .queryParam("access_token", Wework.getToken())
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().statusCode(200).extract().response();
    }

    /**
     * 优化代码，从yaml读取api文件
     *
     * @return 请求的返回
     */
    public Response create(String name,String parentid){
        // 让用例很清晰
        HashMap<String, Object> map = new HashMap<>();
        // 一步到位指定，省去下列两行代码
        map.put("_file","/data/com/testerhome/hogwarts/wework/contcat/create.json");
        map.put("name",name);
        map.put("parentid",parentid);
        // 读数据
//        String body = template("/data/com/testerhome/hogwarts/wework/contcat/create.json",map);
//        map.put("_body",body);
        return getResponseFromYaml("/api/create.yaml",map)
        ;
    }

    /**
     * 更新部门请求对应方法
     *
     * @param id   必填参数部门id
     * @param name 更新的name
     * @return 请求返回
     */
    public Response update2(String id, String name) {
        // 利用JsonPath来读取json并替换参数，模板也需要对于包名
        String body = JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/com/testerhome/hogwarts/wework/contcat/update.json"))
                .set("$.id", id)
                .set("name", name)
                // 自带方法直接转jsonString
                .jsonString();
        return given().log().all()
                .queryParam("access_token", Wework.getToken())
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then().log().all().statusCode(200).extract().response();

    }

    /**
     * 更新
     *
     * @param id   必填参数部门id
     * @param name 更新的name
     * @return 请求返回
     */
    public Response update1(String id, String name) {
        String body = JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/com/testerhome/hogwarts/wework/contcat/update.json"))
                .set("$.id", id)
                .set("name", name)
                .jsonString();
        return getDefaultRequestSpecification()
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then().extract().response();

    }
    /**
     * 更新,优化代码,从yaml读取api文件
     *
     * @param id   必填参数部门id
     * @param name 更新的name
     * @return 请求返回
     */
    public Response update(String id, String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("_file","/data/com/testerhome/hogwarts/wework/contcat/update.json");
        map.put("name",name);
        map.put("id",id);
        return getResponseFromYaml("/api/update.yaml",map);
    }

    // 待完善，重构update，根据抓包导出的har文件
    public Response update(HashMap<String, Object> map){
        // 暂时先写死接口，后面需要从文件读出来
        // todo：
       return templateFromHar("dome.har.json",
                "https://work.weixin.qq.com/wework_admin/party?action=addparty",
               map);
    }

    /**
     * 删除部门
     *
     * @param id 需删除的部门ID
     * @return 请求返回
     */
    public Response delete2(String id) {
        return given().log().all()
                .queryParam("access_token", Wework.getToken())
                .queryParam("id", id)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then().log().all().statusCode(200).extract().response();

    }
    /**
     * 删除部门，引擎封装后优化代码
     *
     * @param id 需删除的部门ID
     * @return 请求返回
     */
    public Response delete1(String id) {
        return getDefaultRequestSpecification()
                .queryParam("id", id)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then().log().all()
                .extract().response();

    }
    /**
     * 删除部门，优化代码,从yaml读取api文件
     *
     * @param id 需删除的部门ID
     * @return 请求返回
     */
    public Response delete(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        return getResponseFromYaml("/api/delete.yaml",map);
    }

    // 清理脏数据专用
    public void deleteAll(){
        List<Integer> idList = list("").then().extract().path("department.id");
        System.out.println(idList);
        idList.forEach(id->delete(id.toString()));
    }
    public Response updatrAll(HashMap<String ,Object> map){
        // todo: 待完善
        return api("api.json",map);
    }
}
