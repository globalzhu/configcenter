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
import org.antframework.configcenter.dal.dao.BeProductVersionDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProductVersion;
import org.antframework.configcenter.facade.order.DeleteBeEmployeeOrder;
import org.antframework.configcenter.facade.order.DeleteBeProductVersionOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除产品版本
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeProductVersionService {
    private final BeProductVersionDao beProductVersionDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeProductVersionOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeProductVersionOrder, EmptyResult> context) {
        BeProductVersion delete = new BeProductVersion();
        delete.setId(context.getOrder().getId());
        beProductVersionDao.delete(delete);
    }
}
