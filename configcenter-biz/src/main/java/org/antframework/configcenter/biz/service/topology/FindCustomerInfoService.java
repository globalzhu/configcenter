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
import org.antframework.configcenter.dal.dao.BeCustomerInfoDao;
import org.antframework.configcenter.dal.entity.BeCustomerInfo;
import org.antframework.configcenter.facade.info.topology.BeCustomerInfoDTO;
import org.antframework.configcenter.facade.order.topology.FindCustomerInfoOrder;
import org.antframework.configcenter.facade.result.topology.FindCustomerInfoResult;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找分支服务
 */
@Service
@AllArgsConstructor
public class FindCustomerInfoService {

    // 分支dao
    private final BeCustomerInfoDao beCustomerInfoDao;

    @ServiceBefore
    public void before(ServiceContext<FindCustomerInfoOrder, FindCustomerInfoResult> context) {
        FindCustomerInfoOrder order = context.getOrder();

        String customerId = order.getCustomerId();
        if (StringUtils.isEmpty(customerId)) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "customerId不能为空");
        }

        BeCustomerInfo customerInfo = beCustomerInfoDao.findById(Long.parseLong(customerId));
        if (customerInfo == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("客户信息[%s]不存在", order.getCustomerId()));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindCustomerInfoOrder, FindCustomerInfoResult> context) {
        FindCustomerInfoOrder order = context.getOrder();
        FindCustomerInfoResult result = context.getResult();
        BeCustomerInfo customerInfo = beCustomerInfoDao.findById(Long.parseLong(order.getCustomerId()));

        BeCustomerInfoDTO beCustomerInfoDTO = new BeCustomerInfoDTO();
        beCustomerInfoDTO.setCustomerCode(customerInfo.getCustomerCode());
        beCustomerInfoDTO.setCustomerName(customerInfo.getCustomerName());
        beCustomerInfoDTO.setCustomerStatus(customerInfo.getCustomerStatus());
        beCustomerInfoDTO.setCreateTime(customerInfo.getCreateTime());
        beCustomerInfoDTO.setId(customerInfo.getId());
        beCustomerInfoDTO.setUpdateTime(customerInfo.getUpdateTime());

        result.setCustomerInfo(beCustomerInfoDTO);
    }
}
