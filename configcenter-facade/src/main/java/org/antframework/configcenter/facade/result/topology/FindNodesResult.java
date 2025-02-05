/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-13 15:31 创建
 */
package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.topology.BeTopologyNodeDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FindNodesResult extends AbstractResult {
    // 应用在环境下的所有分支
    private final List<BeTopologyNodeDTO> nodes = new ArrayList<>();

    public void addNode(BeTopologyNodeDTO note) {
        nodes.add(note);
    }
}
