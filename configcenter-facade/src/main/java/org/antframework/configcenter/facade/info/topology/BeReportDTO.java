package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.AccessType;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeReportDTO extends AbstractInfo {

    private Long id;

    private String reportName;

    private String reportUrl;

    private AccessType accessType;

    private String accessPeople;

    private Long position;

}
