/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-13 15:34 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.configcenter.dal.dao.BeTopologyNodeDao;
import org.antframework.configcenter.dal.entity.BeTopologyNode;
import org.antframework.configcenter.facade.info.topology.BeTopologyNodeDTO;
import org.antframework.configcenter.facade.order.topology.FindNodesOrder;
import org.antframework.configcenter.facade.result.topology.FindNodesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class FindNodesService {

    // 分支dao
    private final BeTopologyNodeDao beTopologyNodeDao;

    @ServiceExecute
    public void execute(ServiceContext<FindNodesOrder, FindNodesResult> context) {

        FindNodesOrder order = context.getOrder();
        FindNodesResult result = context.getResult();

        List<Long> nodeIdList = order.getNodeIdList();
        List<BeTopologyNode> nodes = beTopologyNodeDao.findByIdIn(nodeIdList);

        if(!CollectionUtils.isEmpty(nodes)){
            nodes.stream().forEach(node ->{
                BeTopologyNodeDTO nodeDTO = new BeTopologyNodeDTO();
                nodeDTO.setId(node.getId());
                nodeDTO.setNodeName(node.getNodeName());
                nodeDTO.setNodeCode(node.getNodeCode());
                nodeDTO.setNodeStatus(node.getNodeStatus());
                nodeDTO.setNodeType(node.getNodeType());
                nodeDTO.setConnectGlab(node.getConnectGlab());
                nodeDTO.setCustomerId(node.getCustomerId());
                nodeDTO.setDeployEnv(node.getDeployEnv());
                nodeDTO.setDeployOwner(node.getDeployOwner());
                nodeDTO.setFinishTest(node.getFinishTest());
                nodeDTO.setDeployTime(node.getDeployTime());
                nodeDTO.setLicenseExpireTime(node.getLicenseExpireTime());
                nodeDTO.setLicenseValidDate(node.getLicenseValidDate());
                nodeDTO.setMachineConfig(node.getMachineConfig());
                nodeDTO.setNetworkConfig(node.getNetworkConfig());
                nodeDTO.setRemark(node.getRemark());
                nodeDTO.setScene(node.getScene());
                nodeDTO.setProjectOwner(node.getProjectOwner());
                nodeDTO.setProjectType(node.getProjectType());
                nodeDTO.setProductType(node.getProductType());
                nodeDTO.setProductVersion(node.getProductVersion());
                nodeDTO.setDeployType(node.getDeployType());
                result.addNode(nodeDTO);
            });
        }

    }
}
