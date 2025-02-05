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
import org.antframework.configcenter.dal.dao.BeOutsourceDao;
import org.antframework.configcenter.dal.dao.BePreSaleOrderDao;
import org.antframework.configcenter.dal.entity.BeOutsource;
import org.antframework.configcenter.dal.entity.BePreSaleOrder;
import org.antframework.configcenter.facade.info.topology.BeOutsourceDTO;
import org.antframework.configcenter.facade.info.topology.BePreSaleOrderDTO;
import org.antframework.configcenter.facade.order.topology.FindOutsourceOrder;
import org.antframework.configcenter.facade.order.topology.FindPreSaleOrderOrder;
import org.antframework.configcenter.facade.result.topology.FindOutsourceResult;
import org.antframework.configcenter.facade.result.topology.FindPreSaleOrderResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindOutsourceService {

    // 分支dao
    private final BeOutsourceDao beOutsourceDao;

    @ServiceBefore
    public void before(ServiceContext<FindOutsourceOrder, FindOutsourceResult> context) {
        FindOutsourceOrder order = context.getOrder();

        Long outsourceId = order.getOutsourceId();
        BeOutsource beOutsource = beOutsourceDao.findById(outsourceId);
        if (beOutsource == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("外包[%s]不存在", outsourceId));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindOutsourceOrder, FindOutsourceResult> context) {
        FindOutsourceOrder findOutsourceOrder = context.getOrder();
        FindOutsourceResult result = context.getResult();

        BeOutsource beOutsource = beOutsourceDao.findById(findOutsourceOrder.getOutsourceId());

        BeOutsourceDTO beOutsourceDTO = new BeOutsourceDTO();

        beOutsourceDTO.setId(beOutsource.getId());
        beOutsourceDTO.setName(beOutsource.getName());
        beOutsourceDTO.setPenName(beOutsource.getPenName());
        beOutsourceDTO.setCertNo(beOutsource.getCertNo());
        beOutsourceDTO.setSex(beOutsource.getSex());
        beOutsourceDTO.setEducation(beOutsource.getEducation());
        beOutsourceDTO.setSalary(beOutsource.getSalary());
        beOutsourceDTO.setBaseSite(beOutsource.getBaseSite());
        beOutsourceDTO.setMajor(beOutsource.getMajor());
        beOutsourceDTO.setWorkAge(beOutsource.getWorkAge());
        beOutsourceDTO.setResumeFile(beOutsource.getResumeFile());
        beOutsourceDTO.setEntryTime(beOutsource.getEntryTime());
        beOutsourceDTO.setLeaveTime(beOutsource.getLeaveTime());
        beOutsourceDTO.setServiceStatus(beOutsource.getServiceStatus());

        result.setBeOutsourceDTO(beOutsourceDTO);
    }
}
