package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.MeshStatus;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 7:32 PM
 * @project configcenter
 */

@Getter
@Setter
public class AddOrModifyLinkOrder extends AbstractOrder {

    //组网id
    private Long id;

    // 本方节点id
    @NotNull
    private Long hostNodeId;

    // 合作方节点id
    @NotNull
    private Long guestNodeId;

    // 组网时间
    private Date meshDate;

    // 组网场景
    private String meshScene;

    // 组网状态
    private MeshStatus meshStatus;

}
