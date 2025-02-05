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
import org.antframework.configcenter.dal.dao.BeOutsourceDao;
import org.antframework.configcenter.dal.dao.BeOutsourceProjectRecordDao;
import org.antframework.configcenter.dal.entity.BeOutsource;
import org.antframework.configcenter.dal.entity.BeOutsourceProjectRecord;
import org.antframework.configcenter.facade.info.topology.BeOutsourceDTO;
import org.antframework.configcenter.facade.info.topology.BeOutsourceProjectRecordDTO;
import org.antframework.configcenter.facade.order.topology.FindOutsourceOrder;
import org.antframework.configcenter.facade.order.topology.FindOutsourceProjectRecordOrder;
import org.antframework.configcenter.facade.result.topology.FindOutsourceProjectRecordResult;
import org.antframework.configcenter.facade.result.topology.FindOutsourceResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindOutsourceProjectRecordService {

    // 分支dao
    private final BeOutsourceProjectRecordDao beOutsourceProjectRecordDao;

    @ServiceBefore
    public void before(ServiceContext<FindOutsourceProjectRecordOrder, FindOutsourceProjectRecordResult> context) {
        FindOutsourceProjectRecordOrder order = context.getOrder();

        Long outsourceProjectRecordId = order.getOutsourceProjectRecordId();
        BeOutsourceProjectRecord beOutsourceProjectRecord = beOutsourceProjectRecordDao.findById(outsourceProjectRecordId);
        if (beOutsourceProjectRecord == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("外包入项信息[%s]不存在", outsourceProjectRecordId));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindOutsourceProjectRecordOrder, FindOutsourceProjectRecordResult> context) {
        FindOutsourceProjectRecordOrder order = context.getOrder();
        FindOutsourceProjectRecordResult result = context.getResult();

        BeOutsourceProjectRecord beOutsourceProjectRecord = beOutsourceProjectRecordDao.findById(order.getOutsourceProjectRecordId());


        BeOutsourceProjectRecordDTO beOutsourceProjectRecordDTO = new BeOutsourceProjectRecordDTO();
        beOutsourceProjectRecordDTO.setId(beOutsourceProjectRecord.getOutsourceId());
        beOutsourceProjectRecordDTO.setApplyName(beOutsourceProjectRecord.getApplyName());
        beOutsourceProjectRecordDTO.setApplyTitle(beOutsourceProjectRecord.getApplyTitle());
        beOutsourceProjectRecordDTO.setOutsourceId(beOutsourceProjectRecord.getOutsourceId());
        beOutsourceProjectRecordDTO.setProjectName(beOutsourceProjectRecord.getProjectName());
        beOutsourceProjectRecordDTO.setStoName(beOutsourceProjectRecord.getStoName());
        beOutsourceProjectRecordDTO.setLeaderName(beOutsourceProjectRecord.getLeaderName());
        beOutsourceProjectRecordDTO.setPmName(beOutsourceProjectRecord.getPmName());
        beOutsourceProjectRecordDTO.setBdName(beOutsourceProjectRecord.getBdName());
        beOutsourceProjectRecordDTO.setSaName(beOutsourceProjectRecord.getSaName());
        beOutsourceProjectRecordDTO.setPlanProjectEnterDate(beOutsourceProjectRecord.getPlanProjectEnterDate());
        beOutsourceProjectRecordDTO.setPlanProjectLeaveDate(beOutsourceProjectRecord.getPlanProjectLeaveDate());
        beOutsourceProjectRecordDTO.setRealProjectEnterDate(beOutsourceProjectRecord.getRealProjectEnterDate());
        beOutsourceProjectRecordDTO.setRealProjectLeaveDate(beOutsourceProjectRecord.getRealProjectLeaveDate());
        beOutsourceProjectRecordDTO.setRecordStatus(beOutsourceProjectRecord.getRecordStatus());
        beOutsourceProjectRecordDTO.setRejectReason(beOutsourceProjectRecord.getRejectReason());
        beOutsourceProjectRecordDTO.setScore1(beOutsourceProjectRecord.getScore1());
        beOutsourceProjectRecordDTO.setScore1Content(beOutsourceProjectRecord.getScore1Content());
        beOutsourceProjectRecordDTO.setScore2(beOutsourceProjectRecord.getScore2());
        beOutsourceProjectRecordDTO.setScore2Content(beOutsourceProjectRecord.getScore2Content());

        result.setBeOutsourceProjectRecordDTO(beOutsourceProjectRecordDTO);
    }
}
