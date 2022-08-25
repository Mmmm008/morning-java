package com.kfm;

import java.util.HashMap;
import java.util.Map;

public class EnviromentConfig {

    private static final Map<String, String> configs = new HashMap<>();

    static {
//        configs.put("start_date", "2022-08-22");
//        configs.put("city", "西安");
//        configs.put("birthday", "09-22");
//        configs.put("app_id", "{{your_app_id}}");
//        configs.put("app_secret", "{{your_app_secret}}");
//        configs.put("user_id", "{{user_id}}");
//        configs.put("template_id", "kKeHFgVYCbOn6VzNqddmkk9vsi3CyyB1NtMj9dgpyPQ");
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
