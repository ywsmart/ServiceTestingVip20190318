package com.testerhome.hogwarts.wework.contcat;

import com.testerhome.hogwarts.wework.Api;
import com.testerhome.hogwarts.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * 通讯录的父类初始化发送
 *
 * @author ywsmart
 * @date 2019-03-19
 */
public class Contact extends Api {
    String random = String.valueOf(System.currentTimeMillis());


    @Override
    public RequestSpecification getDefaultRequestSpecification(){
        RequestSpecification requestSpecification =super.getDefaultRequestSpecification();
        requestSpecification.queryParam("access_token",Wework.getToken())
                .contentType(ContentType.JSON);
        requestSpecification.filter((req,res,ctx)->{
            // todo: 对请求 响应做封装
//            req.queryParam("")
            return ctx.next(req,res);
        });
        return requestSpecification;
    }
}
