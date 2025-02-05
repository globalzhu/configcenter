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
import org.antframework.configcenter.dal.dao.*;
import org.antframework.configcenter.dal.entity.*;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeWorkOrderOrder;
import org.antframework.configcenter.facade.vo.RequirementType;
import org.antframework.configcenter.facade.vo.WorkOrderType;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改工时工单
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeWorkOrderService {

    private final BeWorkOrderDao beWorkOrderDao;
    private final BeProjectDao beProjectDao;
    private final BeProductDao beProductDao;
    private final BeProductVersionDao beProductVersionDao;
    private final BeEmployeeDao beEmployeeDao;
    private final BeProjectWorkDao beProjectWorkDao;


    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeWorkOrderOrder, EmptyResult> context) {
        AddOrModifyBeWorkOrderOrder order = context.getOrder();
        BeProject beProject = beProjectDao.findById(order.getProjectId());
        if (beProject == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目信息[%s]不存在", order.getProjectId()));
        }

        // 项目状态信息为空，就补齐
        if(order.getProjectStatus() == null)order.setProjectStatus(beProject.getProjectStatus());

        RequirementType requirementType = order.getRequirementType();
        if(RequirementType.PRODUCT.equals(requirementType)){
            if (beProductDao.findById(order.getProductId()) == null) throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目信息[%s]不存在", order.getProductId()));
            if(order.getProductVersionId() != null){
                if (beProductVersionDao.findById(order.getProductVersionId()) == null)  throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目版本信息[%s]不存在", order.getProductVersionId()));
            }
//            if(StringUtils.isEmpty(order.getBeSystemIds())){
//                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("系统信息[%s]不能为空", order.getBeSystemIds()));
//            }
        }else if(RequirementType.PROJECT.equals(requirementType)){
            if (beProjectWorkDao.findById(order.getProjectWorkId()) == null) throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目工作信息[%s]不存在",order.getProjectWorkId()));
        }

        if(order.getEmployeeId() != null){
            BeEmployee beEmployee = beEmployeeDao.findById(order.getEmployeeId());
            if (beEmployee == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", order.getEmployeeId()));
            }
        }

        if(order.getLoginName() != null){
            BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(order.getLoginName());
            if (beEmployee == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", order.getEmployeeId()));
            }
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeWorkOrderOrder, EmptyResult> context) {
        AddOrModifyBeWorkOrderOrder order = context.getOrder();

        BeWorkOrder beWorkOrder = new BeWorkOrder();

        if(order.getLoginName() != null){
            BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(order.getLoginName());
            beWorkOrder.setEmployeeId(beEmployee.getId());
        }else{
            beWorkOrder.setEmployeeId(order.getEmployeeId());
        }

        beWorkOrder.setId(order.getId());
        beWorkOrder.setProjectId(order.getProjectId());
        beWorkOrder.setProjectWorkId(order.getProjectWorkId());
        beWorkOrder.setBeSystemIds(order.getBeSystemIds());
        beWorkOrder.setProjectStatus(order.getProjectStatus());
        beWorkOrder.setProductId(order.getProductId());
        beWorkOrder.setProductVersionId(order.getProductVersionId());
        beWorkOrder.setRequirementType(order.getRequirementType());
        beWorkOrder.setWorkOrderType(order.getWorkOrderType());
        beWorkOrder.setWorkDate(order.getWorkDate());
        beWorkOrder.setManDay(order.getManDay());
        beWorkOrder.setWorkContent(order.getWorkContent());
        beWorkOrder.setWorkOrderStatus(order.getWorkOrderStatus());
        beWorkOrder.setApproveProgress(order.getApproveProgress());
        beWorkOrder.setRejectReason(order.getRejectReason());
        beWorkOrder.setCreateTime(order.getCreateTime());

        beWorkOrderDao.save(beWorkOrder);
    }

}
