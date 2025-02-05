package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;


@Getter
@Setter
public class JanusQueryNodeResponseDTO extends AbstractInfo {

    String instId;
    String instName;
    String nodeId;
    String mid;
    String nodeCrt;
    String priKey;
    String rootCrt;
    String rootKey;
    String version;
}
