/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-28 22:58 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.facade.order.DeleteBeEmployeeOrder;
import org.antframework.configcenter.facade.order.DeleteBeProjectAccessOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除员工信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeEmployeeService {
    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeEmployeeOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeEmployeeOrder, EmptyResult> context) {
        BeEmployee delete = new BeEmployee();
        delete.setId(context.getOrder().getId());
        beEmployeeDao.delete(delete);
    }
}
