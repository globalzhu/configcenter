/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.biz.util.Apps;
import org.antframework.configcenter.biz.util.Profiles;
import org.antframework.configcenter.dal.dao.BranchDao;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.ProfileInfo;
import org.antframework.configcenter.facade.order.LockBranchOrder;
import org.antframework.configcenter.facade.order.UnLockBranchOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 封版
 */
@Service(enableTx = true)
@AllArgsConstructor
public class UnLockBranchService {

    // 分支dao
    private final BranchDao branchDao;

    @ServiceBefore
    public void before(ServiceContext<UnLockBranchOrder, EmptyResult> context) {
        UnLockBranchOrder order = context.getOrder();
        // 校验入参
        AppInfo app = Apps.findApp(order.getAppId());
        if (app == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("应用[%s]不存在", order.getAppId()));
        }
        ProfileInfo profile = Profiles.findProfile(order.getProfileId());
        if (profile == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("环境[%s]不存在", order.getProfileId()));
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<UnLockBranchOrder, EmptyResult> context) {
        UnLockBranchOrder order = context.getOrder();
        EmptyResult result = context.getResult();
        Branch branch = branchDao.findLockByAppIdAndProfileIdAndBranchId(order.getAppId(), order.getProfileId(), order.getBranchId());
        if (branch == null) {
            throw new BizException(Status.FAIL, CommonResultCode.ILLEGAL_STATE.getCode(), String.format("版本[appId=%s,profileId=%s,version=%d]不存在", order.getAppId(), order.getProfileId(), order.getBranchId()));
        }

        branch.setLockVersion("NO");
        branchDao.save(branch);
    }
}
