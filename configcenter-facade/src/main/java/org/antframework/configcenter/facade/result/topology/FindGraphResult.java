package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.topology.BeTopologyLinkDTO;
import org.antframework.configcenter.facade.info.topology.BeTopologyNodeDTO;

import java.util.List;

/**
 * @author zongzheng
 * @date 2022/10/20 6:51 PM
 * @project configcenter
 */
@Getter
@Setter
public class FindGraphResult extends AbstractResult {

    // 节点列表
    private List<BeTopologyNodeDTO> nodeList;

    //组网列表
    private List<BeTopologyLinkDTO> linkList;

}
