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
import org.antframework.configcenter.dal.dao.BeProductDao;
import org.antframework.configcenter.dal.entity.BeProduct;
import org.antframework.configcenter.facade.order.DeleteBeProductOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 删除产品信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeProductService {
    private final BeProductDao beProductDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeProductOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeProductOrder, EmptyResult> context) {
        BeProduct delete = new BeProduct();
        delete.setId(context.getOrder().getId());
        beProductDao.delete(delete);
    }
}
