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
import org.antframework.configcenter.dal.dao.BeProjectWorkDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectWorkDTO;
import org.antframework.configcenter.facade.order.topology.FindBeProjectWorkOrder;
import org.antframework.configcenter.facade.result.topology.FindBeProjectWorkResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindBeProjectWorkService {

    // 分支dao
    private final BeProjectWorkDao beProjectWorkDao;
    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeProjectWorkOrder, FindBeProjectWorkResult> context) {
        FindBeProjectWorkOrder order = context.getOrder();

        Long id = order.getId();
        BeProjectWork beProjectWork = beProjectWorkDao.findById(id);
        if (beProjectWork == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目工作[%s]不存在", id));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeProjectWorkOrder, FindBeProjectWorkResult> context) {
        FindBeProjectWorkOrder order = context.getOrder();
        FindBeProjectWorkResult result = context.getResult();

        Long id = order.getId();
        BeProjectWork beProjectWork = beProjectWorkDao.findById(id);

        BeProjectWorkDTO beProjectWorkDTO = new BeProjectWorkDTO();
        beProjectWorkDTO.setProjectId(beProjectWork.getProjectId());
        beProjectWorkDTO.setWorkTitle(beProjectWork.getWorkTitle());
        beProjectWorkDTO.setCreateTime(beProjectWork.getCreateTime());
        beProjectWorkDTO.setUpdateTime(beProjectWork.getUpdateTime());
        beProjectWorkDTO.setId(beProjectWork.getId());
        beProjectWorkDTO.setCreateEmployeeId(beProjectWork.getCreateEmployeeId());

        Long createEmployeeId = beProjectWork.getCreateEmployeeId();

        BeEmployee beEmployee = beEmployeeDao.findById(createEmployeeId);
        if(beEmployee != null){
            BeEmployeeDTO beEmployeeDTO = new BeEmployeeDTO();
            beEmployeeDTO.setName(beEmployee.getName());
            beEmployeeDTO.setPenName(beEmployee.getPenName());
            beEmployeeDTO.setWorkLoginName(beEmployee.getWorkLoginName());
            beEmployeeDTO.setPhoneNum(beEmployee.getPhoneNum());
            beEmployeeDTO.setSuperiorId(beEmployee.getSuperiorId());
            beEmployeeDTO.setEmployeeType(beEmployee.getEmployeeType());
            beEmployeeDTO.setPostType(beEmployee.getPostType());
            beEmployeeDTO.setEmployeeLevel(beEmployee.getEmployeeLevel());
            beEmployeeDTO.setUnitCost(beEmployee.getUnitCost());
            beEmployeeDTO.setCertNo(beEmployee.getCertNo());
            beEmployeeDTO.setSex(beEmployee.getSex());
            beEmployeeDTO.setEducation(beEmployee.getEducation());
            beEmployeeDTO.setSalary(beEmployee.getSalary());
            beEmployeeDTO.setBaseSite(beEmployee.getBaseSite());
            beEmployeeDTO.setMajor(beEmployee.getMajor());
            beEmployeeDTO.setWorkAge(beEmployee.getWorkAge());
            beEmployeeDTO.setResumeFile(beEmployee.getResumeFile());
            beEmployeeDTO.setEntryTime(beEmployee.getEntryTime());
            beEmployeeDTO.setLeaveTime(beEmployee.getLeaveTime());
            beEmployeeDTO.setServiceStatus(beEmployee.getServiceStatus());
            beEmployeeDTO.setCreateTime(beEmployee.getCreateTime());
            beEmployeeDTO.setUpdateTime(beEmployee.getUpdateTime());

            beProjectWorkDTO.setCreateEmployeeDTO(beEmployeeDTO);
        }
        result.setBeProjectWorkDTO(beProjectWorkDTO);
    }
}
