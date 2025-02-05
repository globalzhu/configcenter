package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.CustomerStatus;
import org.antframework.configcenter.facade.vo.MeshStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeTopologyLinkDTO extends AbstractInfo {

    // 组网id
    private Long id;

    // 本方hostId
    private Long hostNodeId;

    // 本方节点信息
    private BeTopologyNodeDTO hostNode;

    // 合作方hostId
    private Long guestNodeId;

    // 本方节点信息
    private BeTopologyNodeDTO guestNode;

    // 组网时间
    private Date meshDate;

    // 组网场景
    private String meshScene;

    // 组网状态
    private MeshStatus meshStatus;

    // 创建时间
    private Date createTime;

    // 修改时间
    private Date updateTime;

}
