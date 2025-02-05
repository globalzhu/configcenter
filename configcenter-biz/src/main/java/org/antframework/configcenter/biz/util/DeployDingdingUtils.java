package org.antframework.configcenter.biz.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DeployDingdingUtils {

    /**
     * 超时时间
     */
    private static final int timeout = 10000;

    /**
     * 每个群开通的自定义机器人有webhook，后期可更换或写在配置文件作为参数传入
     */
    private static final String webhook = "https://oapi.dingtalk.com/robot/send?access_token=c6242f63869da69991cda80861b6c150cd8631358c1d78016103f0f14bc856c1";

    /**
     * 自定义关键词，安全设置，后期可更换或写在配置文件作为参数传入
     */
    private static final String CUSTOM_KEYWORDS = "部署工单";

    private static Map<String,String> namePhoneMapping = Maps.newHashMap();
    static{
        namePhoneMapping.put("豆腐","15158008837");
        namePhoneMapping.put("折竹","13167209283");
        namePhoneMapping.put("旭升","13758163981");
        namePhoneMapping.put("含笑","15850284736");
        namePhoneMapping.put("县长","18519698007");
        namePhoneMapping.put("张豪","13683875164");
        namePhoneMapping.put("再锋","15267124426");
        namePhoneMapping.put("文景","13193670267");
    }

    public static Map<String,String> getDeployOwnerList(){
        return namePhoneMapping;
    }

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

    /**
     * 提示@具体选择的某个人
     *
     * @param msg        通知消息
     * @param peopleList 通知人列表
     */
    public static void sendMessageAtChosePerson(String msg, List<String> peopleList) {
        List<String> mobileList = Lists.newArrayList();
        if(peopleList != null){
            peopleList.stream().forEach(people ->{
                mobileList.add(namePhoneMapping.get(people));
            });
        }

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