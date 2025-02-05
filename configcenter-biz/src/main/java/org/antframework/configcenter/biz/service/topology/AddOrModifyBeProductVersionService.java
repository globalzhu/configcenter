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
import org.antframework.configcenter.dal.dao.BeProductVersionDao;
import org.antframework.configcenter.dal.entity.BeProductVersion;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeProductVersionOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改外包人员信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeProductVersionService {

    private final BeProductVersionDao beProductVersionDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeProductVersionOrder, EmptyResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeProductVersionOrder, EmptyResult> context) {
        AddOrModifyBeProductVersionOrder order = context.getOrder();

        BeProductVersion beProductVersion = new BeProductVersion();

        beProductVersion.setId(order.getId());
        beProductVersion.setProductId(order.getProductId());
        beProductVersion.setVersionNum(order.getVersionNum());
        beProductVersion.setVersionPdName(order.getVersionPdName());
        beProductVersion.setVersionDevName(order.getVersionDevName());
        beProductVersion.setReleaseTime(order.getReleaseTime());
        beProductVersion.setReleaseUrl(order.getReleaseUrl());
        beProductVersion.setVersionStatus(order.getVersionStatus());

        beProductVersionDao.save(beProductVersion);

    }

}
