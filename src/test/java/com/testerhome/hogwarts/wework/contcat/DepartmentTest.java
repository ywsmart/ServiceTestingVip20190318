package com.testerhome.hogwarts.wework.contcat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

/**
 * @author ywsmart
 * @date 2019-03-18
 */
class DepartmentTest {
    Department department;
    // 利用时间戳来制造唯一数，注意会留下脏数据，以后需要批量清理
    String random = String.valueOf(System.currentTimeMillis());

    @BeforeEach
    void setUp() {
        if (department == null) {
            department = new Department();
            department.deleteAll();
        }
    }

    @Test
    void list() {
        // 未传参数id
        department.list("").then().statusCode(200);
        // 传参数id
        department.list("1").then().statusCode(200).body("department.name[0]", equalTo("杭州一番"));
        // 传参数id，断言id为1
        department.list("1").then().statusCode(200).body("department.id[0]", equalTo(1));

    }

    @Test
    void creat() {
        department.create("YvanSB1", "1");
        // 断言添加相同部门会报错，状态后面也需要管理起来
        department.create("YvanSB1", "1").then().body("errcode", equalTo(60008));
    }

    @Test
    void createByMap() {
        HashMap<String, Object> map = new HashMap<String, Object>() {
            {
                put("name", "lalala" + random);
                put("parenid", "1");
            }
        };
        department.create(map).then().body("errcode", equalTo(0));
    }

    @Test
    void createWithChinese() {
        department.create("测试部门test" + random, "1").then().body("errcode", equalTo(0));
    }

    // 参数化加断言状态码
    @ParameterizedTest
    @CsvFileSource(resources = "/data/com/testerhome/hogwarts/wework/contcat/createWithDup.csv")
    void createWithDup(String name, Integer expectCode){
        department.create(name+random,"1").then().body("errcode",equalTo(0));
        department.create(name+random,"1").then().body("errcode",equalTo(expectCode));
    }

    @Test
    void update() {
        String nameOld = "testUpdate1" + random;
        department.create(nameOld, "1").then().body("errcode", equalTo(0));
        Integer integerId = department.list("").path("department.find{ it.name=='" + nameOld + "' }.id");
        String id = String.valueOf(integerId);
        department.update(id, "testUpdate2中午" + random).then().body("errcode", equalTo(0));
    }

    /**
     * 我写的用例，利用新建时给定特定ID后，利用特定ID更新部门
     */
    @Test
    void update2() {
        department.create("testUpdate" + random, "1", random).then().body("errcode", equalTo(0));
        department.update(random, "testUpdate" + random).then().body("errcode", equalTo(0));
        department.delete(random).then().body("errcode", equalTo(0));
    }

    @Test
    void delete() {
        String nameOld = "testDelete1" + random;
        department.create(nameOld, "1").then().body("errcode", equalTo(0));
        // 先转换Integer类型再转String，避免出错
        Integer integerId = department.list("").path("department.find{ it.name=='" + nameOld + "' }.id");
        String id = String.valueOf(integerId);
        department.delete(id).then().body("errcode", equalTo(0));
    }

    /**
     * 我写的用例，利用新建时给定特定ID后，利用特定ID删除部门
     */
    @Test
    void delete2() {
        department.create("testDelete" + random, "1", "99").then().body("errcode", equalTo(0));
        department.delete("99").then().body("errcode", equalTo(0));
    }

    // 简单示意动态调用，api.json里面写着对应的方法
    @Test
    void updateAll(){
        HashMap<String,Object> map=new HashMap<>();
        department.api("api.json",map).then().statusCode(200);
    }

    @Test
    void deleteAll(){
        department.deleteAll();
    }
}