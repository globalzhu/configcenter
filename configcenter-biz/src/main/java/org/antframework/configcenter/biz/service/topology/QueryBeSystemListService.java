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
import org.antframework.configcenter.dal.dao.BeProductDao;
import org.antframework.configcenter.dal.dao.BeSystemDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProduct;
import org.antframework.configcenter.dal.entity.BeSystem;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeSystemDTO;
import org.antframework.configcenter.facade.order.topology.QueryBeSystemOrder;
import org.antframework.configcenter.facade.result.topology.QueryBeSystemResult;
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
public class QueryBeSystemListService {

    private final BeEmployeeDao beEmployeeDao;
    private final BeSystemDao beSystemDao;
    private final BeProductDao beProductDao;

    @ServiceBefore
    public void before(ServiceContext<QueryBeSystemOrder, QueryBeSystemResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryBeSystemOrder, QueryBeSystemResult> context) {
        QueryBeSystemResult result = context.getResult();

        // 添加逻辑，如果传入productId，则用productId过滤
        QueryBeSystemOrder order = context.getOrder();
        Long productId = order.getProductId();

        List<Long> relSystemIdList = null;
        if(productId != null){
            BeProduct beProduct = beProductDao.findById(productId);
            if(beProduct != null) {
                String relSystemIds = beProduct.getRelSystemIds();
                if(relSystemIds != null){
                    relSystemIdList = Lists.newArrayList();
                    for(String relSystemId : relSystemIds.split(";")){
                        relSystemIdList.add(Long.parseLong(relSystemId));
                    }
                }
            }
        }

        List<BeSystemDTO> systemDTOList = Lists.newArrayList();
        List<BeSystem> beSystemList = beSystemDao.findAll();
        if(beSystemList != null){
            List<Long> finalRelSystemIdList = relSystemIdList;
            beSystemList.stream().forEach(beSystem->{
                if(finalRelSystemIdList != null
                        && (!finalRelSystemIdList.contains(beSystem.getId()))){
                    return;
                }

                BeSystemDTO beSystemDTO = new BeSystemDTO();
                beSystemDTO.setId(beSystem.getId());
                beSystemDTO.setSystemOwner(beSystem.getSystemOwner());
                beSystemDTO.setSystemName(beSystem.getSystemName());
                beSystemDTO.setSystemCode(beSystem.getSystemCode());
                beSystemDTO.setUpdateTime(beSystem.getUpdateTime());
                beSystemDTO.setCreateTime(beSystem.getCreateTime());

                String loginName = beSystem.getSystemOwner();

                BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
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

                    beSystemDTO.setSystemOwnerDTO(beEmployeeDTO);
                }

                systemDTOList.add(beSystemDTO);
            });
        }




        result.setSystemDTOList(systemDTOList);
    }

}
