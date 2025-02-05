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
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.dao.BeProjectDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeProjectAccessOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;

/**
 * 添加或修改工时工单
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeProjectAccessService {

    private final BeProjectAccessDao beProjectAccessDao;
    private final BeProjectDao beProjectDao;
    private final BeEmployeeDao beEmployeeDao;


    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeProjectAccessOrder, EmptyResult> context) {
        AddOrModifyBeProjectAccessOrder order = context.getOrder();

        BeProject beProject = beProjectDao.findById(order.getProjectId());
        if (beProject == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目信息[%s]不存在", order.getProjectId()));
        }

        BeEmployee beEmployee = beEmployeeDao.findById(order.getEmployeeId());
        if (beEmployee == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", order.getEmployeeId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeProjectAccessOrder, EmptyResult> context) {
        AddOrModifyBeProjectAccessOrder order = context.getOrder();

        // 新增之前先做下去重
        if(order.getId() == null){
            Long projectId = order.getProjectId();
            Long employeeId = order.getEmployeeId();
            List<BeProjectAccess> accessList = beProjectAccessDao.findByProjectIdAndEmployeeId(projectId,employeeId);
            if(accessList != null && accessList.size() > 0) return;
        }

        BeProjectAccess beProjectAccess = new BeProjectAccess();
        beProjectAccess.setId(order.getId());
        beProjectAccess.setProjectId(order.getProjectId());
        beProjectAccess.setAccessLevel(order.getAccessLevel());
        beProjectAccess.setAccessStatus(order.getAccessStatus());
        beProjectAccess.setEmployeeId(order.getEmployeeId());

        beProjectAccessDao.save(beProjectAccess);
    }

}
