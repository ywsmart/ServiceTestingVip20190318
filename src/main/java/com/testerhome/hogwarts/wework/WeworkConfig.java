package com.testerhome.hogwarts.wework;

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
            weworkConfig = new WeworkConfig();
        }
        return weworkConfig;
    }

    /**
     * 从配置文件读参数
     *
     * @param path 文件地址
     */
    public static void load(String path){
        // todo: 后需改为read from yaml or json
    }
}
