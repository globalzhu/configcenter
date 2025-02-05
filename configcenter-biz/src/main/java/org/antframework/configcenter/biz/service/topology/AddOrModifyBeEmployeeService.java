/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeOutsourceDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeOutsource;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeEmployeeOrder;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeOutsourceOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改外包人员信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeEmployeeService {

    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeEmployeeOrder, EmptyResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeEmployeeOrder, EmptyResult> context) {
        AddOrModifyBeEmployeeOrder order = context.getOrder();

        BeEmployee beEmployee = new BeEmployee();
        beEmployee.setId(order.getId());
        beEmployee.setName(order.getName());
        beEmployee.setPenName(order.getPenName());
        beEmployee.setWorkLoginName(order.getWorkLoginName());
        beEmployee.setPhoneNum(order.getPhoneNum());
        beEmployee.setSuperiorId(order.getSuperiorId());
        beEmployee.setEmployeeType(order.getEmployeeType());
        beEmployee.setPostType(order.getPostType());
        beEmployee.setEmployeeLevel(order.getEmployeeLevel());
        beEmployee.setUnitCost(order.getUnitCost());
        beEmployee.setCertNo(order.getCertNo());
        beEmployee.setSex(order.getSex());
        beEmployee.setEducation(order.getEducation());
        beEmployee.setSalary(order.getSalary()==null?"0":(order.getSalary()+""));
        beEmployee.setBaseSite(order.getBaseSite());
        beEmployee.setMajor(order.getMajor());
        beEmployee.setWorkAge(order.getWorkAge());
        beEmployee.setResumeFile(order.getResumeFile());
        beEmployee.setEntryTime(order.getEntryTime());
        beEmployee.setLeaveTime(order.getLeaveTime());
        beEmployee.setServiceStatus(order.getServiceStatus());

        beEmployeeDao.save(beEmployee);

    }

}
