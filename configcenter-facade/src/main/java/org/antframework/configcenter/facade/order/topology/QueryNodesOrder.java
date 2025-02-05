package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.*;
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
public class QueryNodesOrder extends AbstractQueryOrder {

    // 应用id
    @QueryLike(attrName="nodeName")
    private String nodeName;

    // 应用编码
    @QueryEQ(attrName="nodeCode")
    private String nodeCode;

    // 产品类型
    @QueryEQ(attrName="productType")
    private ProductType productType;

    // 产品类型
    @QueryLike(attrName="productVersion")
    private String productVersion;

    // 产品类型
    @QueryEQ(attrName="nodeStatus")
    private NodeStatus nodeStatus;

    // 产品类型
    @QueryLike(attrName="deployOwner")
    private String deployOwner;

    @QueryLTE(attrName="deployTime")
    Date deployStartTime;

    @QueryGTE(attrName="deployTime")
    Date deployEndTime;
}
