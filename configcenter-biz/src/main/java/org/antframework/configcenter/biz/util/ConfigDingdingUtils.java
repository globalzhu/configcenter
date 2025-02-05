package org.antframework.configcenter.biz.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigDingdingUtils {

    /**
     * 超时时间
     */
    private static final int timeout = 10000;

    /**
     * 每个群开通的自定义机器人有webhook，后期可更换或写在配置文件作为参数传入
     */
    private static final String webhook = "https://oapi.dingtalk.com/robot/send?access_token=c6242f63869da69991cda80861b6c150cd8631358c1d78016103f0f14bc856c1";
    private static final String rhino_short_cc = "http://10.99.253.241:9999/api/short?act=cc";

    /**
     * 自定义关键词，安全设置，后期可更换或写在配置文件作为参数传入
     */
    private static final String CUSTOM_KEYWORDS = "mirror";

    private static Map<String,String> namePhoneMapping = Maps.newHashMap();
    static{
        namePhoneMapping.put("飘絮","13816213469");
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
    public static void sendMessageAtChosePerson(String msg, List<String> peopleList, List<String> imageList, String... info) {
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

        Map<String, Object> imageMap = MapUtil.newHashMap();
        imageMap.put("images", imageList);
        if (info.length == 2){
            imageMap.put("appBranch", info[0]);
            imageMap.put("managerName", info[1]);
        }
        String imageBody=JSON.toJSONString(imageMap);
        String responseStr = sendPOST(rhino_short_cc,imageBody);
        System.out.println(responseStr);
    }

    private static String sendPOST(String url,String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 创建HttpPost对象，并设置URL
            HttpPost httpPost = new HttpPost(rhino_short_cc);

            // 设置JSON数据
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");

            // 设置请求实体
            httpPost.setEntity(entity);

            // 执行请求
            org.apache.http.HttpResponse response = httpClient.execute(httpPost);

            // 打印响应状态
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

            // 读取服务器响应内容
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
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