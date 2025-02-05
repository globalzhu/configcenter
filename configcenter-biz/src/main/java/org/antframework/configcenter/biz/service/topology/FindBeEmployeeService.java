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
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeOutsourceDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeOutsource;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeOutsourceDTO;
import org.antframework.configcenter.facade.order.topology.FindBeEmployeeOrder;
import org.antframework.configcenter.facade.order.topology.FindOutsourceOrder;
import org.antframework.configcenter.facade.result.topology.FindBeEmployeeResult;
import org.antframework.configcenter.facade.result.topology.FindOutsourceResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindBeEmployeeService {

    // 分支dao
    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeEmployeeOrder, FindBeEmployeeResult> context) {
        FindBeEmployeeOrder order = context.getOrder();

        Long id = order.getId();
        BeEmployee beEmployee = beEmployeeDao.findById(id);
        if (beEmployee == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工[%s]不存在", id));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeEmployeeOrder, FindBeEmployeeResult> context) {
        FindBeEmployeeResult result = context.getResult();

        BeEmployee beEmployee = beEmployeeDao.findById(context.getOrder().getId());

        BeEmployeeDTO beEmployeeDTO = new BeEmployeeDTO();

        beEmployeeDTO.setId(beEmployee.getId());
        beEmployeeDTO.setName(beEmployee.getName());
        beEmployeeDTO.setPenName(beEmployee.getPenName());
        beEmployeeDTO.setCertNo(beEmployee.getCertNo());
        beEmployeeDTO.setSuperiorId(beEmployee.getSuperiorId());
        beEmployeeDTO.setEmployeeType(beEmployee.getEmployeeType());
        beEmployeeDTO.setPostType(beEmployee.getPostType());
        beEmployeeDTO.setEmployeeLevel(beEmployee.getEmployeeLevel());
        beEmployeeDTO.setUnitCost(beEmployee.getUnitCost());
        beEmployeeDTO.setSex(beEmployee.getSex());
        beEmployeeDTO.setEducation(beEmployee.getEducation());
        beEmployeeDTO.setBaseSite(beEmployee.getBaseSite());
        beEmployeeDTO.setMajor(beEmployee.getMajor());
        beEmployeeDTO.setWorkAge(beEmployee.getWorkAge());
        beEmployeeDTO.setResumeFile(beEmployee.getResumeFile());
        beEmployeeDTO.setEntryTime(beEmployee.getEntryTime());
        beEmployeeDTO.setLeaveTime(beEmployee.getLeaveTime());
        beEmployeeDTO.setServiceStatus(beEmployee.getServiceStatus());

        result.setBeEmployeeDTO(beEmployeeDTO);
    }
}
