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
import org.antframework.configcenter.dal.dao.BeTopologyLinkDao;
import org.antframework.configcenter.dal.dao.BeTopologyNodeDao;
import org.antframework.configcenter.dal.entity.BeTopologyLink;
import org.antframework.configcenter.dal.entity.BeTopologyNode;
import org.antframework.configcenter.facade.order.topology.AddOrModifyLinkOrder;
import org.antframework.configcenter.facade.vo.MeshStatus;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.Date;

/**
 * 添加或修改应用服务
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyLinkService {
    // 应用dao
    private final BeTopologyNodeDao beTopologyNodeDao;

    private final BeTopologyLinkDao beTopologyLinkDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyLinkOrder, EmptyResult> context) {
        AddOrModifyLinkOrder order = context.getOrder();
        Long linkId = order.getId();
        Long guestNodeId = order.getGuestNodeId();
        Long hostNodeId = order.getHostNodeId();

        BeTopologyNode guestNode = beTopologyNodeDao.findById(guestNodeId);
        if(guestNode == null) throw new BizException(
                Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "guestNode is null，guestNodeId="+guestNodeId);

        BeTopologyNode hostNode = beTopologyNodeDao.findById(hostNodeId);
        if(hostNode == null) throw new BizException(
                Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "hostNode is null，hostNode="+hostNode);

        if(linkId != null && linkId.longValue() > 0) {
            if (beTopologyLinkDao.findById(linkId) == null) throw new BizException(
                    Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), "beTopologyLink is null，linkId=" + linkId);
        }
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyLinkOrder, EmptyResult> context) {
        AddOrModifyLinkOrder order = context.getOrder();

        Long linkId = order.getId();
        Long guestNodeId = order.getGuestNodeId();
        Long hostNodeId = order.getHostNodeId();
        Date meshDate = order.getMeshDate();
        String meshScene = order.getMeshScene();
        MeshStatus meshStatus = order.getMeshStatus();

        BeTopologyLink beTopologyLink = null;
        if(linkId != null && linkId.longValue() > 0){
            beTopologyLink = beTopologyLinkDao.findById(linkId);
        }else{
            beTopologyLink = new BeTopologyLink();
        }

        beTopologyLink.setMeshStatus(meshStatus);
        beTopologyLink.setHostNodeId(hostNodeId);
        beTopologyLink.setMeshDate(meshDate);
        beTopologyLink.setGuestNodeId(guestNodeId);
        beTopologyLink.setMeshScene(meshScene);

        beTopologyLinkDao.save(beTopologyLink);
    }

}
