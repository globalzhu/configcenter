package org.antframework.configcenter.biz.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.info.topology.JanusNodeRequestDTO;
import org.antframework.configcenter.facade.info.topology.JanusRequestDTO;
import org.antframework.configcenter.facade.info.topology.JanusResponseDTO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author zongzheng
 * @date 2022/11/21 10:08 AM
 * @project configcenter
 */
public class JanusPostUtils {

    private static final String JANUS_ENDPOINT = "https://janus.glab.trustbe.cn/janus/invoke/v1";
    private static final String JANUS_TOKEN = "TZTpNQNoTLjiyf7JdZxUEtruw3HThunGPxpDGFnJZ7lOaft5s/MM1Y1KKaO8I+kI265F/hFyrkN+N+njGfGtlhZelK/4IBiSs5tsJXVKocWmFydP0gXxRMytm96r01aqcqR+CFpZq51RhfHiz6gsgk/MPdo0RnPuMeiFryKxd+XEWrK2kPZAlHhuWJn86oa4NWyYopmVclMkiBmn5w9z7DghY3lBkEIvc0+OKD83mgKc9hSHRzIpyWILWhob5NtKiyh+6qO/DsZ9VStAwJWAwHqP+wlacp976qgDlkUXYtaE+0td9nFoa+gekLalIj1ZUEolPDxFV4OWBGW7LWvA0w==";

    public static JanusResponseDTO doPostForGenerateCode(JanusNodeRequestDTO nodeDTO) throws Exception {
        Map<String,JanusNodeRequestDTO> requestMap = Maps.newHashMap();
        requestMap.put("node",nodeDTO);

        JanusRequestDTO<JanusNodeRequestDTO> requestDTO = new JanusRequestDTO<JanusNodeRequestDTO>();
        requestDTO.setContent(requestMap);
        requestDTO.setMethod("gaia.openapi.mesh.net.node.generate");

        String jsonResult = doPost(JSON.toJSONString(requestDTO));

        JanusResponseDTO responseDTO = JSON.parseObject(jsonResult,JanusResponseDTO.class);
        return responseDTO;

    }

    public static JanusResponseDTO doPostForGenerateLicense(String node_id, String inst_id, BigInteger max_mills) throws Exception {
        Map<String,Object> licenseDTO = Maps.newHashMap();
        licenseDTO.put("node_id",node_id);
        licenseDTO.put("inst_id",inst_id);
        licenseDTO.put("max_mills",max_mills);

        Map<String,Map> requestMap = Maps.newHashMap();
        requestMap.put("license",licenseDTO);

        JanusRequestDTO<Map> requestDTO = new JanusRequestDTO<Map>();
        requestDTO.setContent(requestMap);
        requestDTO.setMethod("gaia.openapi.mesh.license.generate");

        String jsonResult = doPost(JSON.toJSONString(requestDTO));

        JanusResponseDTO responseDTO = JSON.parseObject(jsonResult,JanusResponseDTO.class);
        return responseDTO;
    }

    /**
     * mid通过mesh节点详情查询
     * @param nodeId
     * @return
     * @throws Exception
     */
    public static JanusResponseDTO doPostForGetCode(String nodeId) throws Exception {
        Map<String,String> requestMap = Maps.newHashMap();
        requestMap.put("node", nodeId);

        JanusRequestDTO<String> requestDTO = new JanusRequestDTO<String>();
        requestDTO.setContent(requestMap);
        requestDTO.setMethod("gaia.openapi.mesh.net.node.get");

        String jsonResult = doPost(JSON.toJSONString(requestDTO));

        JanusResponseDTO responseDTO = JSON.parseObject(jsonResult, JanusResponseDTO.class);
        return responseDTO;

    }

    private static String doPost(String jsonString) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(JANUS_ENDPOINT);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("token", JANUS_TOKEN);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (200 == response.getStatusLine().getStatusCode()) {
                String responseJson = EntityUtils.toString(responseEntity);
                return responseJson;
            } else {
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                        String.format("HttpStatus[%s] fail", response.getStatusLine().getStatusCode()));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JanusResponseDTO result = doPostForGetCode("LX0000010002970");
        System.out.println(result);
    }
}
