package org.antframework.configcenter.biz.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class WorkOrderDingdingUtils {

    /**
     * 超时时间
     */
    private static final int timeout = 10000;

    /**
     * 每个群开通的自定义机器人有webhook，后期可更换或写在配置文件作为参数传入
     */
    private static final String webhook = "https://oapi.dingtalk.com/robot/send?access_token=41f0e0c9b03c32be701fb3ec36001672485d78c41289d0b2df629021e7eed408";

    /**
     * 自定义关键词，安全设置，后期可更换或写在配置文件作为参数传入
     */
    private static final String CUSTOM_KEYWORDS = "蓝效";

    /**
     * 提示@所有人
     *
     * @param msg 通知消息
     */
    public static void sendMessageAtAll(String msg) {
        Map<String, Object> atMap = MapUtil.newHashMap();
        atMap.put("isAtAll", true);
        String reqString = getString(msg, atMap);
        HttpRequest.post(webhook).body(reqString).timeout(timeout).execute().body();
    }

    public static void main(String[] args) {
        WorkOrderDingdingUtils.sendMessageAtChosePerson("测试","18968080082");
    }
    /**
     * 提示@具体选择的某个人
     *
     * @param msg        通知消息
     */
    public static void sendMessageAtChosePerson(String msg, String mobile) {
        List<String> mobileList = Lists.newArrayList();
        mobileList.add(mobile);

        Map<String, Object> atMap = MapUtil.newHashMap();
        atMap.put("isAtAll", false);
        atMap.put("atMobiles", mobileList);
        String reqString = getString(msg, atMap);
        HttpRequest.post(webhook).body(reqString).timeout(timeout).execute().body();
    }

    private static String getString(String msg, Map<String, Object> atMap) {
        String content = CUSTOM_KEYWORDS +"："+ msg;
        Map<String, String> contentMap = MapUtil.newHashMap();
        contentMap.put("content", content);
        Map<String, Object> reqMap = MapUtil.newHashMap();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        String reqString = JSON.toJSONString(reqMap);
        return reqString;
    }

}