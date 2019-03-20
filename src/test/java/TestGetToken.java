import com.testerhome.hogwarts.wework.Wework;
import com.testerhome.hogwarts.wework.WeworkConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * @author ywsmart
 * @date 2019-03-18
 */
public class TestGetToken {
    /**
     * 简单用例请求Token
     */
    @Test
    void testToken(){
        given().log().all()
                .queryParam("corpid","ww3bf68f063cb045c2")
                .queryParam("corpsecret","N8ptBRKeohSskKd9HgC_M5iWWNyWgehC1zWXYWHlSTM")
            .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
            .then().log().all()
                .statusCode(200).body("errcode",equalTo(0));
    }
    /**
     * 简单用例请求Token，使用配置类的参数
     */
    @Test
    void testToken2(){
        given().log().all()
                .queryParam("corpid", WeworkConfig.getInstance().corpid)
                .queryParam("corpsecret",WeworkConfig.getInstance().secret)
            .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
            .then().log().all()
                .statusCode(200).body("errcode",equalTo(0));
    }
    /**
     * 封装Token请求后的调用，代码清爽
     */
    @Test
    void testToken3(){
        Wework wework = new Wework();
        String token = wework.getWeworkToken(WeworkConfig.getInstance().secret);
        // 简单断言不为空
        assertThat(token, not(equalTo(null)));
    }

    @Test
    void createDepartment(){
        given().log().all().queryParam("access_token",Wework.getToken())
                // 需先申明是JSON，会自动声明是UTF-8编码
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"name\": \"广州研发中心\",\n" +
                        "  \"parentid\": 1,\n" +
                        "  \"order\": 1,\n" +
                        "  \"id\": null\n" +
                        "}")
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().statusCode(200).body("errcode",equalTo(0));
    }
}
