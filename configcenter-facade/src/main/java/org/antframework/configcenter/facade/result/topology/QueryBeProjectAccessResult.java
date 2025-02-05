package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectAccessDTO;

import java.util.List;

/**
 * @author zongzheng
 * @date 2022/10/20 6:52 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeProjectAccessResult extends AbstractResult {

    private BeEmployeeDTO beEmployeeDTO;

    private List<QueryBeProjectAccessResultWrapper> projectWrapper;

}
