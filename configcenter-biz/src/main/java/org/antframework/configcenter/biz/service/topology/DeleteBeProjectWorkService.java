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
import org.antframework.configcenter.dal.dao.BeProjectWorkDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.order.DeleteBeEmployeeOrder;
import org.antframework.configcenter.facade.order.DeleteBeProjectWorkOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeProjectWorkService {
    private final BeProjectWorkDao beProjectWorkDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeProjectWorkOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeProjectWorkOrder, EmptyResult> context) {
        BeProjectWork delete = new BeProjectWork();
        delete.setId(context.getOrder().getId());
        beProjectWorkDao.delete(delete);
    }
}
