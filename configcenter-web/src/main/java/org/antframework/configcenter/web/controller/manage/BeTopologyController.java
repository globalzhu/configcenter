package org.antframework.configcenter.web.controller.manage;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.DeployDingdingUtils;
import org.antframework.configcenter.biz.util.JanusPostUtils;
import org.antframework.configcenter.biz.util.WorkOrderDingdingUtils;
import org.antframework.configcenter.facade.api.topology.TopologyService;
import org.antframework.configcenter.facade.info.topology.*;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.order.topology.*;
import org.antframework.configcenter.facade.result.topology.*;
import org.antframework.configcenter.facade.vo.*;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.web.CurrentManagerAssert;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 节点拓扑
 *
 * @author zongzheng
 * @date 2022/10/20 3:59 PM
 * @project configcenter
 */
@RestController
@RequestMapping("/topology")
@AllArgsConstructor
public class BeTopologyController {

    private final TopologyService topologyService;

    private static final String JANUS_ENDPOINT = "http://10.99.51.32:7800/janus/invoke/v1";
    private static final String JANUS_TOKEN = "PhYNZFQrb5gLfUbHNtdn6DNcnZoo/ZQwM/OFDtOm41Cgi/YyJJ7R4Xz/868rU9DZRSnsQRNbeoGOa3/Oxl71m7KwdjbOL+FSReocXL7s+/KiC+oqeQ2UK+I28b7L3CxGObVLjmkIhEUgMntXkrhfAeLZTD+RJmzH2EwcwIkWQJ2sS+VOyb8Bpdwoa32QDn4dzoTyGl0c6QwC/rTzLL4f2MB4fmCLfHhZj7O+lVyWJn63YNUcxzyNGUEQdOvWIrVfeNHOjZaHiBhkh+NjoA0w2VytYSEPWJYqXDG8rEukYh/gJYdjbSNFe3xmLtmR699EHY0AtD9bdfjlSXjMUEibsA==";

    /**
     * 分页查找节点列表
     */
    @RequestMapping("/node/queryCustomerList")
    public QueryCustomerResult queryCustomerList(int pageNo, int pageSize, String customerCode,
                                          String customerName, CustomerStatus customerStatus) {
        QueryCustomerOrder order = new QueryCustomerOrder();
        order.setCustomerCode(customerCode);
        order.setCustomerName(customerName);
        order.setCustomerStatus(customerStatus);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryCustomerResult result = topologyService.queryCustomerList(order);
        return result;
    }


    /**
     * 分页查找节点列表
     */
    @RequestMapping("/node/queryNodeList")
    public QueryNodesResult queryNodeList(int pageNo,
                                          int pageSize,
                                          String nodeName,
                                          String nodeCode,
                                          ProductType productType,
                                          String productVersion,
                                          NodeStatus nodeStatus,
                                          String deployOwner,
                                          @DateTimeFormat(pattern="yyyy-MM-dd")Date deployStartTime,
                                          @DateTimeFormat(pattern="yyyy-MM-dd")Date deployEndTime) {
        QueryNodesOrder order = new QueryNodesOrder();
        order.setNodeName(nodeName);
        order.setNodeCode(nodeCode);
        order.setNodeStatus(nodeStatus);
        order.setProductType(productType);
        order.setDeployOwner(deployOwner);
        order.setDeployStartTime(deployStartTime);
        order.setDeployEndTime(deployEndTime);
        order.setProductVersion(productVersion);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryNodesResult result = topologyService.queryNodeList(order);
        if (result != null && result.getInfos() != null) {
            result.getInfos().stream().forEach(info -> {
                String customerId = info.getCustomerId();
                FindCustomerInfoOrder findCustomerInfoOrder = new FindCustomerInfoOrder();
                findCustomerInfoOrder.setCustomerId(customerId);
                FindCustomerInfoResult findCustomerInfoResult = topologyService.findCustomerInfo(findCustomerInfoOrder);
                if(findCustomerInfoResult.isSuccess()){
                    BeCustomerInfoDTO customerInfo = findCustomerInfoResult.getCustomerInfo();
                    if(customerInfo == null){
                        throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                                String.format("customer[%s] not exist", customerId));
                    }else{
                        info.setCustomerInfo(findCustomerInfoResult.getCustomerInfo());
                    }
                }else{
                    throw new BizException(findCustomerInfoResult.getStatus(), findCustomerInfoResult.getCode(),
                            findCustomerInfoResult.getMessage()+",customerId="+customerId);
                }
            });
        }
        return result;
    }

    /**
     * 分页查找组网列表
     */
    @RequestMapping("/link/queryLinkList")
    public QueryLinksResult queryLinkList(int pageNo,
                                          int pageSize,
                                          Long nodeId,
                                          MeshStatus meshStatus,
                                          @DateTimeFormat(pattern="yyyy-MM-dd")Date meshStartTime,
                                          @DateTimeFormat(pattern="yyyy-MM-dd")Date meshEndTime) {
        List<Long> allNodeIdList = new ArrayList<>();
        List<BeTopologyLinkDTO> allLinkList = new ArrayList<>();
        QueryLinksResult resultAll = new QueryLinksResult();

        if(nodeId == null){
            // 查询所有结果
            QueryLinksOrder order = new QueryLinksOrder();
            order.setMeshEndTime(meshEndTime);
            order.setMeshStartTime(meshStartTime);
            order.setMeshStatus(meshStatus);
            order.setPageNo(pageNo);
            order.setPageSize(pageSize);

            resultAll = topologyService.queryLinkList(order);
            if(!resultAll.isSuccess()) {
                return resultAll;
            }else if(resultAll.getInfos() != null){
                allLinkList.addAll(resultAll.getInfos());
                resultAll.getInfos().stream().forEach(info -> {
                    Long eachGuestNodeId = info.getGuestNodeId();
                    Long eachHostNodeId = info.getHostNodeId();
                    if(!allNodeIdList.contains(eachGuestNodeId))allNodeIdList.add(eachGuestNodeId);
                    if(!allNodeIdList.contains(eachHostNodeId))allNodeIdList.add(eachHostNodeId);
                });
            }

        }else{
            // 分别以host和guest1查询
            QueryLinksOrder orderHost = new QueryLinksOrder();
            orderHost.setMeshEndTime(meshEndTime);
            orderHost.setMeshStartTime(meshStartTime);
            orderHost.setMeshStatus(meshStatus);
            orderHost.setHostNodeId(nodeId);
            orderHost.setPageNo(pageNo);
            orderHost.setPageSize(pageSize);

            QueryLinksResult resultHost = topologyService.queryLinkList(orderHost);
            if(!resultHost.isSuccess()) {
                return resultHost;
            }else if(resultHost.getInfos() != null){
                allLinkList.addAll(resultHost.getInfos());
                QueryLinksResult finalResultAllTempHost = resultAll;
                resultHost.getInfos().stream().forEach(info -> {
                    Long eachGuestNodeId = info.getGuestNodeId();
                    Long eachHostNodeId = info.getHostNodeId();
                    if(!allNodeIdList.contains(eachGuestNodeId))allNodeIdList.add(eachGuestNodeId);
                    if(!allNodeIdList.contains(eachHostNodeId))allNodeIdList.add(eachHostNodeId);
                    finalResultAllTempHost.addInfo(info);
                });
            }

            // 查询guest
            QueryLinksOrder orderGuest = new QueryLinksOrder();
            orderGuest.setMeshEndTime(meshEndTime);
            orderGuest.setMeshStartTime(meshStartTime);
            orderGuest.setMeshStatus(meshStatus);
            orderGuest.setGuestNodeId(nodeId);
            orderGuest.setPageNo(pageNo);
            orderGuest.setPageSize(pageSize);

            QueryLinksResult resultGuest = topologyService.queryLinkList(orderGuest);
            if(!resultGuest.isSuccess()) {
                return resultHost;
            }else if(resultGuest.getInfos() != null){
                allLinkList.addAll(resultGuest.getInfos());
                QueryLinksResult finalResultAllTempGuest = resultAll;
                resultGuest.getInfos().stream().forEach(info -> {
                    Long eachGuestNodeId = info.getGuestNodeId();
                    Long eachHostNodeId = info.getHostNodeId();
                    if(!allNodeIdList.contains(eachGuestNodeId))allNodeIdList.add(eachGuestNodeId);
                    if(!allNodeIdList.contains(eachHostNodeId))allNodeIdList.add(eachHostNodeId);
                    finalResultAllTempGuest.addInfo(info);
                });
            }

            resultAll.setCode(resultGuest.getCode());
            resultAll.setMessage(resultGuest.getMessage());
            resultAll.setStatus(resultGuest.getStatus());
        }

        Map<Long, BeTopologyNodeDTO> allNodeList = queryAllNodeInfo(allNodeIdList);

        Collections.sort(allLinkList, new Comparator<BeTopologyLinkDTO>() {
            @Override
            public int compare(BeTopologyLinkDTO o1, BeTopologyLinkDTO o2) {
                return o1.getMeshDate().compareTo(o2.getMeshDate());
            }
        });

        QueryLinksResult finalResultAll = resultAll;
        allLinkList.stream().forEach(link -> {
            link.setGuestNode(allNodeList.get(link.getGuestNodeId()));
            link.setHostNode(allNodeList.get(link.getHostNodeId()));
        });

        return finalResultAll;
    }

    private Map<Long, BeTopologyNodeDTO> queryAllNodeInfo(List<Long> allNodeIdList){
        Map<Long, BeTopologyNodeDTO> allNodeList = new HashMap<Long,BeTopologyNodeDTO>();
        FindNodesOrder order = new FindNodesOrder();
        order.setNodeIdList(allNodeIdList);
        FindNodesResult result = topologyService.findNodes(order);
        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("findNodes[%s] error", allNodeIdList));
        }

        if(!CollectionUtils.isEmpty(result.getNodes())){
            result.getNodes().stream().forEach(node->{
                FindCustomerInfoOrder findCustomerInfoOrder = new FindCustomerInfoOrder();
                findCustomerInfoOrder.setCustomerId(node.getCustomerId());
                FindCustomerInfoResult findCustomerInfoResult = topologyService.findCustomerInfo(findCustomerInfoOrder);
                if(!findCustomerInfoResult.isSuccess()){
                    throw new BizException(findCustomerInfoResult.getStatus(), findCustomerInfoResult.getMessage(),
                            String.format("findCustomerInfoResult[%s] error", node.getCustomerId()));
                }else{
                    node.setCustomerInfo(findCustomerInfoResult.getCustomerInfo());
                }
                allNodeList.put(node.getId(),node);
            });
        }
        return allNodeList;
    }


    /**
     * 添加或修改客户信息
     */
    @RequestMapping("/node/addOrModifyCustomerInfo")
    public EmptyResult addOrModifyCustomer(String customerName, String customerCode,
                                      CustomerStatus customerStatus, Long id) {
        AddOrModifyCustomerOrder order = new AddOrModifyCustomerOrder();
        order.setId(id);
        order.setCustomerName(customerName);
        order.setCustomerStatus(customerStatus);
        order.setCustomerCode(Optional.ofNullable(customerCode).orElse(UUID.fastUUID().toString()));

        return topologyService.addOrModifyCustomerInfo(order);
    }

    /**
     * 添加或修改客户信息
     */
    @RequestMapping("/node/addOrModifyNode")
    public EmptyResult addOrModifyNode(Long id,
                                       String nodeName,
                                       String nodeCode,
                                       NodeStatus nodeStatus,
                                       NodeType nodeType,
                                       ConnectGlab connectGlab,
                                       String customerId,
                                       DeployEnv deployEnv,
                                       String deployOwner,
                                       FinishTest finishTest,
                                       DeployType deployType,
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date deployTime,
                                       String licenseDetail,
                                       String mid,
                                       Long licenseExpireTime,
                                       String instId,
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date licenseValidDate,
                                       String machineConfig,
                                       String networkConfig,
                                       String remark,
                                       String scene,
                                       String projectOwner,
                                       ProjectType projectType,
                                       ProductType productType,
                                       String productVersion
                                       ) {
        AddOrModifyNodeOrder order = new AddOrModifyNodeOrder();
        order.setId(id);
        order.setInstId(instId);
        order.setNodeName(nodeName);
        order.setNodeCode(nodeCode);
        order.setNodeStatus(nodeStatus);
        order.setNodeType(nodeType);
        order.setConnectGlab(connectGlab);
        order.setCustomerId(customerId);
        order.setDeployEnv(deployEnv);
        order.setDeployOwner(deployOwner);
        order.setFinishTest(finishTest);
        order.setDeployTime(deployTime);
        order.setLicenseExpireTime(licenseExpireTime);
        order.setLicenseValidDate(licenseValidDate);
        order.setMachineConfig(machineConfig);
        order.setNetworkConfig(networkConfig);
        order.setRemark(remark);
        order.setScene(scene);
        order.setProjectOwner(projectOwner);
        order.setProjectType(projectType);
        order.setProductType(productType);
        order.setProductVersion(productVersion);
        order.setDeployType(deployType);
        order.setLicenseDetail(licenseDetail);
        order.setMid(mid);
        return topologyService.addOrModifyNode(order);
    }

    /**
     * 添加或修改组网信息
     */
    @RequestMapping("/link/addOrModifyLink")
    public EmptyResult addOrModifyLink(Long id,
                                       Long hostNodeId,
                                       Long guestNodeId,
                                       MeshStatus meshStatus,
                                       String meshScene,
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date meshDate) {
        AddOrModifyLinkOrder order = new AddOrModifyLinkOrder();
        order.setId(id);
        order.setHostNodeId(hostNodeId);
        order.setGuestNodeId(guestNodeId);
        order.setMeshDate(meshDate);
        order.setMeshStatus(meshStatus);
        order.setMeshScene(meshScene);

        return topologyService.addOrModifyLink(order);
    }

    /**
     * 分页查找工单列表
     */
    @RequestMapping("/order/queryDeployOrderList")
    public QueryOrderResult queryDeployOrderList(int pageNo,
                                                 int pageSize,
                                                 String customerId,
                                                 ProductType productType,
                                                 String productVersion,
                                                 String projectOwner,
                                                 String deployOwner,
                                                 @DateTimeFormat(pattern="yyyy-MM-dd") Date deployStartTime,
                                                 @DateTimeFormat(pattern="yyyy-MM-dd") Date deployEndTime,
                                                 OrderStatus orderStatus) {
        BeCustomerInfoDTO customerInfo = null;
        // 如果customerName非空，则先模糊查询客户信息
        if(StringUtils.isNotEmpty(customerId)) customerInfo = findCustomerById(customerId);

        QueryOrdersOrder order = new QueryOrdersOrder();
        if(customerInfo != null) order.setCustomerId(customerInfo.getId());
        order.setOrderStatus(orderStatus);
        order.setDeployEndTime(deployEndTime);
        order.setDeployOwner(deployOwner);
        order.setDeployStartTime(deployStartTime);
        order.setProjectOwner(projectOwner);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setProductType(productType);
        order.setProductVersion(productVersion);

        QueryOrderResult result = topologyService.queryOrderList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryOrderList[%s] error", order));
        }

        if(!CollectionUtils.isEmpty(result.getInfos())){
            BeCustomerInfoDTO finalCustomerInfo = customerInfo;
            List<Long> nodeIdList = Lists.newArrayList();
            result.getInfos().stream().forEach(info->{
                if(finalCustomerInfo != null){
                    info.setCustomerInfo(finalCustomerInfo);
                }else{
                    info.setCustomerInfo(findCustomerById(info.getCustomerId()));
                }
                if (info.getWithNodeId() != null) {
                    nodeIdList.add(Long.parseLong(info.getWithNodeId()));
                }
            });

            Map<Long, BeTopologyNodeDTO> nodeMap = queryAllNodeInfo(nodeIdList);
            result.getInfos().stream().forEach(info->{
                if (info.getWithNodeId() != null) {
                    info.setWithNode(nodeMap.get(Long.parseLong(info.getWithNodeId())));
                }
            });
        }

        return result;
    }

    private BeCustomerInfoDTO findCustomerById(String customerId){
        BeCustomerInfoDTO customerInfo = null;
        FindCustomerInfoOrder findCustomerInfoOrder = new FindCustomerInfoOrder();
        findCustomerInfoOrder.setCustomerId(customerId);
        FindCustomerInfoResult findCustomerInfoResult = topologyService.findCustomerInfo(findCustomerInfoOrder);
        if(findCustomerInfoResult.isSuccess()){
            customerInfo = findCustomerInfoResult.getCustomerInfo();
            if(customerInfo == null){
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                        String.format("customer[%s] not exist", customerId));
            }
        }else{
            throw new BizException(findCustomerInfoResult.getStatus(), findCustomerInfoResult.getCode(),
                    findCustomerInfoResult.getMessage()+",customerId="+customerId);
        }
        return customerInfo;
    }

    private BeOutsourceDTO findOutsrouceById(Long outsourceId){
        BeOutsourceDTO beOutsourceDTO = null;

        FindOutsourceOrder findOutsourceOrder = new FindOutsourceOrder();
        findOutsourceOrder.setOutsourceId(outsourceId);

        FindOutsourceResult findOutsourceResult = topologyService.findOutsource(findOutsourceOrder);
        if(findOutsourceResult.isSuccess()){
            beOutsourceDTO = findOutsourceResult.getBeOutsourceDTO();
            if(beOutsourceDTO == null){
                throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                        String.format("outsource[%s] not exist", outsourceId));
            }
        }else{
            throw new BizException(findOutsourceResult.getStatus(), findOutsourceResult.getCode(),
                    findOutsourceResult.getMessage()+",outsourceId="+outsourceId);
        }
        return beOutsourceDTO;
    }

    /**
     * 添加或修改部署工单
     */
    @RequestMapping("/order/addOrModifyOrder")
    public EmptyResult addOrModifyOrder(Long id,
                                       String customerId,
                                       OrderType orderType,
                                       String withNodeId,
                                       DeliverType deliverType,
                                       String scene,
                                       String nodeName,
                                       NodeType nodeType,
                                       ProjectType projectType,
                                       ProductType productType,
                                       String productVersion,
                                       ConnectGlab connectGlab,
                                       String projectOwner,
                                       String approveUser,
                                       String deployOwner,
                                       DeployEnv deployEnv,
                                       DeployType deployType,
                                       String machineConfig,
                                       String networkConfig,
                                       String deployArchDoc,
                                       String nodeCode,//这里是需要先生成节点编码
                                       String instId,//这里是需要先生成机构编码
                                       String licenseDetail,//这里是需要先生成节点编码
                                       String mid,
                                       Long licenseExpireTime,
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date licenseValidDate,
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date deployTime,
                                       Long deployCostTime,
                                       FinishTest finishTest,
                                       Long testCostTime,
                                       FinishLuopan finishLuopan,
                                       OrderStatus orderStatus,
                                       String message,
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date planDeployTime,
                                       String confirmLetter,
                                       String certificateDetail,
                                       String luopanDetail,
                                       String customizeDetail
    ) {
        AddOrModifyOrderOrder order = new AddOrModifyOrderOrder();
        order.setId(id);
        order.setInstId(instId);
        order.setNodeCode(nodeCode);
        order.setNodeName(nodeName);
        order.setNodeType(nodeType);
        order.setConnectGlab(connectGlab);
        order.setCustomerId(customerId);
        order.setDeployEnv(deployEnv);
        order.setDeployOwner(deployOwner);
        order.setFinishTest(finishTest);
        order.setDeployTime(deployTime);
        order.setLicenseDetail(licenseDetail);
        order.setMid(mid);
        order.setLicenseExpireTime(licenseExpireTime);
        order.setLicenseValidDate(licenseValidDate);
        order.setMachineConfig(machineConfig);
        order.setNetworkConfig(networkConfig);
        order.setScene(scene);
        order.setProjectOwner(projectOwner);
        order.setProjectType(projectType);
        order.setProductType(productType);
        order.setProductVersion(productVersion);
        order.setDeployType(deployType);
        order.setApproveUser(approveUser);
        order.setDeliverType(deliverType);
        order.setDeployArchDoc(deployArchDoc);
        order.setDeployCostTime(deployCostTime);
        order.setOrderType(orderType);
        order.setFinishLuopan(finishLuopan);
        order.setMessage(message);
        order.setTestCostTime(testCostTime);
        order.setWithNodeId(withNodeId);
        order.setOrderStatus(orderStatus);
        order.setPlanDeployTime(planDeployTime);
        order.setConfirmLetter(confirmLetter);
        order.setCertificateDetail(certificateDetail);
        order.setLuopanDetail(luopanDetail);
        order.setCustomizeDetail(customizeDetail);

        EmptyResult result = topologyService.addOrModifyOrder(order);

        // 如果是更新操作，同时工单状态变更为FINISHED则同步新增NODE信息
        if(result.isSuccess() && id != null && id.longValue() > 0 && OrderStatus.FINISHED.equals(orderStatus)){
            AddOrModifyNodeOrder nodeOrder = new AddOrModifyNodeOrder();
            if (withNodeId != null) {
                nodeOrder.setId(Long.parseLong(withNodeId));//如果是携带有withNodeId，则执行更新操作
            }
            nodeOrder.setNodeName(nodeName);
            nodeOrder.setInstId(instId);
            nodeOrder.setNodeCode(nodeCode);
            nodeOrder.setNodeStatus(NodeStatus.ONLINE);
            nodeOrder.setNodeType(nodeType);
            nodeOrder.setConnectGlab(connectGlab);
            nodeOrder.setLicenseDetail(licenseDetail);
            nodeOrder.setMid(mid);
            nodeOrder.setCustomerId(customerId);
            nodeOrder.setDeployEnv(deployEnv);
            nodeOrder.setDeployOwner(deployOwner);
            nodeOrder.setFinishTest(finishTest);
            nodeOrder.setDeployTime(deployTime);
            nodeOrder.setLicenseExpireTime(licenseExpireTime);
            nodeOrder.setLicenseValidDate(licenseValidDate);
            nodeOrder.setMachineConfig(machineConfig);
            nodeOrder.setNetworkConfig(networkConfig);
            nodeOrder.setRemark(scene);
            nodeOrder.setScene(scene);
            nodeOrder.setProjectOwner(projectOwner);
            nodeOrder.setProjectType(projectType);
            nodeOrder.setProductType(productType);
            nodeOrder.setProductVersion(productVersion);
            nodeOrder.setDeployType(deployType);
            topologyService.addOrModifyNode(nodeOrder);
        }

        return result;
    }

    /**
     * 调用mesh创建节点编码，机构编码，license
     */
    @RequestMapping("/order/createNodeCode")
    public CreateNodeCodeResult createNodeCode(String nodeName,
                                       ProductType productType, Integer days) {

        //node_type:0-GLab、1-GAIA、2-GLite
        String node_type = null;
        switch (productType){
            case GAIA:node_type="1";break;
            case GLAB:node_type="0";break;
            case Glite:node_type="2";break;
        }

        if(node_type == null){
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                    String.format("productType[%s] illegal", productType));
        }

        if (days == null || days < 0 || days > 10 * 365) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                    String.format("days:%d illegal", days));
        }

        JanusNodeRequestDTO nodeDTO = new JanusNodeRequestDTO();
        nodeDTO.setNode_type(node_type);
        //inst_type: 机构类型：00-蓝象、01-普通机构、02-SaaS管理机构、03-联盟盟主机构
        nodeDTO.setInst_type("01");
        nodeDTO.setInst_name(nodeName);

        CreateNodeCodeResult result = new CreateNodeCodeResult();

        try{
            JanusResponseDTO createNodeResult = JanusPostUtils.doPostForGenerateCode(nodeDTO);
            if(createNodeResult.isSuccess()){
                JanusNodeResponseDTO nodeResult = JSON.parseObject(createNodeResult.getContent(),JanusNodeResponseDTO.class);
                result.setNodeCode(nodeResult.getNode_id());
                result.setInstId(nodeResult.getInst_id());
                result.setStatus(Status.SUCCESS);

                long maxMs = days * 24 * 60 * 60 * 1000L;
                // 查询license
                JanusResponseDTO createLicenseResult
                        = JanusPostUtils.doPostForGenerateLicense(
                                nodeResult.getNode_id(),
                                nodeResult.getInst_id(),
                                new BigInteger(String.valueOf(maxMs)));
                if(createLicenseResult.isSuccess()){
                    String licenseDetail = createLicenseResult.getContent();
                    result.setLicenseDetail(licenseDetail);
                    JanusResponseDTO queryNodeResult = JanusPostUtils.doPostForGetCode(nodeResult.getNode_id());
                    if (queryNodeResult.isSuccess()) {
                        JanusQueryNodeResponseDTO queryNodeResponse = JSON.parseObject(queryNodeResult.getContent(), JanusQueryNodeResponseDTO.class);
                        result.setMid(queryNodeResponse.getMid());
                    } else {
                        result.setStatus(Status.FAIL);
                        result.setMessage("post fail,query node=" + queryNodeResult.toString());
                    }
                }else{
                    result.setStatus(Status.FAIL);
                    result.setMessage("post fail,createLicense="+createLicenseResult.toString());
                }

            }else{
                result.setStatus(Status.FAIL);
                result.setMessage("post fail,createNode="+createNodeResult.toString());
            }
        }catch(Exception e){
            result.setCode(CommonResultCode.ILLEGAL_STATE.getCode());
            result.setStatus(Status.FAIL);
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(),
                    String.format("doPost fail[%s]", e.getMessage()));
        }

        return result;
    }

    /**
     * 指派工单到某位运维同学
     */
    @RequestMapping("/order/dispatchOrder")
    public EmptyResult dispatchOrder(Long orderId,String targetDeployOwner){

        FindDeployOrderOrder findOrder = new FindDeployOrderOrder();
        findOrder.setOrderId(orderId);
        FindDeployOrderResult result = topologyService.findDeployOrderService(findOrder);
        BeDeployOrderDTO order = null;
        if(result.isSuccess()){
            order =  result.getBeDeployOrderDTO();
        }else{
            throw new BizException(Status.FAIL,result.getCode(),
                    String.format("queryDeployOrder[%s] fail", orderId));
        }

        AddOrModifyOrderOrder updateOrder = new AddOrModifyOrderOrder();
        updateOrder.setId(orderId);
        updateOrder.setDeployOwner(targetDeployOwner);

        updateOrder.setDeployEnv(order.getDeployEnv());
        updateOrder.setDeployType(order.getDeployType());
        updateOrder.setDeployTime(order.getDeployTime());
        updateOrder.setConnectGlab(order.getConnectGlab());
        updateOrder.setCustomerId(order.getCustomerId());
        updateOrder.setFinishTest(order.getFinishTest());
        updateOrder.setMachineConfig(order.getMachineConfig());
        updateOrder.setNetworkConfig(order.getNetworkConfig());
        updateOrder.setInstId(order.getInstId());
        updateOrder.setNodeCode(order.getNodeCode());
        updateOrder.setScene(order.getScene());
        updateOrder.setOrderStatus(order.getOrderStatus());
        updateOrder.setOrderType(order.getOrderType());
        updateOrder.setProductType(order.getProductType());
        updateOrder.setProductVersion(order.getProductVersion());
        updateOrder.setProjectOwner(order.getProjectOwner());
        updateOrder.setApproveUser(order.getApproveUser());
        updateOrder.setDeployArchDoc(order.getDeployArchDoc());
        updateOrder.setWithNodeId(order.getWithNodeId());
        updateOrder.setTestCostTime(order.getTestCostTime());
        updateOrder.setMessage(order.getMessage());
        updateOrder.setLicenseExpireTime(order.getLicenseExpireTime());
        updateOrder.setLicenseValidDate(order.getLicenseValidDate());
        updateOrder.setFinishLuopan(order.getFinishLuopan());
        updateOrder.setDeployCostTime(order.getDeployCostTime());
        updateOrder.setDeliverType(order.getDeliverType());
        updateOrder.setNodeName(order.getNodeName());
        updateOrder.setNodeType(order.getNodeType());
        updateOrder.setProjectType(order.getProjectType());
        updateOrder.setDeployOwner(order.getDeployOwner());
        updateOrder.setPlanDeployTime(order.getPlanDeployTime());
        updateOrder.setConfirmLetter(order.getConfirmLetter());
        updateOrder.setCertificateDetail(order.getCertificateDetail());
        updateOrder.setLuopanDetail(order.getLuopanDetail());
        updateOrder.setCustomizeDetail(order.getCustomizeDetail());

        EmptyResult updateResult = topologyService.addOrModifyOrder(updateOrder);

        if(updateResult.isSuccess()){
            List<String> mobileList=new ArrayList<>();
            mobileList.add(targetDeployOwner);
            DeployDingdingUtils.sendMessageAtChosePerson("你有一个部署工单，请跟进处理",mobileList);
        }

        return updateResult;
    }

    /**
     * 获取运维同学列表
     */
    @RequestMapping("/order/getDeployOwnerList")
    public QueryDeployOwnerListResult getDeployOwnerList(){
        QueryDeployOwnerListResult result = new QueryDeployOwnerListResult();
        result.setDeplymentOwnerMap(DeployDingdingUtils.getDeployOwnerList());
        result.setStatus(Status.SUCCESS);
        return result;
    }

    /**
     * 售前工单相关接口
     */
    @RequestMapping("/order/addOrModifyPreSaleOrder")
    public EmptyResult addOrModifyPreSaleOrder(Long id,
                                               String customerId,
                                               PreSaleOrderType preSaleOrderType,
                                               String projectName,
                                               String scene,
                                               ProductType productType,
                                               String projectOwner,
                                               String content,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date planFinishDate,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderFirstCreateDate,
                                               String dispatchOwner,
                                               String deployOwner,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDispatchDate,
                                               String finishContent,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate,
                                               PreSaleOrderStatus preSaleOrderStatus,
                                               Long score1, // 业务负责人评分
                                               String score1Content,// 业务负责人评分备注
                                               Long score2,// 运维负责人评分
                                               String score2Content,// 运维负责人评分
                                               String rejectReason// 拒绝原因
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyPreSaleOrderOrder order = new AddOrModifyPreSaleOrderOrder();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setDeployOwner(deployOwner);
        order.setScene(scene);
        order.setProjectName(projectName);
        order.setProjectOwner(projectOwner);
        order.setProductType(productType);
        order.setPreSaleOrderType(preSaleOrderType);
        order.setContent(content);
        order.setPlanFinishDate(planFinishDate);
        order.setOrderFirstCreateDate(orderFirstCreateDate);
        order.setDispatchOwner(StringUtils.isEmpty(dispatchOwner)?"折竹":dispatchOwner);
        order.setOrderDispatchDate(orderDispatchDate);
        order.setFinishContent(finishContent);
        order.setFinishDate(finishDate);
        order.setPreSaleOrderStatus(preSaleOrderStatus);
        order.setScore1(score1);
        order.setScore1Content(score1Content);
        order.setScore2(score2);
        order.setScore2Content(score2Content);
        order.setRejectReason(rejectReason);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindPreSaleOrderOrder findOrder = new FindPreSaleOrderOrder();
            findOrder.setPreSaleOrderId(id);
            FindPreSaleOrderResult findResult = topologyService.findPreSaleOrder(findOrder);
            if(findResult.isSuccess()){
                BePreSaleOrderDTO bePreSaleOrderDTO = findResult.getBePreSaleOrderDTO();
                if(customerId == null)order.setCustomerId(bePreSaleOrderDTO.getCustomerId());
                if(deployOwner == null)order.setDeployOwner(bePreSaleOrderDTO.getDeployOwner());
                if(scene == null)order.setScene(bePreSaleOrderDTO.getScene());
                if(projectName == null){
                    order.setProjectName(bePreSaleOrderDTO.getProjectName());
                    projectName = bePreSaleOrderDTO.getProjectName();
                }
                if(projectOwner == null)order.setProjectOwner(bePreSaleOrderDTO.getProjectOwner());
                if(productType == null)order.setProductType(bePreSaleOrderDTO.getProductType());
                if(preSaleOrderType == null)order.setPreSaleOrderType(bePreSaleOrderDTO.getPreSaleOrderType());
                if(content == null)order.setContent(bePreSaleOrderDTO.getContent());
                if(planFinishDate == null)order.setPlanFinishDate(bePreSaleOrderDTO.getPlanFinishDate());
                if(orderFirstCreateDate == null)order.setOrderFirstCreateDate(bePreSaleOrderDTO.getOrderFirstCreateDate());
                if(order.getDispatchOwner() == null)order.setDispatchOwner(bePreSaleOrderDTO.getDispatchOwner());
                if(orderDispatchDate == null)order.setOrderDispatchDate(bePreSaleOrderDTO.getOrderDispatchDate());
                if(finishContent == null)order.setFinishContent(bePreSaleOrderDTO.getFinishContent());
                if(finishDate == null)order.setFinishDate(bePreSaleOrderDTO.getFinishDate());
                if(preSaleOrderStatus == null)order.setPreSaleOrderStatus(bePreSaleOrderDTO.getPreSaleOrderStatus());
                if(score1 == null)order.setScore1(bePreSaleOrderDTO.getScore1());
                if(score2 == null)order.setScore2(bePreSaleOrderDTO.getScore2());
                if(score1Content == null)order.setScore1Content(bePreSaleOrderDTO.getScore1Content());
                if(score2Content == null)order.setScore2Content(bePreSaleOrderDTO.getScore2Content());
                if(rejectReason == null)order.setRejectReason(bePreSaleOrderDTO.getRejectReason());
            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        //如果是更新操作，并且状态改成了ORDER_DOING，则必须要有运维负责人
        if(id != null && PreSaleOrderStatus.ORDER_DOING.equals(preSaleOrderStatus) && StringUtils.isEmpty(deployOwner)){
            result.setStatus(Status.FAIL);
            result.setCode("deployOwner can not be null");
            result.setMessage("如果是更新操作，并且状态改成了ORDER_DOING，则必须要有运维负责人");
            return result;
        }


        result = topologyService.addOrModifyPreSaleOrder(order);

        //如果是新增工单，则发通知给折竹、含笑、宗政
        if(result.isSuccess() && id == null){
            List<String> mobileList=new ArrayList<>();
            mobileList.add("折竹");
            mobileList.add("含笑");
            mobileList.add("宗政");
            if(projectName != null){
                DeployDingdingUtils.sendMessageAtChosePerson("新增一个售前工单["+projectName+"]，请跟进处理",mobileList);
            }else{
                DeployDingdingUtils.sendMessageAtChosePerson("新增一个售前工单，请跟进处理",mobileList);
            }
        }

        //如果是更新操作，同时已经分配了运维人员，则发消息给对口运维人员
        if(result.isSuccess() && id != null && StringUtils.isNotEmpty(deployOwner)
                && PreSaleOrderStatus.ORDER_DOING.equals(preSaleOrderStatus)){
            List<String> mobileList=new ArrayList<>();
            mobileList.add(deployOwner);
            if(projectName != null){
                DeployDingdingUtils.sendMessageAtChosePerson("您有一个售前工单["+projectName+"]待处理，请跟进处理",mobileList);
            }else{
                DeployDingdingUtils.sendMessageAtChosePerson("您有一个售前工单待处理，请跟进处理",mobileList);
            }

        }

        return result;
    }

    /**
     * 分页查找工单列表
     */
    @RequestMapping("/order/queryPreSaleOrderList")
    public QueryPreSaleOrderResult queryPreSaleOrderList(int pageNo,
                                                 int pageSize,
                                                 String customerId,
                                                 ProductType productType,
                                                 String projectOwner,
                                                 String deployOwner,
                                                 @DateTimeFormat(pattern="yyyy-MM-dd") Date finishStartTime,
                                                 @DateTimeFormat(pattern="yyyy-MM-dd") Date finishEndTime,
                                                 PreSaleOrderStatus preSaleOrderStatus) {
        BeCustomerInfoDTO customerInfo = null;
        // 如果customerName非空，则先模糊查询客户信息
        if(StringUtils.isNotEmpty(customerId)) customerInfo = findCustomerById(customerId);

        QueryPreSaleOrdersOrder order = new QueryPreSaleOrdersOrder();
        if(customerInfo != null) order.setCustomerId(customerInfo.getId());
        order.setPreSaleOrderStatus(preSaleOrderStatus);
        order.setFinishEndTime(finishEndTime);
        order.setDeployOwner(deployOwner);
        order.setFinishStartTime(finishStartTime);
        order.setProjectOwner(projectOwner);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setProductType(productType);

        QueryPreSaleOrderResult result = topologyService.queryPreSaleOrderList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryOrderList[%s] error", order));
        }

        if(!CollectionUtils.isEmpty(result.getInfos())){
            BeCustomerInfoDTO finalCustomerInfo = customerInfo;
            List<Long> nodeIdList = Lists.newArrayList();
            result.getInfos().stream().forEach(info->{
                if(finalCustomerInfo != null){
                    info.setCustomerInfo(finalCustomerInfo);
                }else{
                    info.setCustomerInfo(findCustomerById(info.getCustomerId()));
                }

            });
        }

        return result;
    }

    /**
     * 分页查找工单列表
     */
    @RequestMapping("/order/queryOutsourceProjectRecordList")
    public QueryOutsourceProjectRecordResult queryOutsourceProjectRecordList(int pageNo,
                                                         int pageSize,
                                                         String applyName,
                                                         String projectName,
                                                         RecordStatus recordStatus
                                                         ) {
        QueryOutsourceProjectRecordsOrder order = new QueryOutsourceProjectRecordsOrder();
        order.setRecordStatus(recordStatus);
        order.setApplyName(applyName);
        order.setProjectName(projectName);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryOutsourceProjectRecordResult result = topologyService.queryOutsourceProjectRecordList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryOutsourceProjectRecordList[%s] error", order));
        }

        if(!CollectionUtils.isEmpty(result.getInfos())){
            result.getInfos().stream().forEach(info->{
                info.setBeOutsourceDTO(findOutsrouceById(info.getOutsourceId()));
            });
        }

        return result;
    }

    /**
     * 分页查找外包员工列表
     */
    @RequestMapping("/order/queryOutsourceList")
    public QueryOutsourceResult queryOutsourceList(      int pageNo,
                                                         int pageSize,
                                                         ServiceStatus serviceStatus,
                                                         String name,
                                                         String penName) {

        QueryOutsourcesOrder order = new QueryOutsourcesOrder();
        order.setServiceStatus(serviceStatus);
        order.setName(name);
        order.setPenName(penName);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryOutsourceResult result = topologyService.queryOutsourceList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryOutsourceList[%s] error", order));
        }

        return result;
    }

    /**
     * 外包人员修改更新接口
     */
    @RequestMapping("/order/addOrModifyBeOutsource")
    public EmptyResult addOrModifyBeOutsource(Long id,
                                              String name,
                                              String penName,
                                              String phoneNum,
                                              String certNo,
                                              String sex,
                                              String education,
                                              Long salary,
                                              String baseSite,
                                              String major,
                                              Long workAge,
                                              String resumeFile,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date entryTime,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date leaveTime,
                                              ServiceStatus serviceStatus
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeOutsourceOrder order = new AddOrModifyBeOutsourceOrder();
        order.setId(id);
        order.setName(name);
        order.setPenName(penName);
        order.setPhoneNum(phoneNum);
        order.setCertNo(certNo);
        order.setSex(sex);
        order.setEducation(education);
        order.setSalary(salary);
        order.setBaseSite(baseSite);
        order.setMajor(major);
        order.setWorkAge(workAge);
        order.setResumeFile(resumeFile);
        order.setEntryTime(entryTime);
        order.setLeaveTime(leaveTime);
        order.setServiceStatus(serviceStatus);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindOutsourceOrder findOrder = new FindOutsourceOrder();
            findOrder.setOutsourceId(id);
            FindOutsourceResult findResult = topologyService.findOutsource(findOrder);
            if(findResult.isSuccess()){
                BeOutsourceDTO beOutsourceDTO = findResult.getBeOutsourceDTO();
                if(name == null) order.setName(beOutsourceDTO.getName());
                if(penName == null) order.setPenName(beOutsourceDTO.getPenName());
                if(phoneNum == null) order.setPhoneNum(beOutsourceDTO.getPhoneNum());
                if(certNo == null) order.setCertNo(beOutsourceDTO.getCertNo());
                if(sex == null) order.setSex(beOutsourceDTO.getSex());
                if(education == null) order.setEducation(beOutsourceDTO.getEducation());
                if(salary == null) order.setSalary(beOutsourceDTO.getSalary());
                if(baseSite == null) order.setBaseSite(beOutsourceDTO.getBaseSite());
                if(major == null) order.setMajor(beOutsourceDTO.getMajor());
                if(workAge == null) order.setWorkAge(beOutsourceDTO.getWorkAge());
                if(resumeFile == null) order.setResumeFile(beOutsourceDTO.getResumeFile());
                if(entryTime == null) order.setEntryTime(beOutsourceDTO.getEntryTime());
                if(leaveTime == null) order.setLeaveTime(beOutsourceDTO.getLeaveTime());
                if(serviceStatus == null) order.setServiceStatus(beOutsourceDTO.getServiceStatus());
            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyOutsource(order);

        return result;
    }

    /**
     * 外包人员入项新增修改相关接口
     */
    @RequestMapping("/order/addOrModifyOutsourceProjectRecord")
    public EmptyResult addOrModifyOutsourceProjectRecord(
                                               Long id,
                                               String applyName,
                                               String applyTitle,
                                               Long outsourceId,
                                               String projectName,
                                               String stoName,
                                               String leaderName,
                                               String pmName,
                                               String bdName,
                                               String saName,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date planProjectEnterDate,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date planProjectLeaveDate,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date realProjectEnterDate,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date realProjectLeaveDate,
                                               RecordStatus recordStatus,
                                               Long score1,
                                               String score1Content,
                                               Long score2,
                                               String score2Content,
                                               String rejectReason,
                                               String approvePeople
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyOutsourceProjectRecordOrder order = new AddOrModifyOutsourceProjectRecordOrder();

        order.setId(id);
        order.setApplyName(applyName);
        order.setApplyTitle(applyTitle);
        order.setOutsourceId(outsourceId);
        order.setProjectName(projectName);
        order.setStoName(stoName);
        order.setLeaderName(leaderName);
        order.setPmName(pmName);
        order.setBdName(bdName);
        order.setSaName(saName);
        order.setPlanProjectEnterDate(planProjectEnterDate);
        order.setPlanProjectLeaveDate(planProjectLeaveDate);
        order.setRealProjectEnterDate(realProjectEnterDate);
        order.setRealProjectLeaveDate(realProjectLeaveDate);
        order.setRecordStatus(recordStatus);
        order.setScore1(score1);
        order.setScore1Content(score1Content);
        order.setScore2(score2);
        order.setScore2Content(score2Content);
        order.setRejectReason(rejectReason);
        order.setApprovePeople(approvePeople);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindOutsourceProjectRecordOrder findOrder = new FindOutsourceProjectRecordOrder();
            findOrder.setOutsourceProjectRecordId(id);
            FindOutsourceProjectRecordResult findResult = topologyService.findOutsourceProjectRecord(findOrder);
            if(findResult.isSuccess()){

                BeOutsourceProjectRecordDTO beOutsourceProjectRecordDTO = findResult.getBeOutsourceProjectRecordDTO();
                if(applyName == null)order.setApplyName(beOutsourceProjectRecordDTO.getApplyName());
                if(applyTitle == null)order.setApplyTitle(beOutsourceProjectRecordDTO.getApplyTitle());
                if(outsourceId == null)order.setOutsourceId(beOutsourceProjectRecordDTO.getOutsourceId());
                if(projectName == null)order.setProjectName(beOutsourceProjectRecordDTO.getProjectName());
                if(stoName == null)order.setStoName(beOutsourceProjectRecordDTO.getStoName());
                if(leaderName == null)order.setLeaderName(beOutsourceProjectRecordDTO.getLeaderName());
                if(pmName == null)order.setPmName(beOutsourceProjectRecordDTO.getPmName());
                if(bdName == null)order.setBdName(beOutsourceProjectRecordDTO.getBdName());
                if(saName == null)order.setSaName(beOutsourceProjectRecordDTO.getSaName());
                if(planProjectEnterDate == null)order.setPlanProjectEnterDate(beOutsourceProjectRecordDTO.getPlanProjectEnterDate());
                if(planProjectLeaveDate == null)order.setPlanProjectLeaveDate(beOutsourceProjectRecordDTO.getPlanProjectLeaveDate());
                if(realProjectEnterDate == null)order.setRealProjectEnterDate(beOutsourceProjectRecordDTO.getRealProjectEnterDate());
                if(realProjectLeaveDate == null)order.setRealProjectLeaveDate(beOutsourceProjectRecordDTO.getRealProjectLeaveDate());
                if(recordStatus == null)order.setRecordStatus(beOutsourceProjectRecordDTO.getRecordStatus());
                if(score1 == null)order.setScore1(beOutsourceProjectRecordDTO.getScore1());
                if(score1Content == null)order.setScore1Content(beOutsourceProjectRecordDTO.getScore1Content());
                if(score2 == null)order.setScore2(beOutsourceProjectRecordDTO.getScore2());
                if(score2Content == null)order.setScore2Content(beOutsourceProjectRecordDTO.getScore2Content());
                if(rejectReason == null)order.setRejectReason(beOutsourceProjectRecordDTO.getRejectReason());
                if(approvePeople == null)order.setApprovePeople(beOutsourceProjectRecordDTO.getApprovePeople());
            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyOutsourceProjectRecord(order);

        return result;
    }

    /**
     * 分页查找报表列表
     */
    @RequestMapping("/order/queryReportList")
    public QueryReportResult queryReportList(      int pageNo,
                                                      int pageSize,
                                                      ReportStatus reportStatus) {

        QueryReportsOrder order = new QueryReportsOrder();
        order.setReportStatus(reportStatus);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryReportResult result = topologyService.queryReportList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryReportList[%s] error", order));
        }

        return result;
    }

    /**
     * 新增或修改申报工时
     */
    @RequestMapping("/order/addOrModifyBeWorkOrder")
    public EmptyResult addOrModifyBeWorkOrder(
            Long id,
            Long projectId,
            String beSystemIds,
            Long projectWorkId,
            Long employeeId,
            String loginName,
            Long productId,
            Long productVersionId,
            RequirementType requirementType,
            WorkOrderType workOrderType,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date workDate,
            Double manDay,
            String workContent,
            WorkOrderStatus workOrderStatus,
            String approveProgress,
            String rejectReason
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeWorkOrderOrder order = new AddOrModifyBeWorkOrderOrder();

        order.setId(id);
        order.setProjectId(projectId);
        order.setBeSystemIds(beSystemIds);
        order.setProjectWorkId(projectWorkId);
        order.setEmployeeId(employeeId);
        order.setLoginName(loginName);
        order.setProductId(productId);
        order.setProductVersionId(productVersionId);
        order.setRequirementType(requirementType);
        order.setWorkOrderType(workOrderType);
        order.setWorkDate(workDate);
        order.setManDay(manDay);
        order.setWorkContent(workContent);
        order.setWorkOrderStatus(workOrderStatus);
        order.setApproveProgress(approveProgress);
        order.setRejectReason(rejectReason);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeWorkOrderOrder findOrder = new FindBeWorkOrderOrder();
            findOrder.setId(id);
            FindBeWorkOrderResult findResult = topologyService.findBeWorkOrder(findOrder);
            if(findResult.isSuccess()){

                BeWorkOrderDTO beWorkOrderDTO = findResult.getBeWorkOrderDTO();
                if(projectId == null)order.setProjectId(beWorkOrderDTO.getProjectId());
                if(beSystemIds == null)order.setBeSystemIds(beWorkOrderDTO.getBeSystemIds());
                if(projectWorkId == null)order.setProjectWorkId(beWorkOrderDTO.getProjectWorkId());
                if(employeeId == null)order.setEmployeeId(beWorkOrderDTO.getEmployeeId());
                if(productId == null)order.setProductId(beWorkOrderDTO.getProductId());
                if(productVersionId == null)order.setProductVersionId(beWorkOrderDTO.getProductVersionId());
                if(requirementType == null)order.setRequirementType(beWorkOrderDTO.getRequirementType());
                if(workOrderType == null)order.setWorkOrderType(beWorkOrderDTO.getWorkOrderType());
                // workDay不能更新
                order.setWorkDate(beWorkOrderDTO.getWorkDate());
                order.setCreateTime(beWorkOrderDTO.getCreateTime());
                if(manDay == null)order.setManDay(beWorkOrderDTO.getManDay());
                if(workContent == null)order.setWorkContent(beWorkOrderDTO.getWorkContent());
                if(workOrderStatus == null)order.setWorkOrderStatus(beWorkOrderDTO.getWorkOrderStatus());
                if(approveProgress == null)order.setApproveProgress(beWorkOrderDTO.getApproveProgress());
                if(rejectReason == null)order.setRejectReason(beWorkOrderDTO.getRejectReason());

            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeWorkOrder(order);

        return result;
    }

    /**
     * 新增或修改项目访问权限信息
     */
    @RequestMapping("/order/addOrModifyBeProjectAccess")
    public EmptyResult addOrModifyBeProjectAccess(
            Long id,
            Long projectId,
            Long employeeId,
            AccessLevel accessLevel,
            AccessStatus accessStatus
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeProjectAccessOrder order = new AddOrModifyBeProjectAccessOrder();

        order.setId(id);
        order.setProjectId(projectId);
        order.setEmployeeId(employeeId);
        order.setAccessLevel(accessLevel);
        order.setAccessStatus(accessStatus);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeProjectAccessOrder findOrder = new FindBeProjectAccessOrder();
            findOrder.setId(id);
            FindBeProjectAccessResult findResult = topologyService.findBeProjectAccess(findOrder);
            if(findResult.isSuccess()){

                BeProjectAccessDTO beProjectAccessDTO = findResult.getBeProjectAccessDTO();
                if(projectId == null)order.setProjectId(beProjectAccessDTO.getProjectId());
                if(employeeId == null)order.setEmployeeId(beProjectAccessDTO.getEmployeeId());
                if(accessLevel == null)order.setAccessLevel(beProjectAccessDTO.getAccessLevel());
                if(accessStatus == null)order.setAccessStatus(beProjectAccessDTO.getAccessStatus());

            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeProjectAccess(order);

        return result;
    }

    /**
     * 分页查找工时工单列表
     */
    @RequestMapping("/order/queryBeWorkOrderList")
    public QueryBeWorkOrderResult queryBeWorkOrderList(      int pageNo,
                                                             int pageSize,
                                                             RequirementType requirementType,
                                                             WorkOrderType workOrderType,
                                                             WorkOrderStatus workOrderStatus,
                                                             Long employeeId,
                                                             String loginName,
                                                             Long productVersionId,
                                                             @DateTimeFormat(pattern="yyyy-MM-dd")Date workStartTime,
                                                             @DateTimeFormat(pattern="yyyy-MM-dd")Date workEndTime) {

        QueryBeWorkOrderOrderWrapper wrapper = new QueryBeWorkOrderOrderWrapper();
        QueryBeWorkOrderOrder order = new QueryBeWorkOrderOrder();
        order.setRequirementType(requirementType);
        order.setWorkOrderType(workOrderType);
        order.setWorkOrderStatus(workOrderStatus);

        if(employeeId != null){
            List<Long> employeeIdList = Lists.newArrayList();
            employeeIdList.add(employeeId);
            order.setEmployeeId(employeeIdList);
        }

        order.setProductVersionId(productVersionId);
        order.setWorkStartTime(workStartTime);
        order.setWorkEndTime(workEndTime);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        wrapper.setQueryBeWorkOrderOrder(order);
        wrapper.setLoginName(loginName);

        QueryBeWorkOrderResult result = topologyService.queryBeWorkOrderList(wrapper);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryReportList[%s] error", order));
        }

        return result;
    }

    /**
     * 分页查找项目列表
     */
    @RequestMapping("/order/queryBeProjectList")
    public QueryBeProjectResult queryBeProjectList(          int pageNo,
                                                             int pageSize,
                                                             String projectName,
                                                             BeProjectType beProjectType,
                                                             ProjectStatus projectStatus) {
        QueryBeProjectOrderWrapper wrapper = new QueryBeProjectOrderWrapper();
        QueryBeProjectOrder order = new QueryBeProjectOrder();
        order.setProjectStatus(projectStatus);
        order.setBeProjectType(beProjectType);
        order.setProjectName(projectName);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        wrapper.setIsFiltered(Boolean.TRUE);
        wrapper.setQueryBeProjectOrder(order);

        ManagerInfo manager = CurrentManagerAssert.current();
        wrapper.setLoginUserName(manager.getManagerId());

        QueryBeProjectResult result = topologyService.queryBeProjectList(wrapper);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeProjectList[%s] error", order));
        }

        return result;
    }

    /**
     * 查询该项目下的所有项目工作
     */
    @RequestMapping("/order/queryBeProjectWorkList")
    public QueryBeProjectWorkResult queryBeProjectWorkList(Long projectId) {
        QueryBeProjectWorkOrder order = new QueryBeProjectWorkOrder();
        order.setProjectId(projectId);
        QueryBeProjectWorkResult result = topologyService.queryBeProjectWorkList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeProjectWorkList[%s] error", order));
        }

        return result;
    }

    /**
     * 查询所有系统信息
     */
    @RequestMapping("/order/queryBeSystemList")
    public QueryBeSystemResult queryBeSystemList(Long productId) {
        QueryBeSystemOrder order = new QueryBeSystemOrder();
        order.setProductId(productId);
        QueryBeSystemResult result = topologyService.queryBeSystemList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    "queryBeSystemList error");
        }

        return result;
    }

    @RequestMapping("/order/queryBeProjectListWithPreSaleProject")
    public QueryBeProjectResult queryBeProjectListWithPreSaleProject(
                                                             int pageNo,
                                                             int pageSize,
                                                             String projectName,
                                                             BeProjectType beProjectType,
                                                             ProjectStatus projectStatus) {
        QueryBeProjectOrderWrapper wrapper = new QueryBeProjectOrderWrapper();
        QueryBeProjectOrder order = new QueryBeProjectOrder();
        order.setProjectStatus(projectStatus);
        order.setBeProjectType(beProjectType);
        order.setProjectName(projectName);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        wrapper.setIsFiltered(Boolean.FALSE);
        wrapper.setQueryBeProjectOrder(order);

        ManagerInfo manager = CurrentManagerAssert.current();
        wrapper.setLoginUserName(manager.getManagerId());

        QueryBeProjectResult result = topologyService.queryBeProjectList(wrapper);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeProjectList[%s] error", order));
        }

        return result;
    }

    /**
     * 分页查找产品列表
     */
    @RequestMapping("/order/queryBeProductList")
    public QueryBeProductResult queryBeProductList(          int pageNo,
                                                             int pageSize,
                                                             String productName,
                                                             String productCode,
                                                             ProductDevStatus productDevStatus) {

        QueryBeProductOrder order = new QueryBeProductOrder();
        order.setProductDevStatus(productDevStatus);
        order.setProductCode(productCode);
        order.setProductName(productName);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryBeProductResult result = topologyService.queryBeProductList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeProductList[%s] error", order));
        }

        return result;
    }

    /**
     * 分页查找项目权限列表
     */
    @RequestMapping("/order/queryBeProjectAccessListByLoginName")
    public QueryBeProjectAccessResult queryBeProjectAccessListByLoginName(
                                                             String loginName,
                                                             String projectName) {

        if(StringUtils.isEmpty(loginName)){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getMessage(),
                    String.format("queryBeProjectAccessList error,[%s] is null", loginName));
        }

        QueryBeProjectAccessOrderWrapper wrapper = new QueryBeProjectAccessOrderWrapper();
        wrapper.setProjectName(projectName);
        wrapper.setLoginName(loginName);

        QueryBeProjectAccessResult result = topologyService.queryBeProjectAccessListByLoginName(wrapper);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeProjectAccessList[%s] error", wrapper));
        }

        return result;
    }


    @RequestMapping("/order/queryBeProductVersionList")
    public QueryBeProductVersionResult queryBeProductVersionList(          int pageNo,
                                                                    int pageSize,
                                                                    Long productId,
                                                                    VersionStatus versionStatus) {

        QueryBeProductVersionOrder order = new QueryBeProductVersionOrder();
        order.setVersionStatus(versionStatus);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);
        order.setProductId(productId);

        QueryBeProductVersionResult result = topologyService.queryBeProductVersionList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeProductVersionList[%s] error", order));
        }

        return result;
    }

    @RequestMapping("/order/queryBeEmployeeList")
    public QueryBeEmployeeResult queryBeEmployeeList(                int pageNo,
                                                                     int pageSize,
                                                                     Long id,
                                                                     String name,
                                                                     EmployeeType employeeType,
                                                                     ServiceStatus serviceStatus,
                                                                     Long superiorId) {

        QueryBeEmployeeOrder order = new QueryBeEmployeeOrder();
        order.setPenName(name);
        order.setId(id);
        order.setSuperiorId(superiorId);
        order.setServiceStatus(serviceStatus);
        order.setEmployeeType(employeeType);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        QueryBeEmployeeResult result = topologyService.queryBeEmployeeList(order);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeEmployeeList[%s] error", order));
        }

        return result;
    }

    /**
     * 新增一个接口，根据workLoginName查询所有下属的工单
     */
    @RequestMapping("/order/queryBeWorkOrderListByLoginName")
    public QueryBeWorkOrderResult queryBeWorkOrderListByLoginName(
                                                             int pageNo,
                                                             int pageSize,
                                                             RequirementType requirementType,
                                                             WorkOrderType workOrderType,
                                                             WorkOrderStatus workOrderStatus,
                                                             Long employeeId,
                                                             String loginName,
                                                             Long productVersionId,
                                                             @DateTimeFormat(pattern="yyyy-MM-dd")Date workStartTime,
                                                             @DateTimeFormat(pattern="yyyy-MM-dd")Date workEndTime) {

        QueryBeWorkOrderOrderWrapper wrapper = new QueryBeWorkOrderOrderWrapper();

        QueryBeWorkOrderOrder order = new QueryBeWorkOrderOrder();
        order.setRequirementType(requirementType);
        order.setWorkOrderType(workOrderType);
        order.setWorkOrderStatus(workOrderStatus);
//        order.setEmployeeId(employeeId);
        order.setProductVersionId(productVersionId);
        order.setWorkStartTime(workStartTime);
        order.setWorkEndTime(workEndTime);
        order.setPageNo(pageNo);
        order.setPageSize(pageSize);

        wrapper.setQueryBeWorkOrderOrder(order);
        wrapper.setLoginName(loginName);
        wrapper.setFilteredEmployeeId(employeeId);


        QueryBeWorkOrderResult result = topologyService.queryBeWorkOrderListByLoginName(wrapper);

        if(!result.isSuccess()){
            throw new BizException(result.getStatus(), result.getMessage(),
                    String.format("queryBeWorkOrderListByLoginName[%s] error", order));
        }

        return result;
    }

    /**
     * 删除工时工单
     */
    @RequestMapping("/order/deleteBeWorkOrder")
    public EmptyResult deleteBeWorkOrder(Long id) {
        DeleteBeWorkOrderOrder order = new DeleteBeWorkOrderOrder();
        order.setId(id);
        return topologyService.deleteBeWorkOrder(order);
    }


    /**
     * 删除项目权限
     */
    @RequestMapping("/order/deleteBeProcessAccess")
    public EmptyResult deleteBeProcessAccess(Long id) {
        DeleteBeProjectAccessOrder order = new DeleteBeProjectAccessOrder();
        order.setId(id);

        return topologyService.deleteBeProjectAccess(order);
    }

    /**
     * 批量审批工单接口
     * ids,以分号分割
     */
    @RequestMapping("/order/batchApproveOrRejectBeWorkOrder")
    public EmptyResult batchApproveOrRejectBeWorkOrder(
            String ids,
            WorkOrderStatus workOrderStatus,
            String rejectReason
    ) {

        if(StringUtils.isEmpty(ids)){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getMessage(),
                    "batchApproveOrRejectBeWorkOrder error,ids is null");
        }

        EmptyResult result = new EmptyResult();
        BatchUpdateBeWorkOrderOrder order = new BatchUpdateBeWorkOrderOrder();

        List<Long> idList = Lists.newArrayList();
        for(String id : ids.split(";")){
            idList.add(Long.parseLong(id));
        }

        order.setIdList(idList);
        order.setWorkOrderStatus(workOrderStatus);
        order.setRejectReason(rejectReason);

        result = topologyService.batchApproveOrRejectBeWorkOrder(order);

        return result;
    }

    /**
     * 员工信息新增或更新接口
     */
    @RequestMapping("/order/addOrModifyBeEmployee")
    public EmptyResult addOrModifyBeEmployee(Long id,
                                              String name,
                                              String penName,
                                              String workLoginName,
                                              String phoneNum,
                                              Long superiorId,
                                              EmployeeType employeeType,
                                              PostType postType,
                                              Long employeeLevel,
                                              Long unitCost,
                                              String certNo,
                                              String sex,
                                              String education,
                                              Long salary,
                                              String baseSite,
                                              String major,
                                              Long workAge,
                                              String resumeFile,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date entryTime,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date leaveTime,
                                              ServiceStatus serviceStatus
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeEmployeeOrder order = new AddOrModifyBeEmployeeOrder();
        order.setId(id);
        order.setName(name);
        order.setPenName(penName);
        order.setWorkLoginName(workLoginName);
        order.setPhoneNum(phoneNum);
        order.setSuperiorId(superiorId);
        order.setEmployeeType(employeeType);
        order.setPostType(postType);
        order.setEmployeeLevel(employeeLevel);
        order.setUnitCost(unitCost);
        order.setCertNo(certNo);
        order.setSex(sex);
        order.setEducation(education);
        order.setSalary(salary);
        order.setBaseSite(baseSite);
        order.setMajor(major);
        order.setWorkAge(workAge);
        order.setResumeFile(resumeFile);
        order.setEntryTime(entryTime);
        order.setLeaveTime(leaveTime);
        order.setServiceStatus(serviceStatus);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeEmployeeOrder findOrder = new FindBeEmployeeOrder();
            findOrder.setId(id);
            FindBeEmployeeResult findResult = topologyService.findBeEmployee(findOrder);
            if(findResult.isSuccess()){
                BeEmployeeDTO beEmployeeDTO = findResult.getBeEmployeeDTO();
                if(name == null) order.setName(beEmployeeDTO.getName());
                if(penName == null) order.setPenName(beEmployeeDTO.getPenName());
                if(workLoginName == null) order.setWorkLoginName(beEmployeeDTO.getWorkLoginName());
                if(phoneNum == null) order.setPhoneNum(beEmployeeDTO.getPhoneNum());
                if(superiorId == null) order.setSuperiorId(beEmployeeDTO.getSuperiorId());
                if(employeeType == null) order.setEmployeeType(beEmployeeDTO.getEmployeeType());
                if(postType == null) order.setPostType(beEmployeeDTO.getPostType());
                if(employeeLevel == null) order.setEmployeeLevel(beEmployeeDTO.getEmployeeLevel());
                if(unitCost == null) order.setUnitCost(beEmployeeDTO.getUnitCost());
                if(certNo == null) order.setCertNo(beEmployeeDTO.getCertNo());
                if(sex == null) order.setSex(beEmployeeDTO.getSex());
                if(education == null) order.setEducation(beEmployeeDTO.getEducation());
                if(salary == null) order.setSalary(beEmployeeDTO.getSalary()==null?0L:Long.parseLong(beEmployeeDTO.getSalary()));
                if(baseSite == null) order.setBaseSite(beEmployeeDTO.getBaseSite());
                if(major == null) order.setMajor(beEmployeeDTO.getMajor());
                if(workAge == null) order.setWorkAge(beEmployeeDTO.getWorkAge());
                if(resumeFile == null) order.setResumeFile(beEmployeeDTO.getResumeFile());
                if(entryTime == null) order.setEntryTime(beEmployeeDTO.getEntryTime());
                if(leaveTime == null) order.setLeaveTime(beEmployeeDTO.getLeaveTime());
                if(serviceStatus == null) order.setServiceStatus(beEmployeeDTO.getServiceStatus());
            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeEmployee(order);

        return result;
    }

    /**
     * 删除员工信息
     */
    @RequestMapping("/order/deleteBeEmployee")
    public EmptyResult deleteBeEmployee(Long id) {
        DeleteBeEmployeeOrder order = new DeleteBeEmployeeOrder();
        order.setId(id);

        return topologyService.deleteBeEmployee(order);
    }

    /**
     * 删除项目工作
     */
    @RequestMapping("/order/deleteBeProjectWork")
    public EmptyResult deleteBeProjectWork(Long id) {
        DeleteBeProjectWorkOrder order = new DeleteBeProjectWorkOrder();
        order.setId(id);

        return topologyService.deleteBeProjectWork(order);
    }

    /**
     * 删除产品信息
     */
    @RequestMapping("/order/deleteBeProduct")
    public EmptyResult deleteBeProduct(Long id) {
        DeleteBeProductOrder order = new DeleteBeProductOrder();
        order.setId(id);

        return topologyService.deleteBeProduct(order);
    }

    /**
     * 删除产品版本信息
     */
    @RequestMapping("/order/deleteBeProductVersion")
    public EmptyResult deleteBeProductVersion(Long id) {
        DeleteBeProductVersionOrder order = new DeleteBeProductVersionOrder();
        order.setId(id);

        return topologyService.deleteBeProductVersion(order);
    }

    /**
     * 删除项目信息
     */
    @RequestMapping("/order/deleteBeProject")
    public EmptyResult deleteBeProject(Long id) {
        DeleteBeProjectOrder order = new DeleteBeProjectOrder();
        order.setId(id);

        return topologyService.deleteBeProject(order);
    }

    /**
     * 产品信息新增或更新接口
     */
    @RequestMapping("/order/addOrModifyBeProduct")
    public EmptyResult addOrModifyBeProduct(Long id,
                                            Long parentId,
                                            String productName,
                                            String productCode,
                                            String pdName,
                                            String devName,
                                            String approvePeople,
                                            String quotationScope,
                                            String maturityDegree,
                                            ProductDevStatus productDevStatus,
                                            ProductClass productClass
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeProductOrder order = new AddOrModifyBeProductOrder();
        order.setId(id);
        order.setParentId(parentId);
        order.setProductName(productName);
        order.setProductCode(productCode);
        order.setPdName(pdName);
        order.setDevName(devName);
        order.setApprovePeople(approvePeople);
        order.setQuotationScope(quotationScope);
        order.setMaturityDegree(maturityDegree);
        order.setProductDevStatus(productDevStatus);
        order.setProductClass(productClass);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeProductOrder findOrder = new FindBeProductOrder();
            findOrder.setId(id);
            FindBeProductResult findResult = topologyService.findBeProduct(findOrder);
            if(findResult.isSuccess()){
                BeProductDTO beProductDTO =findResult.getBeProductDTO();
                if(parentId == null) order.setParentId(beProductDTO.getParentId());
                if(productName == null) order.setProductName(beProductDTO.getProductName());
                if(productCode == null) order.setProductCode(beProductDTO.getProductCode());
                if(pdName == null) order.setPdName(beProductDTO.getPdName());
                if(devName == null) order.setDevName(beProductDTO.getDevName());
                if(approvePeople == null) order.setApprovePeople(beProductDTO.getApprovePeople());
                if(quotationScope == null) order.setQuotationScope(beProductDTO.getQuotationScope());
                if(maturityDegree == null) order.setMaturityDegree(beProductDTO.getMaturityDegree());
                if(productDevStatus == null) order.setProductDevStatus(beProductDTO.getProductDevStatus());
                if(productClass == null) order.setProductClass(beProductDTO.getProductClass());
            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeProduct(order);

        return result;
    }

    /**
     * 项目工作新增或更新接口
     */
    @RequestMapping("/order/addOrModifyBeProjectWork")
    public EmptyResult addOrModifyBeProjectWork(Long id,
                                            String workTitle,
                                            String loginName,
                                            Long projectId
    ) {
        EmptyResult result = new EmptyResult();
        AddOrModifyBeProjectWorkOrder order = new AddOrModifyBeProjectWorkOrder();
        order.setId(id);
        order.setWorkTitle(workTitle);
        order.setLoginName(loginName);
        order.setProjectId(projectId);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeProjectWorkOrder findOrder = new FindBeProjectWorkOrder();
            findOrder.setId(id);
            FindBeProjectWorkResult findResult = topologyService.findBeProjectWork(findOrder);
            if(findResult.isSuccess()){
                BeProjectWorkDTO beProjectWorkDTO =findResult.getBeProjectWorkDTO();
                if(workTitle == null) order.setWorkTitle(beProjectWorkDTO.getWorkTitle());
                if(projectId == null) order.setProjectId(beProjectWorkDTO.getProjectId());
            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeProjectWork(order);
        return result;
    }

    /**
     * 产品版本信息新增或更新接口
     */
    @RequestMapping("/order/addOrModifyBeProductVersion")
    public EmptyResult addOrModifyBeProductVersion(Long id,
                                            Long productId,
                                            String versionNum,
                                            String versionPdName,
                                            String versionDevName,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date releaseTime,
                                            String releaseUrl,
                                            VersionStatus versionStatus
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeProductVersionOrder order = new AddOrModifyBeProductVersionOrder();
        order.setId(id);
        order.setProductId(productId);
        order.setVersionNum(versionNum);
        order.setVersionPdName(versionPdName);
        order.setVersionDevName(versionDevName);
        order.setReleaseTime(releaseTime);
        order.setReleaseUrl(releaseUrl);
        order.setVersionStatus(versionStatus);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeProductVersionOrder findOrder = new FindBeProductVersionOrder();
            findOrder.setId(id);
            FindBeProductVersionResult findResult = topologyService.findBeProductVersion(findOrder);
            if(findResult.isSuccess()){
                BeProductVersionDTO beProductVersionDTO =findResult.getBeProductVersionDTO();

                if(productId == null) order.setProductId(beProductVersionDTO.getProductId());
                if(versionNum == null) order.setVersionNum(beProductVersionDTO.getVersionNum());
                if(versionPdName == null) order.setVersionPdName(beProductVersionDTO.getVersionPdName());
                if(versionDevName == null) order.setVersionDevName(beProductVersionDTO.getVersionDevName());
                if(releaseTime == null) order.setReleaseTime(beProductVersionDTO.getReleaseTime());
                if(releaseUrl == null) order.setReleaseUrl(beProductVersionDTO.getReleaseUrl());
                if(versionStatus == null) order.setVersionStatus(beProductVersionDTO.getVersionStatus());

            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeProductVersion(order);

        return result;
    }

    /**
     * 产品信息新增或更新接口
     */
    @RequestMapping("/order/addOrModifyBeProject")
    public EmptyResult addOrModifyBeProject(Long id,
                                            String projectName,
                                            String projectCode,
                                            String projectMemo,
                                            BeProjectType beProjectType,
                                            Long customerId,
                                            String accessPeople,
                                            String customerContact,
                                            String customerContactInfo,
                                            String bdName,
                                            String saName,
                                            String pmName,
                                            String stoName,
                                            String approvePeople,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                            String productIds,
                                            ProjectStatus projectStatus
    ) {

        EmptyResult result = new EmptyResult();
        AddOrModifyBeProjectOrder order = new AddOrModifyBeProjectOrder();
        order.setId(id);
        order.setProjectName(projectName);
        order.setProjectCode(projectCode);
        order.setProjectMemo(projectMemo);
        order.setBeProjectType(beProjectType);
        order.setCustomerId(customerId);
        order.setAccessPeople(accessPeople);
        order.setCustomerContact(customerContact);
        order.setCustomerContactInfo(customerContactInfo);
        order.setBdName(bdName);
        order.setSaName(saName);
        order.setPmName(pmName);
        order.setStoName(stoName);
        order.setApprovePeople(approvePeople);
        order.setBeginTime(beginTime);
        order.setEndTime(endTime);
        order.setProductIds(productIds);
        order.setProjectStatus(projectStatus);

        // 如果id不为空，则是更新操作，需要先补齐为空的属性
        if(id != null && id.longValue() >0){
            FindBeProjectOrder findOrder = new FindBeProjectOrder();
            findOrder.setId(id);
            FindBeProjectResult findResult = topologyService.findBeProject(findOrder);
            if(findResult.isSuccess()){
                BeProjectDTO beProjectDTO =findResult.getBeProjectDTO();
                if(projectName == null) order.setProjectName(beProjectDTO.getProjectName());
                if(projectCode == null) order.setProjectCode(beProjectDTO.getProjectCode());
                if(projectMemo == null) order.setProjectMemo(beProjectDTO.getProjectMemo());
                if(beProjectType == null) order.setBeProjectType(beProjectDTO.getBeProjectType());
                if(customerId == null) order.setCustomerId(beProjectDTO.getCustomerId());
                if(accessPeople == null) order.setAccessPeople(beProjectDTO.getAccessPeople());
                if(customerContact == null) order.setCustomerContact(beProjectDTO.getCustomerContact());
                if(customerContactInfo == null) order.setCustomerContactInfo(beProjectDTO.getCustomerContactInfo());
                if(bdName == null) order.setBdName(beProjectDTO.getBdName());
                if(saName == null) order.setSaName(beProjectDTO.getSaName());
                if(pmName == null) order.setPmName(beProjectDTO.getPmName());
                if(stoName == null) order.setStoName(beProjectDTO.getStoName());
                if(approvePeople == null) order.setApprovePeople(beProjectDTO.getApprovePeople());
                if(beginTime == null) order.setBeginTime(beProjectDTO.getBeginTime());
                if(endTime == null) order.setEndTime(beProjectDTO.getEndTime());
                if(productIds == null) order.setProductIds(beProjectDTO.getProductIds());
                if(projectStatus == null) order.setProjectStatus(beProjectDTO.getProjectStatus());

            }else{
                result.setStatus(findResult.getStatus());
                result.setCode(findResult.getCode());
                result.setMessage(findResult.getMessage());
                return result;
            }
        }

        result = topologyService.addOrModifyBeProject(order);

        return result;
    }

    @RequestMapping("/order/isFinishedThisWeek")
    public WeekFinishResult isFinishedThisWeek(@DateTimeFormat(pattern = "yyyy-MM-dd") Date monday,String loginName){

        WeekFinishResult result = new WeekFinishResult();
        if(StringUtils.isEmpty(loginName))throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getMessage(),"isFinishedThisWeek error,loginName is null");
        if(monday == null) throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getMessage(),"isFinishedThisWeek error,monday is null");

        WeekFinishWorkOrderOrder order = new WeekFinishWorkOrderOrder();
        order.setMonday(monday);
        order.setLoginName(loginName);

        result = topologyService.isFinishedThisWeek(order);

        return result;
    }

    @RequestMapping("/order/noticeLeader")
    public EmptyResult noticeLeader(
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date monday,
            Long leaderId){
        EmptyResult result = new EmptyResult();
        QueryBeEmployeeResult queryLeaderResult
                = queryBeEmployeeList(1,1000,leaderId,null,null,null,null);

        if(!queryLeaderResult.isSuccess()){
            result.setStatus(queryLeaderResult.getStatus());
            result.setMessage(queryLeaderResult.getMessage());
            result.setCode(queryLeaderResult.getCode());
            return result;
        }

        List<BeEmployeeDTO> leaderList = queryLeaderResult.getInfos();
        if(CollectionUtils.isEmpty(leaderList)){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getMessage(),
                    String.format("noticeLeader error,[%s] query null", leaderId));
        }

        BeEmployeeDTO leader = leaderList.get(0);
        String phoneNum = leader.getPhoneNum();
        Long superiorId = leader.getId();
        QueryBeEmployeeResult queryFollowerResult
                = queryBeEmployeeList(1,1000,null,null,null,null,superiorId);

        if(!queryFollowerResult.isSuccess()){
            result.setStatus(queryFollowerResult.getStatus());
            result.setMessage(queryFollowerResult.getMessage());
            result.setCode(queryFollowerResult.getCode());
            return result;
        }

        List<BeEmployeeDTO> followerList = queryFollowerResult.getInfos();

        if(CollectionUtils.isEmpty(followerList)){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getMessage(),
                    String.format("noticeLeader error,[%s] follows query null", leaderId));
        }

        StringBuffer message = new StringBuffer("您团队在")
                .append(DateFormatUtils.format(monday,"yyyy-MM-dd"))
                .append("~")
                .append(DateFormatUtils.format(DateUtils.addDays(monday,5),"yyyy-MM-dd")).append("期间");

        AtomicBoolean isAllFinish = new AtomicBoolean(true);
        List<String> unfinishedEmployeeList = Lists.newArrayList();
        followerList.stream().forEach(employee->{
            WeekFinishResult finishedResult = isFinishedThisWeek(monday,employee.getWorkLoginName());

            if(!finishedResult.isSuccess()){
                result.setStatus(finishedResult.getStatus());
                result.setMessage(finishedResult.getMessage());
                result.setCode(finishedResult.getCode());
                return;
            }

            BeEmployeeDTO beEmployeeDTO = finishedResult.getBeEmployeeDTO();
            if(PostType.BD.equals(beEmployeeDTO.getPostType())){
                // BD不用填工时
                return;
            }

            if(!finishedResult.getIsFinishedThisWeek()){
                isAllFinish.set(false);
                unfinishedEmployeeList.add(employee.getPenName());
            }
        });

        if(isAllFinish.get()){
            message.append("已经完成全员申报！");
        }else{
            message.append("未完成全员申报，未完成的成员包括");
            unfinishedEmployeeList.stream().forEach(name->message.append(name).append(","));
        }
        message.append("请及时敦促以上人员完成工时申报！");

        WorkOrderDingdingUtils.sendMessageAtChosePerson(message.toString(),phoneNum);

        return result;
    }

    @RequestMapping("/order/checkLeaders")
    public EmptyResult checkLeaders(
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date monday){
        EmptyResult result = new EmptyResult();
        List<Long> leaders = Lists.newArrayList();
        leaders.add(1L);
        leaders.add(2L);
        leaders.add(3L);
        leaders.add(4L);
        leaders.add(5L);
        leaders.add(6L);
        leaders.add(7L);
        leaders.add(9L);
        leaders.add(10L);
        leaders.add(26L);
        leaders.add(43L);
        leaders.add(47L);
        leaders.add(10L);

        for(Long leader : leaders){
            noticeLeader(monday,leader);
        }
        return result;
    }

}
