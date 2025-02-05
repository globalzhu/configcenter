package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.topology.BeEmployeeDTO;

/**
 * @author zongzheng
 * @date 2024/1/31 9:56 AM
 * @project configcenter
 */
@Getter
@Setter
public class WeekFinishResult  extends AbstractResult {

    private Boolean isFinishedThisWeek = Boolean.FALSE;

    private String noticeMessage;

    private BeEmployeeDTO beEmployeeDTO;

}
