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
import org.antframework.configcenter.dal.dao.BePreSaleOrderDao;
import org.antframework.configcenter.dal.entity.BePreSaleOrder;
import org.antframework.configcenter.facade.order.topology.AddOrModifyPreSaleOrderOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改部署工单
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyPreSaleOrderService {

    private final BeCustomerInfoDao beCustomerInfoDao;

    private final BePreSaleOrderDao bePreSaleOrderDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyPreSaleOrderOrder, EmptyResult> context) {
        AddOrModifyPreSaleOrderOrder order = context.getOrder();
        String customerId = order.getCustomerId();
        if(customerId == null || customerId.length() == 0){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerId为空");
        }

//        BeCustomerInfo customerInfo = beCustomerInfoDao.findById(Long.parseLong(customerId));
//        if(customerInfo == null){
//            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerInfo为空，customerId="+customerId);
//        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyPreSaleOrderOrder, EmptyResult> context) {
        AddOrModifyPreSaleOrderOrder order = context.getOrder();
        BePreSaleOrder bePreSaleOrder = new BePreSaleOrder();
        bePreSaleOrder.setId(order.getId());
        bePreSaleOrder.setDeployOwner(order.getDeployOwner());
        bePreSaleOrder.setCustomerId(order.getCustomerId());
        bePreSaleOrder.setScene(order.getScene());
        bePreSaleOrder.setProductType(order.getProductType());
        bePreSaleOrder.setProjectOwner(order.getProjectOwner());
        bePreSaleOrder.setDeployOwner(order.getDeployOwner());
        bePreSaleOrder.setPreSaleOrderStatus(order.getPreSaleOrderStatus());
        bePreSaleOrder.setPreSaleOrderType(order.getPreSaleOrderType());
        bePreSaleOrder.setContent(order.getContent());
        bePreSaleOrder.setFinishContent(order.getFinishContent());
        bePreSaleOrder.setFinishDate(order.getFinishDate());
        bePreSaleOrder.setProjectName(order.getProjectName());
        bePreSaleOrder.setPlanFinishDate(order.getPlanFinishDate());
        bePreSaleOrder.setOrderFirstCreateDate(order.getOrderFirstCreateDate());
        bePreSaleOrder.setDispatchOwner(order.getDispatchOwner());
        bePreSaleOrder.setScore1(order.getScore1());
        bePreSaleOrder.setScore1Content(order.getScore1Content());
        bePreSaleOrder.setScore2(order.getScore2());
        bePreSaleOrder.setScore2Content(order.getScore2Content());
        bePreSaleOrder.setRejectReason(order.getRejectReason());
        bePreSaleOrder.setOrderDispatchDate(order.getOrderDispatchDate());

        bePreSaleOrderDao.save(bePreSaleOrder);
    }

}
