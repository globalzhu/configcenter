/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service.topology;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.antframework.boot.bekit.CommonQueries;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.*;
import org.antframework.configcenter.dal.entity.*;
import org.antframework.configcenter.facade.info.topology.*;
import org.antframework.configcenter.facade.order.topology.QueryBeWorkOrderOrder;
import org.antframework.configcenter.facade.order.topology.QueryBeWorkOrderOrderWrapper;
import org.antframework.configcenter.facade.result.topology.QueryBeWorkOrderResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bekit.service.ServiceEngine;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;
import java.util.Map;

/**
 * 查找工时工单服务
 */
@Service
@AllArgsConstructor
public class QueryBeWorkOrderListService {

    private final BeProjectDao beProjectDao;
    private final BeProductDao beProductDao;
    private final BeProductVersionDao beProductVersionDao;
    private final BeEmployeeDao beEmployeeDao;
    private final BeSystemDao beSystemDao;
    private final BeProjectWorkDao beProjectWorkDao;

    private final ServiceEngine serviceEngine;

    @ServiceBefore
    public void before(ServiceContext<QueryBeWorkOrderOrderWrapper, QueryBeWorkOrderResult> context) {
        QueryBeWorkOrderOrderWrapper wrapper = context.getOrder();
        String loginName = wrapper.getLoginName();
        if(loginName != null){
            BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
            if (beEmployee == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", loginName));
            }
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryBeWorkOrderOrderWrapper, QueryBeWorkOrderResult> context) {

        QueryBeWorkOrderOrderWrapper wrapper = context.getOrder();
        QueryBeWorkOrderOrder order = wrapper.getQueryBeWorkOrderOrder();
        String loginName = wrapper.getLoginName();
        if(loginName != null){
            BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
            List<Long> employIdList = Lists.newArrayList();
            employIdList.add(beEmployee.getId());
            order.setEmployeeId(employIdList);
        }

        QueryBeWorkOrderResult filledResult = context.getResult();

        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeWorkOrderDao.class));
        QueryBeWorkOrderResult queryBeWorkOrderResult =  result.convertTo(QueryBeWorkOrderResult.class);

        if(!queryBeWorkOrderResult.isSuccess()){
            filledResult.setCode(queryBeWorkOrderResult.getCode());
            filledResult.setMessage(queryBeWorkOrderResult.getMessage());
            filledResult.setStatus(queryBeWorkOrderResult.getStatus());
            return;
        }

        List<Long> projectIdList = Lists.newArrayList();
        List<Long> productIdList = Lists.newArrayList();
        List<Long> projectWorkIdList = Lists.newArrayList();
        List<Long> systemIdList = Lists.newArrayList();
        List<Long> productVersionIdList = Lists.newArrayList();
        List<Long> employeeIdList = Lists.newArrayList();

        queryBeWorkOrderResult.getInfos().stream().forEach(
                beWorkOrderDTO -> {
                    beWorkOrderDTO.setWorkDateStr(DateFormatUtils.format(beWorkOrderDTO.getWorkDate(),"yyyy-MM-dd"));
                    projectIdList.add(beWorkOrderDTO.getProjectId());
                    productIdList.add(beWorkOrderDTO.getProductId());
                    productVersionIdList.add(beWorkOrderDTO.getProductVersionId());
                    employeeIdList.add(beWorkOrderDTO.getEmployeeId());
                    projectWorkIdList.add(beWorkOrderDTO.getProjectWorkId());
                    if(StringUtils.isNotEmpty(beWorkOrderDTO.getBeSystemIds())){
                        for(String sysIdStr : beWorkOrderDTO.getBeSystemIds().split(";")){
                            if(!systemIdList.contains(Long.parseLong(sysIdStr))){
                                systemIdList.add(Long.parseLong(sysIdStr));
                            }
                        }
                    }
                    filledResult.addInfo(beWorkOrderDTO);
                }
        );

        Map<Long,BeProjectDTO> projectDTOMap = queryProjectList(projectIdList);
        Map<Long,BeProductDTO> productDTOMap = queryProductList(productIdList);
        Map<Long,BeProductVersionDTO> productVersionDTOMap = queryProductVersionList(productVersionIdList);
        Map<Long,BeEmployeeDTO> employeeDTOMap = queryEmployeeList(employeeIdList);
        Map<Long,BeProjectWorkDTO> projectWorkDTOMap = queryProjectWorkList(projectWorkIdList);
        Map<Long,BeSystemDTO> systemDTOMap = querySystemList(systemIdList);

        filledResult.getInfos().stream().forEach(
                beWorkOrderDTO -> {
                    beWorkOrderDTO.setBeProjectDTO(projectDTOMap.get(beWorkOrderDTO.getProjectId()));
                    beWorkOrderDTO.setBeProductDTO(productDTOMap.get(beWorkOrderDTO.getProductId()));
                    beWorkOrderDTO.setBeProductVersionDTO(productVersionDTOMap.get(beWorkOrderDTO.getProductVersionId()));
                    beWorkOrderDTO.setBeEmployeeDTO(employeeDTOMap.get(beWorkOrderDTO.getEmployeeId()));
                    beWorkOrderDTO.setBeProjectWorkDTO(projectWorkDTOMap.get(beWorkOrderDTO.getProjectWorkId()));

                    String beSystemIds = beWorkOrderDTO.getBeSystemIds();
                    if(StringUtils.isNotEmpty(beSystemIds)){
                        List<BeSystemDTO> systems = Lists.newArrayList();
                        for(String beSystemIdStr : beSystemIds.split(";")){
                            systems.add(systemDTOMap.get(Long.parseLong(beSystemIdStr)));
                        }

                        beWorkOrderDTO.setBeSystemDTOList(systems);
                    }
                }
        );

    }

    private Map<Long,BeProjectDTO> queryProjectList(List<Long> projectIdList){
        Map<Long,BeProjectDTO> projectDTOMap = Maps.newHashMap();

        if(projectIdList != null && projectIdList.size() > 0){
            List<BeProject> projectList = beProjectDao.findByIdIn(projectIdList);
            if(projectList != null && projectList.size() >0){
                projectList.stream().forEach(beProject->{
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

                    projectDTOMap.put(beProject.getId(),beProjectDTO);
                });
            }
        }

        return projectDTOMap;
    }

    private Map<Long, BeProjectWorkDTO> queryProjectWorkList(List<Long> projectWorkIdList){
        Map<Long,BeProjectWorkDTO> projectWorkDTOMap = Maps.newHashMap();

        if(projectWorkIdList != null && projectWorkIdList.size() > 0){
            List<BeProjectWork> projectWorkList = beProjectWorkDao.findByIdIn(projectWorkIdList);
            if(projectWorkList != null && projectWorkList.size() >0){
                projectWorkList.stream().forEach(projectWork->{
                    BeProjectWorkDTO beProjectWorkDTO = new BeProjectWorkDTO();
                    beProjectWorkDTO.setProjectId(projectWork.getProjectId());
                    beProjectWorkDTO.setWorkTitle(projectWork.getWorkTitle());
                    beProjectWorkDTO.setId(projectWork.getId());
                    beProjectWorkDTO.setCreateEmployeeId(projectWork.getCreateEmployeeId());
                    beProjectWorkDTO.setCreateTime(projectWork.getCreateTime());
                    beProjectWorkDTO.setUpdateTime(projectWork.getUpdateTime());

                    projectWorkDTOMap.put(projectWork.getId(),beProjectWorkDTO);
                });
            }
        }

        return projectWorkDTOMap;
    }

    private Map<Long, BeSystemDTO> querySystemList(List<Long> systemIdList){
        Map<Long,BeSystemDTO> systemDTOMap = Maps.newHashMap();

        if(systemIdList != null && systemIdList.size() > 0){
            List<BeSystem> systemList = beSystemDao.findByIdIn(systemIdList);
            if(systemList != null && systemList.size() >0){
                systemList.stream().forEach(system->{
                    BeSystemDTO beSystemDTO = new BeSystemDTO();
                    beSystemDTO.setSystemCode(system.getSystemCode());
                    beSystemDTO.setSystemName(system.getSystemName());
                    beSystemDTO.setSystemOwner(system.getSystemOwner());
                    beSystemDTO.setCreateTime(system.getCreateTime());
                    beSystemDTO.setUpdateTime(system.getUpdateTime());
                    beSystemDTO.setId(system.getId());

                    systemDTOMap.put(system.getId(),beSystemDTO);
                });
            }
        }
        return systemDTOMap;
    }

    private Map<Long,BeProductDTO> queryProductList(List<Long> productIdList){
        Map<Long,BeProductDTO> productDTOMap = Maps.newHashMap();

        if(productIdList != null && productIdList.size() > 0){
            List<BeProduct> productList = beProductDao.findByIdIn(productIdList);
            if(productList != null && productList.size() >0){
                productList.stream().forEach(beProduct->{
                    BeProductDTO beProductDTO = new BeProductDTO();
                    beProductDTO.setParentId(beProduct.getParentId());
                    beProductDTO.setProductName(beProduct.getProductName());
                    beProductDTO.setProductCode(beProduct.getProductCode());
                    beProductDTO.setPdName(beProduct.getPdName());
                    beProductDTO.setDevName(beProduct.getDevName());
                    beProductDTO.setApprovePeople(beProduct.getApprovePeople());
                    beProductDTO.setQuotationScope(beProduct.getQuotationScope());
                    beProductDTO.setMaturityDegree(beProduct.getMaturityDegree());
                    beProductDTO.setProductDevStatus(beProduct.getProductDevStatus());
                    beProductDTO.setCreateTime(beProduct.getCreateTime());
                    beProductDTO.setUpdateTime(beProduct.getUpdateTime());
                    beProductDTO.setRelSystemIds(beProduct.getRelSystemIds());
                    beProductDTO.setProductClass(beProduct.getProductClass());

                    productDTOMap.put(beProduct.getId(),beProductDTO);
                });
            }
        }

        return productDTOMap;
    }

    private Map<Long,BeProductVersionDTO> queryProductVersionList(List<Long> productVersionIdList){
        Map<Long,BeProductVersionDTO> productVersionDTOMap = Maps.newHashMap();

        if(productVersionIdList != null && productVersionIdList.size() > 0){
            List<BeProductVersion> productVersionList = beProductVersionDao.findByIdIn(productVersionIdList);
            if(productVersionList != null && productVersionList.size() >0){
                productVersionList.stream().forEach(beProductVersion->{
                    BeProductVersionDTO beProductVersionDTO = new BeProductVersionDTO();
                    beProductVersionDTO.setProductId(beProductVersion.getProductId());
                    beProductVersionDTO.setVersionNum(beProductVersion.getVersionNum());
                    beProductVersionDTO.setVersionPdName(beProductVersion.getVersionPdName());
                    beProductVersionDTO.setVersionDevName(beProductVersion.getVersionDevName());
                    beProductVersionDTO.setReleaseTime(beProductVersion.getReleaseTime());
                    beProductVersionDTO.setReleaseUrl(beProductVersion.getReleaseUrl());
                    beProductVersionDTO.setVersionStatus(beProductVersion.getVersionStatus());
                    beProductVersionDTO.setCreateTime(beProductVersion.getCreateTime());
                    beProductVersionDTO.setUpdateTime(beProductVersion.getUpdateTime());

                    productVersionDTOMap.put(beProductVersion.getId(),beProductVersionDTO);
                });
            }
        }

        return productVersionDTOMap;
    }

    private Map<Long,BeEmployeeDTO> queryEmployeeList(List<Long> employeeIdList){
        Map<Long,BeEmployeeDTO> employeeDTOMap = Maps.newHashMap();

        if(employeeIdList != null && employeeIdList.size() > 0){
            List<BeEmployee> employeeList = beEmployeeDao.findByIdIn(employeeIdList);
            if(employeeList != null && employeeList.size() >0){
                employeeList.stream().forEach(beEmployee->{
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

                    employeeDTOMap.put(beEmployee.getId(),beEmployeeDTO);
                });
            }
        }

        return employeeDTOMap;
    }

}
