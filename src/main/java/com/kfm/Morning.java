package com.kfm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Random;

public class Morning {

    private static Map<String, String> configs = EnviromentConfig.getConfigs();

    public void send(){
        String client = WeChatUtil.getToken(configs.get("app_id"), configs.get("app_secret"));
        String templateId = configs.get("template_id");
        try{
            int count = 0;
            String[] users = configs.get("user_id").split("\n");
            String data = initData();
            for (String userId : users){
                int code = WeChatUtil.sendTemplate(client, userId, templateId, data);
                System.out.println(userId + " : " + code);
                count ++;
            }
            System.out.println("发送了" + count + "条消息");
        } catch (Exception e){
            e.printStackTrace();
            System.err.printf("微信端返回错误：%s", e.getMessage());
        }


    }

    private String initData(){

        JSONObject weather = getWeather(configs.get("city"));
        String city = configs.get("city");

        JSONObject entries = new JSONObject();
        entries.putOpt("city", new ValueDO(city, getRandomColor()));
        entries.putOpt("date", new ValueDO(today(), getRandomColor()));
        entries.putOpt("weather", new ValueDO(getWeather(city).getStr("weather"), getRandomColor()));
        entries.putOpt("temperature", new ValueDO(String.valueOf(Math.floor(weather.getDouble("temp"))), getRandomColor()));
        entries.putOpt("highest", new ValueDO(String.valueOf(Math.floor(weather.getDouble("high"))), getRandomColor()));
        entries.putOpt("lowest", new ValueDO(String.valueOf(Math.floor(weather.getDouble("low"))), getRandomColor()));
        entries.putOpt("love_days", new ValueDO(String.valueOf(getMemorialDaysCount(configs.get("start_date"))), getRandomColor()));
        entries.putOpt("birthday_left", new ValueDO(String.valueOf(getBirthdayLeft(configs.get("birthday"))), getRandomColor()));
        entries.putOpt("words", new ValueDO(getWords(), getRandomColor()));

        return entries.toString();
    }

    // weather 直接返回对象，在使用的地方用字段进行调用。
    public JSONObject getWeather(String city){
        if (ObjectUtil.isNull(city)){
            System.err.println("请设置城市");
            return null;
        }
        String url = "http://autodev.openspeech.cn/csp/api/v2.1/weather?openId=aiuicus&clientType=android&sign=android&city=" + city;
        String result = HttpRequest.get(url).execute().body();
        if (ObjectUtil.isNull(result)){
            return null;
        }
        JSONObject res = JSONUtil.parseObj(result);
        JSONObject weather = res.getJSONObject("data").getJSONArray("list").getJSONObject(0);
        return weather;
    }

    //纪念日正数
    public long getMemorialDaysCount(String startDate){
        if (ObjectUtil.isNull(startDate)){
            System.err.println("没有设置 START_DATE");
            return 0;
        }
        LocalDate now = LocalDate.now();
        LocalDate start = LocalDate.parse(startDate);
        return start.until(now, ChronoUnit.DAYS);
    }

    //生日倒计时
    public long getBirthdayLeft(String birthday){
        if (ObjectUtil.isNull(birthday)){
            System.err.println("没有设置 BIRTHDAY");
            return 0;
        }
        LocalDate now = LocalDate.now();
        String date = now.getYear() + "-" + birthday;
        LocalDate next = LocalDate.parse(date);
        if (now.isAfter(next)){
            return next.until(now, ChronoUnit.DAYS);
        } else {
            return now.until(next.plusYears(1), ChronoUnit.DAYS);
        }
    }
    //彩虹屁 接口不稳定，所以失败的话会重新调用，直到成功
    public String getWords(){
        HttpResponse response = HttpRequest.get("https://api.shadiao.pro/chp").execute();
        if (!response.isOk()){
            return getWords();
        }
        return JSONUtil.parseObj(response.body()).getJSONObject("data").getStr("text");
    }

    private Random random = new Random();
    // 随机颜色
    public String getRandomColor(){
        return "#" + Integer.toHexString(random.nextInt(0, 0xFFFFFF));
    }

    public String today(){
        LocalDate date = LocalDate.now();
        return date.getYear() + "年" + date.getMonth().getValue() + "月" + date.getDayOfMonth();
    }


    public static void main(String[] args) {
        Morning morning = new Morning();
        System.out.println(morning.initData());
    }

}


class ValueDO {
    private String value;

    private String color;

    public ValueDO(String value, String color){
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
