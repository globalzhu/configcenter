package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.topology.BeTopologyNodeDTO;

/**
 * @author zongzheng
 * @date 2022/10/20 6:52 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryNodesResult extends AbstractQueryResult<BeTopologyNodeDTO> {
}
