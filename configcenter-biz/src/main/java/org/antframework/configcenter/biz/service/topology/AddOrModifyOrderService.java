/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeCustomerInfoDao;
import org.antframework.configcenter.dal.dao.BeDeployOrderDao;
import org.antframework.configcenter.dal.dao.BeTopologyNodeDao;
import org.antframework.configcenter.dal.entity.BeCustomerInfo;
import org.antframework.configcenter.dal.entity.BeDeployOrder;
import org.antframework.configcenter.dal.entity.BeTopologyNode;
import org.antframework.configcenter.facade.order.topology.AddOrModifyNodeOrder;
import org.antframework.configcenter.facade.order.topology.AddOrModifyOrderOrder;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改部署工单
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyOrderService {
    // 应用dao
    private final BeTopologyNodeDao beTopologyNodeDao;

    private final BeCustomerInfoDao beCustomerInfoDao;

    private final BeDeployOrderDao beDeployOrderDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyOrderOrder, EmptyResult> context) {
        AddOrModifyOrderOrder order = context.getOrder();
        String customerId = order.getCustomerId();
        if(customerId == null || customerId.length() == 0){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerId为空");
        }

        BeCustomerInfo customerInfo = beCustomerInfoDao.findById(Long.parseLong(customerId));
        if(customerInfo == null){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerInfo为空，customerId="+customerId);
        }

        String withNodeId = order.getWithNodeId();
        if(StringUtils.isNotEmpty(withNodeId)){
            BeTopologyNode withNode = beTopologyNodeDao.findById(Long.parseLong(withNodeId));
            if(withNode == null){
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "withNode为空，withNodeId="+withNodeId);
            }
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyOrderOrder, EmptyResult> context) {
        AddOrModifyOrderOrder order = context.getOrder();
        BeDeployOrder beDeployOrder = new BeDeployOrder();
        beDeployOrder.setId(order.getId());
        beDeployOrder.setDeployEnv(order.getDeployEnv());
        beDeployOrder.setDeployOwner(order.getDeployOwner());
        beDeployOrder.setDeployType(order.getDeployType());
        beDeployOrder.setDeployTime(order.getDeployTime());
        beDeployOrder.setConnectGlab(order.getConnectGlab());
        beDeployOrder.setCustomerId(order.getCustomerId());
        beDeployOrder.setFinishTest(order.getFinishTest());
        beDeployOrder.setMachineConfig(order.getMachineConfig());
        beDeployOrder.setNetworkConfig(order.getNetworkConfig());
        beDeployOrder.setInstId(order.getInstId());
        beDeployOrder.setNodeCode(order.getNodeCode());
        beDeployOrder.setScene(order.getScene());
        beDeployOrder.setOrderStatus(order.getOrderStatus());
        beDeployOrder.setOrderType(order.getOrderType());
        beDeployOrder.setProductType(order.getProductType());
        beDeployOrder.setProductVersion(order.getProductVersion());
        beDeployOrder.setProjectOwner(order.getProjectOwner());
        beDeployOrder.setApproveUser(order.getApproveUser());
        beDeployOrder.setDeployArchDoc(order.getDeployArchDoc());
        beDeployOrder.setWithNodeId(order.getWithNodeId());
        beDeployOrder.setTestCostTime(order.getTestCostTime());
        beDeployOrder.setMessage(order.getMessage());
        beDeployOrder.setLicenseDetail(order.getLicenseDetail());
        beDeployOrder.setMid(order.getMid());
        beDeployOrder.setLicenseExpireTime(order.getLicenseExpireTime());
        beDeployOrder.setLicenseValidDate(order.getLicenseValidDate());
        beDeployOrder.setFinishLuopan(order.getFinishLuopan());
        beDeployOrder.setDeployCostTime(order.getDeployCostTime());
        beDeployOrder.setDeliverType(order.getDeliverType());
        beDeployOrder.setNodeName(order.getNodeName());
        beDeployOrder.setNodeType(order.getNodeType());
        beDeployOrder.setProjectType(order.getProjectType());
        beDeployOrder.setDeployOwner(order.getDeployOwner());
        beDeployOrder.setPlanDeployTime(order.getPlanDeployTime());
        beDeployOrder.setConfirmLetter(order.getConfirmLetter());
        beDeployOrder.setCertificateDetail(order.getCertificateDetail());
        beDeployOrder.setLuopanDetail(order.getLuopanDetail());
        beDeployOrder.setCustomizeDetail(order.getCustomizeDetail());

        beDeployOrderDao.save(beDeployOrder);
    }

}
