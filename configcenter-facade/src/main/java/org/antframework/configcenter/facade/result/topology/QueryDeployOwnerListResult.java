package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;

import java.util.Map;

/**
 * @author zongzheng
 * @date 2022/11/18 3:30 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryDeployOwnerListResult extends AbstractResult {

    private Map<String,String> deplymentOwnerMap;

}
