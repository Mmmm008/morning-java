package com.kfm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class WeChatUtil {

    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String TEMPLATE_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public static String getToken(String appId, String secret){
        String response = HttpRequest.get(TOKEN_URL)
                .form("grant_type", "client_credential")
                .form("appid", appId)
                .form("secret", secret)
                .timeout(20000)//超时，毫秒
                .execute().body();
        JSONObject json = JSONUtil.parseObj(response);
        if (json.getStr("errcode") == null){
            return json.getStr("access_token");
        }
        throw new RuntimeException("获取微信TOKEN错误， 错误码为：" + json.getStr("errcode"));
    }

    public static Integer sendTemplate(String token, String userId, String templateId, JSONObject data){
        if (ObjectUtil.isNull(token)){
            return 40002;
        }
        JSONObject params = new JSONObject();
        params.putOpt("touser", userId);
        params.putOpt("template_id", templateId);
        params.putOpt("data", data);
        HttpRequest request = HttpRequest.post(TEMPLATE_URL_PREFIX + token)
                .body(params.toString());
        HttpResponse response = request.execute();
        JSONObject jsonObject = JSONUtil.parseObj(response);
        if (response.isOk()){
            return jsonObject.getInt("status");
        }
        return jsonObject.getInt("errcode");

    }


    public static void main(String[] args) {
        String appId = "wx50d8ce677b17e5e9";
        String secret = "c54825b060b6243f53db92ccb780b52c";
        System.out.println(getToken(appId, secret));
    }
}
