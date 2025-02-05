package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryGTE;
import org.antframework.common.util.query.annotation.operator.QueryLTE;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.NodeStatus;
import org.antframework.configcenter.facade.vo.OrderStatus;
import org.antframework.configcenter.facade.vo.ProductType;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryOrdersOrder extends AbstractQueryOrder {

    // 产品类型
    @QueryEQ(attrName="productType")
    private ProductType productType;

    // 产品类型
    @QueryLike(attrName="productVersion")
    private String productVersion;

    // 项目负责人
    @QueryLike(attrName="projectOwner")
    private String projectOwner;

    // 运维负责人
    @QueryLike(attrName="deployOwner")
    private String deployOwner;

    @QueryLTE(attrName="deployTime")
    Date deployStartTime;

    @QueryGTE(attrName="deployTime")
    Date deployEndTime;

    // 工单状态
    @QueryEQ(attrName="orderStatus")
    private OrderStatus orderStatus;

    // 客户ID
    @QueryEQ(attrName="customerId")
    private Long customerId;

}
