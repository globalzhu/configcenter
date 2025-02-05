/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeProductVersionDao;
import org.antframework.configcenter.dal.entity.BeProductVersion;
import org.antframework.configcenter.facade.info.topology.BeProductVersionDTO;
import org.antframework.configcenter.facade.order.topology.FindBeProductVersionOrder;
import org.antframework.configcenter.facade.result.topology.FindBeProductVersionResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindBeProductVersionService {

    // 分支dao
    private final BeProductVersionDao beProductVersionDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeProductVersionOrder, FindBeProductVersionResult> context) {
        FindBeProductVersionOrder order = context.getOrder();

        Long id = order.getId();
        BeProductVersion beProductVersion = beProductVersionDao.findById(id);
        if (beProductVersion == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("产品版本[%s]不存在", id));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeProductVersionOrder, FindBeProductVersionResult> context) {
        FindBeProductVersionResult result = context.getResult();

        BeProductVersion beProductVersion = beProductVersionDao.findById(context.getOrder().getId());

        BeProductVersionDTO beProductVersionDTO = new BeProductVersionDTO();

        beProductVersionDTO.setId(beProductVersion.getId());
        beProductVersionDTO.setProductId(beProductVersion.getProductId());
        beProductVersionDTO.setVersionNum(beProductVersion.getVersionNum());
        beProductVersionDTO.setVersionPdName(beProductVersion.getVersionPdName());
        beProductVersionDTO.setVersionDevName(beProductVersion.getVersionDevName());
        beProductVersionDTO.setReleaseTime(beProductVersion.getReleaseTime());
        beProductVersionDTO.setReleaseUrl(beProductVersion.getReleaseUrl());
        beProductVersionDTO.setVersionStatus(beProductVersion.getVersionStatus());

        result.setBeProductVersionDTO(beProductVersionDTO);
    }
}
