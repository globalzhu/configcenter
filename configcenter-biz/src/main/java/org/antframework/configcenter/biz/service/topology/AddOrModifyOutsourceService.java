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
import org.antframework.configcenter.dal.dao.BeOutsourceDao;
import org.antframework.configcenter.dal.entity.BeOutsource;
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
public class AddOrModifyOutsourceService {

    private final BeOutsourceDao beOutsourceDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeOutsourceOrder, EmptyResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeOutsourceOrder, EmptyResult> context) {
        AddOrModifyBeOutsourceOrder order = context.getOrder();

        BeOutsource beOutsource = new BeOutsource();
        beOutsource.setId(order.getId());
        beOutsource.setName(order.getName());
        beOutsource.setPenName(order.getPenName());
        beOutsource.setPhoneNum(order.getPhoneNum());
        beOutsource.setCertNo(order.getCertNo());
        beOutsource.setSex(order.getSex());
        beOutsource.setEducation(order.getEducation());
        beOutsource.setSalary(order.getSalary());
        beOutsource.setBaseSite(order.getBaseSite());
        beOutsource.setMajor(order.getMajor());
        beOutsource.setWorkAge(order.getWorkAge());
        beOutsource.setResumeFile(order.getResumeFile());
        beOutsource.setEntryTime(order.getEntryTime());
        beOutsource.setLeaveTime(order.getLeaveTime());
        beOutsource.setServiceStatus(order.getServiceStatus());

        beOutsourceDao.save(beOutsource);

    }

}
