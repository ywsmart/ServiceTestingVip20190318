package com.testerhome.hogwarts.wework.devtools;

import com.testerhome.hogwarts.wework.Api;
import com.testerhome.hogwarts.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author ywsmart
 * @date 2019-03-28
 */
public class App extends Api {
    @Override
    public RequestSpecification getDefaultRequestSpecification() {
        RequestSpecification requestSpecification = super.getDefaultRequestSpecification();
        requestSpecification.queryParam("access_token", Wework.getToken())
                .contentType(ContentType.JSON);
        requestSpecification.filter((req, res, ctx) -> {
            // todo: 对请求 响应做封装
//            req.queryParam("")
            return ctx.next(req, res);
        });
        return requestSpecification;
    }

    public Response listApp(){
        return getResponseFromHar("/api/app.har.json",".*tid=67.*",null);
    }
}
