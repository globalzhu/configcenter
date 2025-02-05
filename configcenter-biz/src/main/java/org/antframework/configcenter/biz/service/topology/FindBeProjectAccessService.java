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
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.facade.info.topology.BeProjectAccessDTO;
import org.antframework.configcenter.facade.order.topology.FindBeProjectAccessOrder;
import org.antframework.configcenter.facade.result.topology.FindBeProjectAccessResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找工时工单服务
 */
@Service
@AllArgsConstructor
public class FindBeProjectAccessService {

    private final BeProjectAccessDao beProjectAccessDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeProjectAccessOrder, FindBeProjectAccessResult> context) {
        FindBeProjectAccessOrder order = context.getOrder();

        Long id = order.getId();
        BeProjectAccess beProjectAccess = beProjectAccessDao.findById(order.getId());
        if (beProjectAccess == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目权限信息[%s]不存在", id));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeProjectAccessOrder, FindBeProjectAccessResult> context) {
        FindBeProjectAccessOrder order = context.getOrder();
        FindBeProjectAccessResult result = context.getResult();

        BeProjectAccess beProjectAccess = beProjectAccessDao.findById(order.getId());

        BeProjectAccessDTO beProjectAccessDTO = new BeProjectAccessDTO();
        beProjectAccessDTO.setAccessStatus(beProjectAccess.getAccessStatus());
        beProjectAccessDTO.setProjectId(beProjectAccess.getProjectId());
        beProjectAccessDTO.setAccessLevel(beProjectAccess.getAccessLevel());
        beProjectAccessDTO.setEmployeeId(beProjectAccess.getEmployeeId());
        beProjectAccessDTO.setCreateTime(beProjectAccess.getCreateTime());
        beProjectAccessDTO.setUpdateTime(beProjectAccess.getUpdateTime());

        result.setBeProjectAccessDTO(beProjectAccessDTO);

    }

}
