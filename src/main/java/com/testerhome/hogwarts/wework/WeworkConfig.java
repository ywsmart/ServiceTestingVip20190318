package com.testerhome.hogwarts.wework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

/**
 * 企业微信的配置
 *
 * @author ywsmart
 * @date 2019-03-18
 */
public class WeworkConfig {

    /**
     * 应用ID
     */
    public String agentId = "1000002";
    /**
     * 应用凭证密钥
     */
    public String secret = "N8ptBRKeohSskKd9HgC_M5iWWNyWgehC1zWXYWHlSTM";
    /**
     * 企业ID
     */
    public String corpid = "ww3bf68f063cb045c2";
    /**
     * 通讯录凭证秘钥
     */
    public String contactSecret = "grvjbJPQ21CoOWKoUgGmaBIsjQFrbbTz0kByOH99IUA";

    private static WeworkConfig weworkConfig;

    /**
     * 利用简单的单例来获取配置
     *
     * @return
     */
    public static WeworkConfig getInstance() {
        if (weworkConfig == null) {
//            weworkConfig = new WeworkConfig();
            // 不用new去给它参数配置
            weworkConfig = load("/conf/WeworkConfig.yaml");
            // 打印了实例对象
            System.out.println(weworkConfig);
            System.out.println(weworkConfig.agentId);
        }
        return weworkConfig;
    }

    /**
     * 从配置文件读参数，利用Jackson来解析yaml
     *
     * @param path 文件地址
     */
    public static WeworkConfig load(String path){
        // fixed: 后需改为read from yaml or json
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // 还可以读json
//        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
//            System.out.println(mapper.writeValueAsString(WeworkConfig.getInstance()));
            // 读json可以用此方法格式化输出
//            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(WeworkConfig.getInstance()));
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path), WeworkConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
