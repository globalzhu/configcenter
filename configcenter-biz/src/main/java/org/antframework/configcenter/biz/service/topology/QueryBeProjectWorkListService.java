/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service.topology;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeProjectWorkDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectWorkDTO;
import org.antframework.configcenter.facade.order.topology.QueryBeProjectWorkOrder;
import org.antframework.configcenter.facade.result.topology.QueryBeProjectWorkResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;

/**
 * 查找项目工作服务
 */
@Service
@AllArgsConstructor
public class QueryBeProjectWorkListService {

    private final BeEmployeeDao beEmployeeDao;
    private final BeProjectWorkDao beProjectWorkDao;

    @ServiceBefore
    public void before(ServiceContext<QueryBeProjectWorkOrder, QueryBeProjectWorkResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryBeProjectWorkOrder, QueryBeProjectWorkResult> context) {
        QueryBeProjectWorkResult result = context.getResult();
        List<BeProjectWorkDTO> projectWorkDTOList = Lists.newArrayList();
        QueryBeProjectWorkOrder order = context.getOrder();
        Long projectId = order.getProjectId();

        List<BeProjectWork> beProjectWorkList = beProjectWorkDao.findAllByProjectId(projectId);
        if(beProjectWorkList != null){
            beProjectWorkList.stream().forEach(beProjectWork->{
                BeProjectWorkDTO beProjectWorkDTO = new BeProjectWorkDTO();
                beProjectWorkDTO.setId(beProjectWork.getId());
                beProjectWorkDTO.setProjectId(beProjectWork.getProjectId());
                beProjectWorkDTO.setWorkTitle(beProjectWork.getWorkTitle());
                beProjectWorkDTO.setUpdateTime(beProjectWork.getUpdateTime());
                beProjectWorkDTO.setCreateTime(beProjectWork.getCreateTime());

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

                projectWorkDTOList.add(beProjectWorkDTO);
            });
        }
        result.setProjectWorkDTOList(projectWorkDTOList);
    }

}
