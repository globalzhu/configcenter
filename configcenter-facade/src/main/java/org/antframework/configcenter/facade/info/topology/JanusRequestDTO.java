package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

import java.util.Map;

/**
 * @author zongzheng
 * @date 2022/11/18 4:20 PM
 * @project configcenter
 */
@Getter
@Setter
public class JanusRequestDTO<T> extends AbstractInfo {

    private String method;

    private Map<String,T> content;

}
