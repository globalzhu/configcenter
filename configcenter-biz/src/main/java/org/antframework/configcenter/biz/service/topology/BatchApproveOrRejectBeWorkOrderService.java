/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.BeWorkOrderDao;
import org.antframework.configcenter.dal.entity.BeWorkOrder;
import org.antframework.configcenter.facade.order.topology.BatchUpdateBeWorkOrderOrder;
import org.antframework.configcenter.facade.vo.WorkOrderStatus;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 添加或修改工时工单
 */
@Service(enableTx = true)
@AllArgsConstructor
@Transactional
public class BatchApproveOrRejectBeWorkOrderService {

    private final BeWorkOrderDao beWorkOrderDao;

    @ServiceBefore
    public void before(ServiceContext<BatchUpdateBeWorkOrderOrder, EmptyResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<BatchUpdateBeWorkOrderOrder, EmptyResult> context) {
        BatchUpdateBeWorkOrderOrder order = context.getOrder();

        List<Long> idList = order.getIdList();
        WorkOrderStatus workOrderStatus = order.getWorkOrderStatus();
        String approveProgress = order.getApproveProgress();
        String rejectReason = order.getRejectReason();

        if(idList != null){
            for(Long id : idList){
                BeWorkOrder oldOne = beWorkOrderDao.findById(id);
                if(oldOne == null) continue;

                BeWorkOrder beWorkOrder = new BeWorkOrder();

                beWorkOrder.setId(id);
                beWorkOrder.setProjectId(oldOne.getProjectId());
                beWorkOrder.setProjectStatus(oldOne.getProjectStatus());
                beWorkOrder.setProductId(oldOne.getProductId());
                beWorkOrder.setProductVersionId(oldOne.getProductVersionId());
                beWorkOrder.setRequirementType(oldOne.getRequirementType());
                beWorkOrder.setWorkOrderType(oldOne.getWorkOrderType());
                beWorkOrder.setWorkDate(oldOne.getWorkDate());
                beWorkOrder.setManDay(oldOne.getManDay());
                beWorkOrder.setWorkContent(oldOne.getWorkContent());
                beWorkOrder.setWorkOrderStatus(workOrderStatus);
                beWorkOrder.setApproveProgress(approveProgress);
                beWorkOrder.setRejectReason(rejectReason);
                beWorkOrder.setEmployeeId(oldOne.getEmployeeId());
                beWorkOrder.setProjectWorkId(oldOne.getProjectWorkId());
                beWorkOrder.setBeSystemIds(oldOne.getBeSystemIds());
                beWorkOrder.setCreateTime(oldOne.getCreateTime());

                beWorkOrderDao.save(beWorkOrder);
            }
        }

    }

}
