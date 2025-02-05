package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeProjectWorkDTO extends AbstractInfo {

    private Long id;

    private Long projectId;

    private String workTitle;

    private Long createEmployeeId;

    private BeEmployeeDTO createEmployeeDTO;

    private Date createTime;

    private Date updateTime;

}
