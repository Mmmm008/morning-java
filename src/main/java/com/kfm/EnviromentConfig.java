package com.kfm;

import java.util.HashMap;
import java.util.Map;

public class EnviromentConfig {

    private static final Map<String, String> configs = new HashMap<>();

    static {
        // 本地测试
//        configs.put("start_date", "2022-08-22");
//        configs.put("city", "西安");
//        configs.put("birthday", "09-22");
//        configs.put("app_id", "{{yourAppId}}");
//        configs.put("app_secret", "{{yourAppSecret}}");
//        configs.put("user_id", "{{yourUserId}}");
//        configs.put("template_id", "{{yourTemplateId}}");
        // 读取系统变量
        configs.put("start_date", System.getenv("START_DATE"));
        configs.put("city", System.getenv("CITY"));
        configs.put("birthday", System.getenv("BIRTHDAY"));
        configs.put("app_id", System.getenv("APP_ID"));
        configs.put("app_secret", System.getenv("APP_SECRET"));
        configs.put("user_id", System.getenv("USER_ID"));
        configs.put("template_id", System.getenv("TEMPLATE_ID"));
    }

    public static Map<String, String> getConfigs() {
        return configs;
    }

}
