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
import org.antframework.configcenter.dal.dao.BeProductDao;
import org.antframework.configcenter.dal.entity.BeProduct;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeProductOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改外包人员信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeProductService {

    private final BeProductDao beProductDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeProductOrder, EmptyResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeProductOrder, EmptyResult> context) {
        AddOrModifyBeProductOrder order = context.getOrder();

        BeProduct beProduct = new BeProduct();
        beProduct.setId(order.getId());
        beProduct.setParentId(order.getParentId());
        beProduct.setProductName(order.getProductName());
        beProduct.setProductCode(order.getProductCode());
        beProduct.setPdName(order.getPdName());
        beProduct.setDevName(order.getDevName());
        beProduct.setApprovePeople(order.getApprovePeople());
        beProduct.setQuotationScope(order.getQuotationScope());
        beProduct.setMaturityDegree(order.getMaturityDegree());
        beProduct.setProductDevStatus(order.getProductDevStatus());
        beProduct.setProductClass(order.getProductClass());

        beProductDao.save(beProduct);

    }

}
