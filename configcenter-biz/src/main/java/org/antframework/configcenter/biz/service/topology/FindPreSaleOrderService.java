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
import org.antframework.configcenter.dal.dao.BePreSaleOrderDao;
import org.antframework.configcenter.dal.entity.BeDeployOrder;
import org.antframework.configcenter.dal.entity.BePreSaleOrder;
import org.antframework.configcenter.facade.info.topology.BeDeployOrderDTO;
import org.antframework.configcenter.facade.info.topology.BePreSaleOrderDTO;
import org.antframework.configcenter.facade.order.topology.FindDeployOrderOrder;
import org.antframework.configcenter.facade.order.topology.FindPreSaleOrderOrder;
import org.antframework.configcenter.facade.result.topology.FindDeployOrderResult;
import org.antframework.configcenter.facade.result.topology.FindPreSaleOrderResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找工单服务
 */
@Service
@AllArgsConstructor
public class FindPreSaleOrderService {

    // 分支dao
    private final BePreSaleOrderDao bePreSaleOrderDao;

    @ServiceBefore
    public void before(ServiceContext<FindPreSaleOrderOrder, FindPreSaleOrderResult> context) {
        FindPreSaleOrderOrder order = context.getOrder();

        Long orderId = order.getPreSaleOrderId();
        BePreSaleOrder bePreSaleOrder = bePreSaleOrderDao.findById(orderId);
        if (bePreSaleOrder == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("工单[%s]不存在", orderId));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindPreSaleOrderOrder, FindPreSaleOrderResult> context) {
        FindPreSaleOrderOrder findDeployOrderOrder = context.getOrder();
        FindPreSaleOrderResult result = context.getResult();
        BePreSaleOrder order = bePreSaleOrderDao.findById(findDeployOrderOrder.getPreSaleOrderId());

        BePreSaleOrderDTO bePreSaleOrderDTO = new BePreSaleOrderDTO();
        bePreSaleOrderDTO.setPreSaleOrderStatus(order.getPreSaleOrderStatus());
        bePreSaleOrderDTO.setPreSaleOrderType(order.getPreSaleOrderType());
        bePreSaleOrderDTO.setCustomerId(order.getCustomerId());
        bePreSaleOrderDTO.setDeployOwner(order.getDeployOwner());
        bePreSaleOrderDTO.setId(order.getId());
        bePreSaleOrderDTO.setContent(order.getContent());
        bePreSaleOrderDTO.setDispatchOwner(order.getDispatchOwner());
        bePreSaleOrderDTO.setFinishContent(order.getFinishContent());
        bePreSaleOrderDTO.setFinishDate(order.getFinishDate());
        bePreSaleOrderDTO.setFinishContent(order.getFinishContent());
        bePreSaleOrderDTO.setProjectOwner(order.getProjectOwner());
        bePreSaleOrderDTO.setOrderDispatchDate(order.getOrderDispatchDate());
        bePreSaleOrderDTO.setOrderFirstCreateDate(order.getOrderFirstCreateDate());
        bePreSaleOrderDTO.setScore1(order.getScore1());
        bePreSaleOrderDTO.setScore1Content(order.getScore1Content());
        bePreSaleOrderDTO.setScore2(order.getScore2());
        bePreSaleOrderDTO.setScore2Content(order.getScore2Content());
        bePreSaleOrderDTO.setRejectReason(order.getRejectReason());
        bePreSaleOrderDTO.setScene(order.getScene());
        bePreSaleOrderDTO.setProductType(order.getProductType());
        bePreSaleOrderDTO.setProjectName(order.getProjectName());
        bePreSaleOrderDTO.setPlanFinishDate(order.getPlanFinishDate());

        result.setBePreSaleOrderDTO(bePreSaleOrderDTO);
    }
}
