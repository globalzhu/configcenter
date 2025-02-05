/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeWorkOrderDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeWorkOrder;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.order.topology.WeekFinishWorkOrderOrder;
import org.antframework.configcenter.facade.result.topology.WeekFinishResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 判断本周工时是否完成填报
 */
@Service(enableTx = true)
@AllArgsConstructor
public class IsFinishedThisWeekService {

    private final BeEmployeeDao beEmployeeDao;

    private final BeWorkOrderDao beWorkOrderDao;

    @ServiceBefore
    public void before(ServiceContext<WeekFinishWorkOrderOrder, WeekFinishResult> context) {
        WeekFinishWorkOrderOrder wrapper = context.getOrder();
        String loginName = wrapper.getLoginName();

        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
        if (beEmployee == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", loginName));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<WeekFinishWorkOrderOrder, WeekFinishResult> context) {
        WeekFinishResult result= context.getResult();
        StringBuffer unFinishedNoticeMessage = new StringBuffer();
        unFinishedNoticeMessage.append("未完成日期");
        WeekFinishWorkOrderOrder wrapper = context.getOrder();
        String loginName = wrapper.getLoginName();
        Date workDay = wrapper.getMonday();

        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginName);
        Long employeeId = beEmployee.getId();

        Boolean isFinishedThisWeek = Boolean.TRUE;

        for(int i = 0 ; i < 5 ; i ++){
            Date queryDate = DateUtils.addDays(workDay,i);

            List<BeWorkOrder> workOrderList =  beWorkOrderDao.findAllByWorkDateAndEmployeeId(queryDate,employeeId);
            if(CollectionUtils.isEmpty(workOrderList)){
                isFinishedThisWeek = false;
                unFinishedNoticeMessage.append(DateFormatUtils.format(queryDate,"yyyy-MM-dd")).append(";");
                continue;
            }

            AtomicReference<Double> manday = new AtomicReference<>(0d);
            workOrderList.stream().forEach(beWorkOrder->{
                manday.updateAndGet(v -> v + beWorkOrder.getManDay());
            });

            if(manday.get() < 1){
                isFinishedThisWeek = false;
                unFinishedNoticeMessage.append(DateFormatUtils.format(queryDate,"yyyy-MM-dd")).append(";");
                continue;
            }
        }

        result.setIsFinishedThisWeek(isFinishedThisWeek);
        if(isFinishedThisWeek){
            result.setNoticeMessage("本周已经完成工时填报！^-^");
        }else{
            result.setNoticeMessage(unFinishedNoticeMessage.toString());
        }
        result.setBeEmployeeDTO(convert(beEmployee));
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

}
