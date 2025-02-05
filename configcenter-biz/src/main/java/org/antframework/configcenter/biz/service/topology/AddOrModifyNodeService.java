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
import org.antframework.configcenter.dal.dao.BeTopologyNodeDao;
import org.antframework.configcenter.dal.entity.BeCustomerInfo;
import org.antframework.configcenter.dal.entity.BeTopologyNode;
import org.antframework.configcenter.facade.order.topology.AddOrModifyNodeOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改应用服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyNodeService {
    // 应用dao
    private final BeTopologyNodeDao beTopologyNodeDao;

    private final BeCustomerInfoDao beCustomerInfoDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyNodeOrder, EmptyResult> context) {
        AddOrModifyNodeOrder order = context.getOrder();
        String customerId = order.getCustomerId();
        if(customerId == null || customerId.length() == 0){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerId为空");
        }

        BeCustomerInfo customerInfo = beCustomerInfoDao.findById(Long.parseLong(customerId));
        if(customerInfo == null){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerInfo为空，customerId="+customerId);
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyNodeOrder, EmptyResult> context) {
        AddOrModifyNodeOrder order = context.getOrder();

        BeCustomerInfo customerInfo = beCustomerInfoDao.findById(Long.parseLong(order.getCustomerId()));

        Long nodeId = order.getId();
        BeTopologyNode beTopologyNode = null;
        if(nodeId != null && nodeId.longValue() > 0){
            beTopologyNode = beTopologyNodeDao.findById(nodeId);
        }

        if(beTopologyNode == null) {
            beTopologyNode = new BeTopologyNode();
        }

        beTopologyNode.setLicenseDetail(order.getLicenseDetail());
        beTopologyNode.setMid(order.getMid());
        beTopologyNode.setNodeName(order.getNodeName());
        beTopologyNode.setInstId(order.getInstId());
        beTopologyNode.setNodeCode(order.getNodeCode());
        beTopologyNode.setNodeStatus(order.getNodeStatus());
        beTopologyNode.setNodeType(order.getNodeType());
        beTopologyNode.setConnectGlab(order.getConnectGlab());
        beTopologyNode.setDeployEnv(order.getDeployEnv());
        beTopologyNode.setDeployOwner(order.getDeployOwner());
        beTopologyNode.setFinishTest(order.getFinishTest());
        beTopologyNode.setDeployTime(order.getDeployTime());
        beTopologyNode.setCustomerId(customerInfo.getId().toString());
        beTopologyNode.setLicenseExpireTime(order.getLicenseExpireTime());
        beTopologyNode.setLicenseValidDate(order.getLicenseValidDate());
        beTopologyNode.setMachineConfig(order.getMachineConfig());
        beTopologyNode.setNetworkConfig(order.getNetworkConfig());
        beTopologyNode.setRemark(order.getRemark());
        beTopologyNode.setScene(order.getScene());
        beTopologyNode.setProjectOwner(order.getProjectOwner());
        beTopologyNode.setProjectType(order.getProjectType());
        beTopologyNode.setProductVersion(order.getProductVersion());
        beTopologyNode.setProductType(order.getProductType());
        beTopologyNode.setDeployType(order.getDeployType());

        beTopologyNodeDao.save(beTopologyNode);
    }

}
