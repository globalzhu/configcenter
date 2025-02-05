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
import org.antframework.configcenter.dal.dao.BeProductDao;
import org.antframework.configcenter.dal.dao.BeProjectWorkDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProduct;
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeProductOrder;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeProjectWorkOrder;
import org.antframework.configcenter.facade.order.topology.QueryBeWorkOrderOrderWrapper;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改外包人员信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeProjectWorkService {

    private final BeProjectWorkDao beProjectWorkDao;
    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeProjectWorkOrder, EmptyResult> context) {
        AddOrModifyBeProjectWorkOrder order = context.getOrder();
        String loginName = order.getLoginName();

        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
        if (beEmployee == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", loginName));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeProjectWorkOrder, EmptyResult> context) {
        AddOrModifyBeProjectWorkOrder order = context.getOrder();

        String loginName = order.getLoginName();
        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);

        BeProjectWork beProjectWork = new BeProjectWork();
        beProjectWork.setId(order.getId());
        beProjectWork.setProjectId(order.getProjectId());
        beProjectWork.setWorkTitle(order.getWorkTitle());
        beProjectWork.setCreateEmployeeId(beEmployee.getId());

        beProjectWorkDao.save(beProjectWork);

    }

}
