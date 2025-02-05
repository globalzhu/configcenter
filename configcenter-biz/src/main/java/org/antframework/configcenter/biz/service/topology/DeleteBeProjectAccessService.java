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
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.facade.order.DeleteBeProjectAccessOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除项目权限
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeProjectAccessService {
    private final BeProjectAccessDao beProjectAccessDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeProjectAccessOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeProjectAccessOrder, EmptyResult> context) {
        BeProjectAccess delete = new BeProjectAccess();
        delete.setId(context.getOrder().getId());
        beProjectAccessDao.delete(delete);
    }
}
