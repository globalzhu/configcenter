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
import org.antframework.configcenter.dal.dao.*;
import org.antframework.configcenter.dal.entity.*;
import org.antframework.configcenter.facade.info.topology.*;
import org.antframework.configcenter.facade.order.topology.FindBeWorkOrderOrder;
import org.antframework.configcenter.facade.result.topology.FindBeWorkOrderResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找工时工单服务
 */
@Service
@AllArgsConstructor
public class FindBeWorkOrderService {

    private final BeWorkOrderDao beWorkOrderDao;
    private final BeProjectDao beProjectDao;
    private final BeProductDao beProductDao;
    private final BeProductVersionDao beProductVersionDao;
    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeWorkOrderOrder, FindBeWorkOrderResult> context) {
        FindBeWorkOrderOrder order = context.getOrder();

        Long id = order.getId();
        BeWorkOrder beWorkOrder = beWorkOrderDao.findById(id);
        if (beWorkOrder == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("工时工单信息[%s]不存在", id));
        }

        BeProject beProject = beProjectDao.findById(beWorkOrder.getProjectId());
        if (beProject == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目信息[%s]不存在", beWorkOrder.getProjectId()));
        }

        if(beWorkOrder.getProductId() != null){
            BeProduct beProduct = beProductDao.findById(beWorkOrder.getProductId());
            if (beProduct == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目信息[%s]不存在", beWorkOrder.getProductId()));
            }
        }

        if(beWorkOrder.getProductVersionId() != null){
            BeProductVersion beProductVersion = beProductVersionDao.findById(beWorkOrder.getProductVersionId());
            if (beProductVersion == null) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目版本信息[%s]不存在", beWorkOrder.getProductVersionId()));
            }
        }

        BeEmployee beEmployee = beEmployeeDao.findById(beWorkOrder.getEmployeeId());
        if (beEmployee == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", beWorkOrder.getEmployeeId()));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeWorkOrderOrder, FindBeWorkOrderResult> context) {
        FindBeWorkOrderOrder order = context.getOrder();
        FindBeWorkOrderResult result = context.getResult();

        BeWorkOrder beWorkOrder = beWorkOrderDao.findById(order.getId());

        BeWorkOrderDTO beWorkOrderDTO = new BeWorkOrderDTO();

        beWorkOrderDTO.setProjectId(beWorkOrder.getProjectId());
        beWorkOrderDTO.setProjectWorkId(beWorkOrder.getProjectWorkId());
        beWorkOrderDTO.setBeSystemIds(beWorkOrder.getBeSystemIds());
        beWorkOrderDTO.setProjectStatus(beWorkOrder.getProjectStatus());
        beWorkOrderDTO.setEmployeeId(beWorkOrder.getEmployeeId());
        beWorkOrderDTO.setProductId(beWorkOrder.getProductId());
        beWorkOrderDTO.setProductVersionId(beWorkOrder.getProductVersionId());
        beWorkOrderDTO.setRequirementType(beWorkOrder.getRequirementType());
        beWorkOrderDTO.setWorkOrderType(beWorkOrder.getWorkOrderType());
        beWorkOrderDTO.setWorkDate(beWorkOrder.getWorkDate());
        beWorkOrderDTO.setManDay(beWorkOrder.getManDay());
        beWorkOrderDTO.setWorkContent(beWorkOrder.getWorkContent());
        beWorkOrderDTO.setWorkOrderStatus(beWorkOrder.getWorkOrderStatus());
        beWorkOrderDTO.setApproveProgress(beWorkOrder.getApproveProgress());
        beWorkOrderDTO.setRejectReason(beWorkOrder.getRejectReason());
        beWorkOrderDTO.setCreateTime(beWorkOrder.getCreateTime());
        beWorkOrderDTO.setUpdateTime(beWorkOrder.getUpdateTime());

        // 补全信息
        BeProject beProject = beProjectDao.findById(beWorkOrder.getProjectId());
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

        beWorkOrderDTO.setBeProjectDTO(beProjectDTO);

        if(beWorkOrder.getProductId() != null){
            BeProduct beProduct = beProductDao.findById(beWorkOrder.getProductId());
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

            beWorkOrderDTO.setBeProductDTO(beProductDTO);
        }


        if(beWorkOrder.getProductVersionId() != null){
            BeProductVersion beProductVersion = beProductVersionDao.findById(beWorkOrder.getProductVersionId());
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

            beWorkOrderDTO.setBeProductVersionDTO(beProductVersionDTO);

        }

        BeEmployee beEmployee = beEmployeeDao.findById(beWorkOrder.getEmployeeId());
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

        beWorkOrderDTO.setBeEmployeeDTO(beEmployeeDTO);

        result.setBeWorkOrderDTO(beWorkOrderDTO);
    }

}
