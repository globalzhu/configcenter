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
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeOutsourceDao;
import org.antframework.configcenter.dal.dao.BeOutsourceProjectRecordDao;
import org.antframework.configcenter.dal.entity.BeOutsource;
import org.antframework.configcenter.dal.entity.BeOutsourceProjectRecord;
import org.antframework.configcenter.facade.order.topology.AddOrModifyOutsourceProjectRecordOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 添加或修改部署工单
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyOutsourceProjectRecordService {

    private final BeOutsourceDao beOutsourceDao;

    private final BeOutsourceProjectRecordDao beOutsourceProjectRecordDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyOutsourceProjectRecordOrder, EmptyResult> context) {
        AddOrModifyOutsourceProjectRecordOrder order = context.getOrder();
        Long outsourceId = order.getOutsourceId();
        if(outsourceId == null){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "outsourceId为空");
        }

        BeOutsource beOutsource = beOutsourceDao.findById(outsourceId);
        if(beOutsource == null){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "beOutsource为空，outsourceId="+outsourceId);
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyOutsourceProjectRecordOrder, EmptyResult> context) {
        AddOrModifyOutsourceProjectRecordOrder order = context.getOrder();

        BeOutsourceProjectRecord beOutsourceProjectRecord = new BeOutsourceProjectRecord();
        beOutsourceProjectRecord.setId(order.getId());
        beOutsourceProjectRecord.setApplyName(order.getApplyName());
        beOutsourceProjectRecord.setApplyTitle(order.getApplyTitle());
        beOutsourceProjectRecord.setOutsourceId(order.getOutsourceId());
        beOutsourceProjectRecord.setProjectName(order.getProjectName());
        beOutsourceProjectRecord.setStoName(order.getStoName());
        beOutsourceProjectRecord.setLeaderName(order.getLeaderName());
        beOutsourceProjectRecord.setPmName(order.getPmName());
        beOutsourceProjectRecord.setBdName(order.getBdName());
        beOutsourceProjectRecord.setSaName(order.getSaName());
        beOutsourceProjectRecord.setPlanProjectEnterDate(order.getPlanProjectEnterDate());
        beOutsourceProjectRecord.setPlanProjectLeaveDate(order.getPlanProjectLeaveDate());
        beOutsourceProjectRecord.setRealProjectEnterDate(order.getRealProjectEnterDate());
        beOutsourceProjectRecord.setRealProjectLeaveDate(order.getRealProjectLeaveDate());
        beOutsourceProjectRecord.setRecordStatus(order.getRecordStatus());
        beOutsourceProjectRecord.setRejectReason(order.getRejectReason());
        beOutsourceProjectRecord.setScore1(order.getScore1());
        beOutsourceProjectRecord.setScore1Content(order.getScore1Content());
        beOutsourceProjectRecord.setScore2(order.getScore2());
        beOutsourceProjectRecord.setScore2Content(order.getScore2Content());
        beOutsourceProjectRecord.setApprovePeople(order.getApprovePeople());

        beOutsourceProjectRecordDao.save(beOutsourceProjectRecord);
    }

}
