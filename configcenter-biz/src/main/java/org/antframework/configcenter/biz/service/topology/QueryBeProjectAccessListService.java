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
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.dao.BeProjectDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectAccessDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectDTO;
import org.antframework.configcenter.facade.order.topology.QueryBeProjectAccessOrderWrapper;
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

/**
 * 查找工时工单服务
 */
@Service
@AllArgsConstructor
public class QueryBeProjectAccessListService {

    private final BeEmployeeDao beEmployeeDao;
    private final BeProjectAccessDao beProjectAccessDao;

    private final BeProjectDao beProjectDao;

    private final ServiceEngine serviceEngine;

    @ServiceBefore
    public void before(ServiceContext<QueryBeProjectAccessOrderWrapper, QueryBeProjectAccessResult> context) {
        QueryBeProjectAccessOrderWrapper wrapper = context.getOrder();
        String loginName = wrapper.getLoginName();

        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
        if (beEmployee == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", loginName));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryBeProjectAccessOrderWrapper, QueryBeProjectAccessResult> context) {
        QueryBeProjectAccessResult result = context.getResult();
        List<QueryBeProjectAccessResultWrapper> projectWrapper = Lists.newArrayList();

        QueryBeProjectAccessOrderWrapper wrapper = context.getOrder();
        String loginName = wrapper.getLoginName();
        String projectName = wrapper.getProjectName();

        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
        result.setBeEmployeeDTO(convert(beEmployee));

        // 特殊逻辑：这几位同学能看到所有权限控制信息
        if(loginName != null && (
                "weiruo".equals(loginName)
                        ||"zongzheng".equals(loginName)
                        ||"jiuhe".equals(loginName)
                        ||"ningxin".equals(loginName))){
            List<BeProjectAccess> allBeProjectAccessList = Lists.newArrayList();
            if(StringUtils.isNotEmpty(projectName)){
                BeProject beProject = beProjectDao.findByProjectName(projectName.trim());
                if(beProject == null) return;
                allBeProjectAccessList
                        = beProjectAccessDao.findByProjectIdAndAccessStatus(beProject.getId(),AccessStatus.NORMAL);
            }else{
                allBeProjectAccessList
                        = beProjectAccessDao.findByAccessStatus(AccessStatus.NORMAL);
            }

            List<BeProjectAccessDTO> beProjectAccessDTOList = Lists.newArrayList();
            allBeProjectAccessList.stream().forEach(innerBeProjectAccess -> {
                BeProjectAccessDTO eachBeProjectAccessDTO = convert(innerBeProjectAccess);

                Long innerEmployeeId = innerBeProjectAccess.getEmployeeId();
                Long innerProjectId = innerBeProjectAccess.getProjectId();
                BeEmployee innerBeEmployee = beEmployeeDao.findById(innerEmployeeId);
                BeProject innerBeProject = beProjectDao.findById(innerProjectId);

                if(innerBeProject != null)eachBeProjectAccessDTO.setBeProjectDTO(convert(innerBeProject));
                if(innerBeEmployee != null)eachBeProjectAccessDTO.setBeEmployeeDTO(convert(innerBeEmployee));

                beProjectAccessDTOList.add(eachBeProjectAccessDTO);
            });

            QueryBeProjectAccessResultWrapper allWrapper = new QueryBeProjectAccessResultWrapper();
            allWrapper.setBeProjectAccessDTOList(beProjectAccessDTOList);
            projectWrapper.add(allWrapper);
        }else{

            // 查询当前登陆用户所有管理的项目列表
            List<BeProjectAccess>  beProjectAccessList
                    = beProjectAccessDao.findByEmployeeIdAndAccessLevelAndAccessStatus(
                    beEmployee.getId(), AccessLevel.ADMIN, AccessStatus.NORMAL);

            if(CollectionUtils.isEmpty(beProjectAccessList)){
                return;
            }

            List<Long> projectIdList = Lists.newArrayList();
            for(BeProjectAccess beProjectAccess : beProjectAccessList){
                if(!projectIdList.contains(beProjectAccess.getProjectId())){
                    projectIdList.add(beProjectAccess.getProjectId());
                }
            }

            for(Long eachProjectId : projectIdList){

                QueryBeProjectAccessResultWrapper eachWrapper = new QueryBeProjectAccessResultWrapper();
                List<BeProjectAccessDTO> beProjectAccessDTOList = Lists.newArrayList();

                BeProject beProject = beProjectDao.findById(eachProjectId);

                if(beProject != null){
                    if(StringUtils.isNotEmpty(projectName)){
                        // 如果projectName为非空，名称不匹配，则跳过
                        if(!beProject.getProjectName().contains(projectName)){
                            continue;
                        }
                    }

                    BeProjectDTO beProjectDTO = convert(beProject);
                    eachWrapper.setBeProjectDTO(beProjectDTO);
                }

                List<BeProjectAccess> eachBeProjectAccessList
                        = beProjectAccessDao.findByProjectIdAndAccessStatus(eachProjectId,AccessStatus.NORMAL);

                if(CollectionUtils.isEmpty(eachBeProjectAccessList)) return;

                eachBeProjectAccessList.stream().forEach(innerBeProjectAccess -> {
                    BeProjectAccessDTO eachBeProjectAccessDTO = convert(innerBeProjectAccess);

                    Long innerEmployeeId = innerBeProjectAccess.getEmployeeId();
                    Long innerProjectId = innerBeProjectAccess.getProjectId();
                    BeEmployee innerBeEmployee = beEmployeeDao.findById(innerEmployeeId);
                    BeProject innerBeProject = beProjectDao.findById(innerProjectId);

                    if(innerBeProject != null)eachBeProjectAccessDTO.setBeProjectDTO(convert(innerBeProject));
                    if(innerBeEmployee != null)eachBeProjectAccessDTO.setBeEmployeeDTO(convert(innerBeEmployee));

                    beProjectAccessDTOList.add(eachBeProjectAccessDTO);
                });

                eachWrapper.setBeProjectAccessDTOList(beProjectAccessDTOList);
                projectWrapper.add(eachWrapper);
            }
        }

        result.setProjectWrapper(projectWrapper);
    }

    private BeEmployeeDTO convert(BeEmployee beEmployee){
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
        return beEmployeeDTO;
    }

    private BeProjectDTO convert(BeProject beProject){
        BeProjectDTO beProjectDTO = new BeProjectDTO();
        beProjectDTO.setProjectName(beProject.getProjectName());
        beProjectDTO.setProjectCode(beProject.getProjectCode());
        beProjectDTO.setBeProjectType(beProject.getBeProjectType());
        beProjectDTO.setProjectMemo(beProject.getProjectMemo());
        beProjectDTO.setCustomerId(beProject.getCustomerId());
        beProjectDTO.setCustomerContact(beProject.getCustomerContact());
        beProjectDTO.setCustomerContactInfo(beProject.getCustomerContactInfo());
        beProjectDTO.setBdName(beProject.getBdName());
        beProjectDTO.setSaName(beProject.getSaName());
        beProjectDTO.setPmName(beProject.getPmName());
        beProjectDTO.setStoName(beProject.getStoName());
        beProjectDTO.setApprovePeople(beProject.getApprovePeople());
        beProjectDTO.setBeginTime(beProject.getBeginTime());
        beProjectDTO.setEndTime(beProject.getEndTime());
        beProjectDTO.setProductIds(beProject.getProductIds());
        beProjectDTO.setProjectStatus(beProject.getProjectStatus());
        beProjectDTO.setCreateTime(beProject.getCreateTime());
        beProjectDTO.setUpdateTime(beProject.getUpdateTime());
        return beProjectDTO;
    }

    private BeProjectAccessDTO convert(BeProjectAccess beProjectAccess){
        BeProjectAccessDTO beProjectAccessDTO = new BeProjectAccessDTO();
        beProjectAccessDTO.setId(beProjectAccess.getId());
        beProjectAccessDTO.setProjectId(beProjectAccess.getProjectId());
        beProjectAccessDTO.setAccessLevel(beProjectAccess.getAccessLevel());
        beProjectAccessDTO.setAccessStatus(beProjectAccess.getAccessStatus());
        beProjectAccessDTO.setEmployeeId(beProjectAccess.getEmployeeId());
        beProjectAccessDTO.setCreateTime(beProjectAccess.getCreateTime());
        beProjectAccessDTO.setUpdateTime(beProjectAccess.getUpdateTime());
        return beProjectAccessDTO;
    }

}
