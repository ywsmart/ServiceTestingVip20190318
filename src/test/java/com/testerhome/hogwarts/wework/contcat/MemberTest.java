package com.testerhome.hogwarts.wework.contcat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ywsmart
 * @date 2019-03-20
 */
class MemberTest {

    static Member member;

    @BeforeAll
    static void setUp() {
        member = new Member();
    }

    // 参数化
    @ParameterizedTest
    @ValueSource(strings = {"yvan_", "hogwarts_", "testerhome_"})
    void create(String name) {
        String newName = name + member.random;
        String random = String.valueOf(System.currentTimeMillis()).substring(5 + 0, 5 + 8);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", newName);
        map.put("name", newName);
        map.put("department", Arrays.asList(1, 2));
        map.put("mobile", "151" + random);
        map.put("alias", newName);
        map.put("email", random + "@666.com");
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));
    }

    // 参数化CSV
    @ParameterizedTest
    @CsvFileSource(resources = "/data/com/testerhome/hogwarts/wework/contcat/member.csv")
    void create(String name,String alias) {
        String newName = name + member.random;
        String random = String.valueOf(System.currentTimeMillis()).substring(5 + 0, 5 + 8);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", newName);
        map.put("name", newName);
        map.put("department", Arrays.asList(1, 2));
        map.put("mobile", "151" + random);
        map.put("alias", alias);
        map.put("email", random + "@666.com");
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));
    }

    // 手动加入参数
    @Test
    void create1() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", "yvan_" + member.random);
        map.put("name", "yvan_" + member.random);
        map.put("department", Arrays.asList(1, 2));
        map.put("mobile", "151" + member.random.substring(0, 8));
        map.put("alias", "yvan_" + member.random);
        map.put("email", member.random.substring(0, 8) + "@666.com");
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void get() {
        member.get("yvan").then().statusCode(200).body("errcode", equalTo(0)).body("name", equalTo("Yvan"));
    }

    @Test
    void update() {
        String newName = "yvan_" + member.random;
        String random = String.valueOf(System.currentTimeMillis()).substring(5 + 0, 5 + 8);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", newName);
        map.put("name", newName);
        map.put("department", Arrays.asList(1, 2));
        map.put("mobile", "151" + member.random.substring(0, 8));
        map.put("alias", newName);
        map.put("email", member.random + "@666.com");
        // 先新建一个待更新的用户
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("userid", newName);
        map2.put("name", newName);
        map2.put("department", Arrays.asList(1, 2));
        map2.put("mobile", "151" + random);
        map2.put("alias", newName);
        map2.put("email", random + "@666.com");
        // 更新用户
        member.update(map2).then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void delete() {
        String name = "yvan_" + member.random;
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", name);
        map.put("name", name);
        map.put("department", Arrays.asList(1, 2));
        map.put("mobile", "151" + member.random.substring(0, 8));
        map.put("alias", name);
        map.put("email", member.random.substring(0, 8) + "@666.com");
        // 先创建一个成员
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));
        // 再删除该成员
        member.delete(name).then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void batchDelete() {
        String random = String.valueOf(System.currentTimeMillis()).substring(5 + 0, 5 + 8);
        String newName = "yvan_" + random;
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", newName);
        map.put("name", newName);
        map.put("department", Arrays.asList(1, 2));
        map.put("mobile", "151" + random);
        map.put("alias", newName);
        map.put("email", random + "@666.com");
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));

        String random2 = String.valueOf(System.currentTimeMillis()).substring(5 + 0, 5 + 8);
        String newName2 = "yvan_" + random2;
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("userid", newName2);
        map2.put("name", newName2);
        map2.put("department", Arrays.asList(1, 2));
        map2.put("mobile", "151" + random2);
        map2.put("alias", newName2);
        map2.put("email", random2 + "@666.com");
        member.create(map2).then().statusCode(200).body("errcode", equalTo(0));
        // 新建两个后批量删除
        List<String> useridList = new ArrayList<>();
        useridList.add(newName);
        useridList.add(newName2);
        member.batchDelete(useridList).then().statusCode(200).body("errcode", equalTo(0));

    }

    @Test
    void simpleList() {
        member.simpleList("1", "1").then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void list() {
        member.list("1", "1").then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void convertToOpenid1() {
        member.convertToOpenid1("yvan").then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void convertToOpenid2() {
        member.convertToOpenid2("oeCjy01W1_kurL_o7OFDVQQ70Gr0").then().statusCode(200).body("errcode", equalTo(40003));
    }

    @Test
    void invite() {
        List<String> userList = new ArrayList<>();
        userList.add("BenXiaoy");
        userList.add("xxxxxxxx");
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", userList);
        member.invite(map).then().statusCode(200).body("errcode", equalTo(0));
    }
}