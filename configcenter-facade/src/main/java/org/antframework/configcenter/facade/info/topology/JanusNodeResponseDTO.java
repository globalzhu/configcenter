package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * @author zongzheng
 * @date 2022/11/18 4:26 PM
 * @project configcenter
 */
@Getter
@Setter
public class JanusNodeResponseDTO extends AbstractInfo {

    String node_id;

    String node_type;

    String inst_id;

    String inst_type;

    String inst_name;
}
