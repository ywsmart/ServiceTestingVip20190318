package com.testerhome.hogwarts.wework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

/**
 * 发送引擎
 *
 * @author ywsmart
 * @date 2019-03-19
 */
public class Api {
    HashMap<String, Object> query = new HashMap<String, Object>();
    // 利用下面方法不需要这样初始化
//    public RequestSpecification requestSpecification = given();

    // 获取默认请求，即初始化
    public RequestSpecification getDefaultRequestSpecification() {
        return given().log().all();
    }

    // 用不到send方法，举例子
//    public Response send() {
//        // 根据all()的返回类型去定义接收的类型
//        requestSpecification = given().log().all();
//        query.entrySet().forEach(entry -> {
//            requestSpecification.queryParam(entry.getKey(), entry.getValue());
//        });
//        return requestSpecification.when().request("get", "baidu.com");
//    }

    // 模板读取。读取的json数据用map来改，放在父类，方便调用，或者放在util里也可以
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }

    /**
     * @param path   har文件路径
     * @param patten 匹配正则
     * @param map
     * @return
     */
    public Response templateFromHar(String path, String patten, HashMap<String, Object> map) {
        // todo: 支持从har文件读写接口定义并发送
        HarReader harReader = new HarReader();
        try {
            Har har = harReader.readFromFile(new File(getClass().getResource("/api/app.har.json").getPath()));
            HarRequest request = new HarRequest();
            for (HarEntry entry : har.getLog().getEntries()) {
                request = entry.getRequest();
                if (request.getUrl().matches(patten)) {
                    break;
                }
            }
            Restful restful = new Restful();
            restful.method = request.getMethod().name().toLowerCase();
            // todo: 待去掉url的query部分
            restful.url = request.getUrl();
            request.getQueryString().forEach(q -> {
                        restful.query.put(q.getName(), q.getValue());
                    }
            );
            restful.body = request.getPostData().getText();
        } catch (HarReaderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response templateFromSwagger(String path, String patten, HashMap<String, Object> map) {
        // todo: 支持从swagger自动生成接口定义并发送
        // todo: 分析swagger codegen
        // 从Swagger读取请求，//示意一下，未完成
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return getDefaultRequestSpecification().when().request(method, url);
    }

    public Restful getApiFromHar(String path, String patten) {
        HarReader harReader = new HarReader();
        try {
            Har har = harReader.readFromFile(new File(getClass().getResource(path).getPath()));
            HarRequest request = null;
            Boolean match = false;
            for (HarEntry entry : har.getLog().getEntries()) {
                request = entry.getRequest();
                if (request.getUrl().matches(patten)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                request = null;
                throw new Exception("正则未匹配到");
            }
            Restful restful = new Restful();
            restful.method = request.getMethod().toString();
            // todo: 待去掉url的query部分
            restful.url = request.getUrl();
            request.getQueryString().forEach(q -> {
                        restful.query.put(q.getName(), q.getValue());
                    }
            );
            restful.body = request.getPostData().getText();
            return restful;
        } catch (HarReaderException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拆读取方法，先读取api
     *
     * @param path yaml路径
     * @return
     */
    public Restful getApiFromYaml(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path), Restful.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Restful updateApiFromMap(Restful restful, HashMap<String, Object> map) {
        if (map==null) {
            return restful;
        }
        if (restful.method.toLowerCase().equals("get")) {
            map.entrySet().forEach(entry -> {
                restful.query.replace(entry.getKey(), entry.getValue().toString());
            });
        }
        if (restful.method.toLowerCase().contains("post")) {
            // 有body用body
            if (map.containsKey("_body")) {
                restful.body = map.get("_body").toString();
            }
            // 有file的用file
            if (map.containsKey("_file")) {
                String filePath = map.get("_file").toString();
                map.remove("_file");
                restful.body = template(filePath, map);
            }
        }
        return restful;
    }

    public Response getResponseFromRestful(Restful restful) {
        RequestSpecification requestSpecification = getDefaultRequestSpecification();
        if (restful.query != null) {
            restful.query.entrySet().forEach(entry -> {
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }
        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }
        // todo: 后续在此可以编写多环境支持，替换url，更新host的header
        return requestSpecification.log().all()
                .request(restful.method, restful.url).then().log().all().extract().response();
    }

    public Response getResponseFromHar(String path, String pattern, HashMap<String, Object> map) {
        Restful restful = getApiFromHar(path, pattern);
        restful = updateApiFromMap(restful, map);
        return getResponseFromRestful(restful);
    }

    public Response getResponseFromYaml(String path, HashMap<String, Object> map) {
        // fixed: 根据yaml生成接口定义并发送
        Restful restful = getApiFromYaml(path);
        return getResponseFromRestful(updateApiFromMap(restful, map));
    }

    /**
     * 需拆分两部分，获取restful和更新restful
     *
     * @param path
     * @param map
     * @return
     */
    public Response templateFromYaml1(String path, HashMap<String, Object> map) {
        // fixed: 根据yaml生成接口定义并发送
        Restful restful = getApiFromYaml(path);
        if (restful.method.toLowerCase().equals("get")) {
            map.entrySet().forEach(entry -> {
                restful.query.replace(entry.getKey(), entry.getValue().toString());
            });
        }
        if (restful.method.toLowerCase().contains("post")) {
            // 有body用body
            if (map.containsKey("_body")) {
                restful.body = map.get("_body").toString();
            }
            // 有file的用file
            if (map.containsKey("_file")) {
                String filePath = map.get("_file").toString();
                map.remove("_file");
                restful.body = template(filePath, map);
            }
        }
        RequestSpecification requestSpecification = getDefaultRequestSpecification();
        if (restful.query != null) {
            restful.query.entrySet().forEach(entry -> {
//                requestSpecification = requestSpecification.queryParam(entry.getKey(), entry.getValue());
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }
        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }
        // todo: 后续在此可以编写多环境支持，替换url，更新host的header
        return requestSpecification.log().all()
                .request(restful.method, restful.url).then().log().all().extract().response();
    }
    // todo：支持wsdl soap

    public Response api(String path, HashMap<String, Object> map) {
        // todo: 动态调用各
        return null;
    }
}
