/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.boot.bekit.CommonQueries;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.*;
import org.antframework.configcenter.dal.entity.*;
import org.antframework.configcenter.facade.order.topology.QueryBeProjectOrderWrapper;
import org.antframework.configcenter.facade.result.topology.QueryBeProjectResult;
import org.antframework.configcenter.facade.vo.AccessStatus;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.bekit.service.ServiceEngine;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 查找工时工单服务
 */
@Service
@AllArgsConstructor
public class QueryBeProjectListService {

    private final BeEmployeeDao beEmployeeDao;
    private final BeProjectAccessDao beProjectAccessDao;

    private final ServiceEngine serviceEngine;

    @ServiceBefore
    public void before(ServiceContext<QueryBeProjectOrderWrapper, QueryBeProjectResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<QueryBeProjectOrderWrapper, QueryBeProjectResult> context) {

        QueryBeProjectResult filteredResult = context.getResult();
        Boolean isFiltered = context.getOrder().getIsFiltered();

        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, context.getOrder().getQueryBeProjectOrder(), QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeProjectDao.class));
        QueryBeProjectResult queryBeProjectResult =  result.convertTo(QueryBeProjectResult.class);

        if(!queryBeProjectResult.isSuccess()){
            filteredResult.setCode(queryBeProjectResult.getCode());
            filteredResult.setMessage(queryBeProjectResult.getMessage());
            filteredResult.setStatus(queryBeProjectResult.getStatus());
            return;
        }

        /**
         * 特殊判断逻辑
         * ProjectStatus为PRE_SALE售前阶段时，要做权限判断
         * 只有对应有权限的人才能看到该项目名称
         */
        String loginUserName = context.getOrder().getLoginUserName();
        BeEmployee beEmployee = beEmployeeDao.findByWorkLoginName(loginUserName);

        if(beEmployee == null){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("员工信息[%s]不存在", loginUserName));
        }

        queryBeProjectResult.getInfos().stream().forEach(
                beProjectDTO -> {
                    Long projectId = beProjectDTO.getId();
                    ProjectStatus projectStatus = beProjectDTO.getProjectStatus();

                    if(isFiltered && ProjectStatus.PRE_SALE.equals(projectStatus)){
                        // 特殊逻辑：如果是weiruo，jiuhe，zongzheng的账号，能看到所有项目
                        if(loginUserName != null && (
                                "weiruo".equals(loginUserName)
                                        ||"zongzheng".equals(loginUserName)
                                        ||"jiuhe".equals(loginUserName)
                                        ||"ningxin".equals(loginUserName))) {
                            filteredResult.addInfo(beProjectDTO);
                        }else{
                            BeProjectAccess beProjectAccess
                                    = beProjectAccessDao.findByEmployeeIdAndProjectIdAndAccessStatus(
                                    beEmployee.getId(),projectId, AccessStatus.NORMAL);
                            if(beProjectAccess != null){
                                // 有权限
                                filteredResult.addInfo(beProjectDTO);
                            }
                        }
                    }else{
                        filteredResult.addInfo(beProjectDTO);
                    }
                }
        );
        filteredResult.setTotalCount(queryBeProjectResult.getTotalCount());
    }

}
