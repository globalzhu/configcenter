package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.AccessLevel;
import org.antframework.configcenter.facade.vo.AccessStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeProjectAccessDTO extends AbstractInfo {

    private Long id;

    private Long projectId;

    private BeProjectDTO beProjectDTO;

    private Long employeeId;

    private BeEmployeeDTO beEmployeeDTO;

    private AccessLevel accessLevel;

    private AccessStatus accessStatus;

    private Date createTime;

    private Date updateTime;

}
