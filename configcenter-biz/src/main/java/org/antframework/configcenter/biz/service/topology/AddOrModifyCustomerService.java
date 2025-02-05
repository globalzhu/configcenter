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
import org.antframework.configcenter.dal.dao.BeCustomerInfoDao;
import org.antframework.configcenter.dal.entity.BeCustomerInfo;
import org.antframework.configcenter.facade.order.topology.AddOrModifyCustomerOrder;
import org.antframework.configcenter.facade.vo.CustomerStatus;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改应用服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyCustomerService {
    // 应用dao
    private final BeCustomerInfoDao beCustomerInfoDao;

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyCustomerOrder, EmptyResult> context) {
        AddOrModifyCustomerOrder order = context.getOrder();

        String customerCode = order.getCustomerCode();
        String customerName = order.getCustomerName();
        CustomerStatus customerStatus = order.getCustomerStatus();

        BeCustomerInfo customerInfo = null;
        if (order.getId() != null) {
            customerInfo = beCustomerInfoDao.findById(order.getId());
        }
        if (customerInfo == null) {
            customerInfo = new BeCustomerInfo();
            customerInfo.setCustomerCode(customerCode);
        }

        customerInfo.setCustomerName(customerName);
        customerInfo.setCustomerStatus(customerStatus);

        beCustomerInfoDao.save(customerInfo);
    }

}
