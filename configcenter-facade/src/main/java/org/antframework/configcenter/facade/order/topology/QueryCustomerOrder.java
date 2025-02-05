package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryGTE;
import org.antframework.common.util.query.annotation.operator.QueryLTE;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.CustomerStatus;
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
public class QueryCustomerOrder extends AbstractQueryOrder {

    // 客户名称
    @QueryLike(attrName="customerName")
    private String customerName;

    // 客户状态
    @QueryEQ(attrName="customerStatus")
    private CustomerStatus customerStatus;

    // 机构编码
    @QueryLike(attrName="customerCode")
    private String customerCode;

}
