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
import org.antframework.configcenter.dal.dao.BeProjectDao;
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.facade.info.topology.BeProjectDTO;
import org.antframework.configcenter.facade.order.topology.FindBeProjectOrder;
import org.antframework.configcenter.facade.result.topology.FindBeProjectResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindBeProjectService {

    // 分支dao
    private final BeProjectDao beProjectDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeProjectOrder, FindBeProjectResult> context) {
        FindBeProjectOrder order = context.getOrder();

        Long id = order.getId();
        BeProject beProject = beProjectDao.findById(id);
        if (beProject == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("项目[%s]不存在", id));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeProjectOrder, FindBeProjectResult> context) {
        FindBeProjectResult result = context.getResult();

        BeProject beProject = beProjectDao.findById(context.getOrder().getId());

        BeProjectDTO beProjectDTO = new BeProjectDTO();

        beProjectDTO.setId(beProject.getId());
        beProjectDTO.setProjectName(beProject.getProjectName());
        beProjectDTO.setProjectCode(beProject.getProjectCode());
        beProjectDTO.setProjectMemo(beProject.getProjectMemo());
        beProjectDTO.setBeProjectType(beProject.getBeProjectType());
        beProjectDTO.setCustomerId(beProject.getCustomerId());
        beProjectDTO.setAccessPeople(beProject.getAccessPeople());
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

        result.setBeProjectDTO(beProjectDTO);
    }
}
