package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryGTE;
import org.antframework.common.util.query.annotation.operator.QueryLTE;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.MeshStatus;
import org.antframework.configcenter.facade.vo.NodeStatus;
import org.antframework.configcenter.facade.vo.ProductType;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryLinksOrder extends AbstractQueryOrder {

    // 本方节点id
    @QueryEQ(attrName="hostNodeId")
    private Long hostNodeId;

    // 对方节点id
    @QueryEQ(attrName="guestNodeId")
    private Long guestNodeId;

    // 产品类型
    @QueryEQ(attrName="meshStatus")
    private MeshStatus meshStatus;

    // 组网开始时间
    @QueryLTE(attrName="meshDate")
    Date meshStartTime;

    // 组网结束时间
    @QueryGTE(attrName="meshDate")
    Date meshEndTime;
}
