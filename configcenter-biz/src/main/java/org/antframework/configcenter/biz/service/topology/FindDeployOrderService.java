/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeDeployOrderDao;
import org.antframework.configcenter.dal.entity.BeDeployOrder;
import org.antframework.configcenter.facade.info.topology.BeDeployOrderDTO;
import org.antframework.configcenter.facade.order.topology.FindDeployOrderOrder;
import org.antframework.configcenter.facade.result.topology.FindDeployOrderResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找工单服务
 */
@Service
@AllArgsConstructor
public class FindDeployOrderService {

    // 分支dao
    private final BeDeployOrderDao beDeployOrderDao;

    @ServiceBefore
    public void before(ServiceContext<FindDeployOrderOrder, FindDeployOrderResult> context) {
        FindDeployOrderOrder order = context.getOrder();

        Long orderId = order.getOrderId();
        BeDeployOrder beDeployOrder = beDeployOrderDao.findById(orderId);
        if (beDeployOrder == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("部署工单[%s]不存在", orderId));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindDeployOrderOrder, FindDeployOrderResult> context) {
        FindDeployOrderOrder findDeployOrderOrder = context.getOrder();
        FindDeployOrderResult result = context.getResult();
        BeDeployOrder order = beDeployOrderDao.findById(findDeployOrderOrder.getOrderId());

        BeDeployOrderDTO beDeployOrder = new BeDeployOrderDTO();

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

        result.setBeDeployOrderDTO(beDeployOrder);
    }
}
