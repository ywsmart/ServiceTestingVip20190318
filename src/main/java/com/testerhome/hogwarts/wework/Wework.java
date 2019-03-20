package com.testerhome.hogwarts.wework;

import io.restassured.RestAssured;

/**
 * @author ywsmart
 * @date 2019-03-18
 */
public class Wework {

    public static String token;

    /**
     * 封装后的获取token请求
     *
     * @return token字符串
     */
    public static String getWeworkToken(String secret) {
        return RestAssured.given().log().all()
                .queryParam("corpid", WeworkConfig.getInstance().corpid)
                .queryParam("corpsecret", secret)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().log().all()
                .statusCode(200)
                .extract().path("access_token");
    }

    /**
     * 获取通讯录secret
     *
     * @return
     */
    public static String getWeworkTokenForContact() {
        return "";
    }

    /**
     * 使用已有的token值，避免重复调用
     *
     * @return token字符串
     */
    public static String getToken() {
        //todo: 后需改支持两种类型的token
        if (token == null) {
            token = getWeworkToken(WeworkConfig.getInstance().contactSecret);
        }
        return token;
    }
}
