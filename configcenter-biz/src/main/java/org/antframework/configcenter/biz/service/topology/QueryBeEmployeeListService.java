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
import org.antframework.boot.bekit.CommonQueries;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.dao.BeProjectDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectAccessDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectDTO;
import org.antframework.configcenter.facade.order.topology.QueryBeEmployeeOrder;
import org.antframework.configcenter.facade.order.topology.QueryBeProjectAccessOrderWrapper;
import org.antframework.configcenter.facade.result.topology.QueryBeEmployeeResult;
import org.antframework.configcenter.facade.result.topology.QueryBeProjectAccessResult;
import org.antframework.configcenter.facade.result.topology.QueryBeProjectAccessResultWrapper;
import org.antframework.configcenter.facade.vo.AccessLevel;
import org.antframework.configcenter.facade.vo.AccessStatus;
import org.apache.commons.lang3.StringUtils;
import org.bekit.service.ServiceEngine;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryBeEmployeeListService {

    private final BeEmployeeDao beEmployeeDao;

    private final ServiceEngine serviceEngine;

    @ServiceBefore
    public void before(ServiceContext<QueryBeEmployeeOrder, QueryBeEmployeeResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryBeEmployeeOrder, QueryBeEmployeeResult> context) {
        QueryBeEmployeeResult result = context.getResult();

        CommonQueries.CommonQueryResult commonResult
                = serviceEngine.execute(
                        CommonQueries.SERVICE_NAME,
                        context.getOrder(),
                        QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeEmployeeDao.class));
        QueryBeEmployeeResult tmpResult = commonResult.convertTo(QueryBeEmployeeResult.class);
        if(tmpResult.isSuccess()){
            for(BeEmployeeDTO beEmployeeDTO : tmpResult.getInfos()){
                Long superiorId = beEmployeeDTO.getSuperiorId();
                if(superiorId != null){
                    BeEmployee beEmployee = beEmployeeDao.findById(superiorId);
                    if(beEmployee != null){
                        BeEmployeeDTO superiorDTO = new BeEmployeeDTO();

                        superiorDTO.setId(beEmployee.getId());
                        superiorDTO.setName(beEmployee.getName());
                        superiorDTO.setPenName(beEmployee.getPenName());
                        superiorDTO.setCertNo(beEmployee.getCertNo());
                        superiorDTO.setSuperiorId(beEmployee.getSuperiorId());
                        superiorDTO.setEmployeeType(beEmployee.getEmployeeType());
                        superiorDTO.setPostType(beEmployee.getPostType());
                        superiorDTO.setEmployeeLevel(beEmployee.getEmployeeLevel());
                        superiorDTO.setUnitCost(beEmployee.getUnitCost());
                        superiorDTO.setSex(beEmployee.getSex());
                        superiorDTO.setEducation(beEmployee.getEducation());
                        superiorDTO.setBaseSite(beEmployee.getBaseSite());
                        superiorDTO.setMajor(beEmployee.getMajor());
                        superiorDTO.setWorkAge(beEmployee.getWorkAge());
                        superiorDTO.setResumeFile(beEmployee.getResumeFile());
                        superiorDTO.setEntryTime(beEmployee.getEntryTime());
                        superiorDTO.setLeaveTime(beEmployee.getLeaveTime());
                        superiorDTO.setServiceStatus(beEmployee.getServiceStatus());

                        beEmployeeDTO.setSuperiorDTO(superiorDTO);
                    }
                }
                result.addInfo(beEmployeeDTO);
            }
        }

        result.setTotalCount(tmpResult.getTotalCount());
        result.setCode(tmpResult.getCode());
        result.setStatus(tmpResult.getStatus());
        result.setMessage(tmpResult.getMessage());

    }
}
