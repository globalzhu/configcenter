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
import org.antframework.configcenter.dal.dao.BeWorkOrderDao;
import org.antframework.configcenter.dal.entity.BeWorkOrder;
import org.antframework.configcenter.facade.order.DeleteBeWorkOrderOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除工时工单
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeWorkOrderService {
    // 分支dao
    private final BeWorkOrderDao beWorkOrderDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeWorkOrderOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeWorkOrderOrder, EmptyResult> context) {
        BeWorkOrder delete = new BeWorkOrder();
        delete.setId(context.getOrder().getId());
        beWorkOrderDao.delete(delete);
    }
}
