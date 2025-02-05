package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectAccessDTO;
import org.antframework.configcenter.facade.info.topology.BeProjectDTO;

import java.util.List;

/**
 * @author zongzheng
 * @date 2022/10/20 6:52 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeProjectAccessResultWrapper {

    private BeProjectDTO beProjectDTO;

    private List<BeProjectAccessDTO> beProjectAccessDTOList;

}
