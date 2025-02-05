package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * @author zongzheng
 * @date 2022/11/18 6:24 PM
 * @project configcenter
 */
@Getter
@Setter
public class JanusResponseDTO extends AbstractInfo {

    private String code;

    private String content;

    private String message;

    private String signature;

    private boolean success;

}
