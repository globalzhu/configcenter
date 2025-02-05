package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.ProductDevStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeProductOrder extends AbstractQueryOrder {

    @QueryEQ(attrName="productDevStatus")
    private ProductDevStatus productDevStatus;

    @QueryLike(attrName="productName")
    private String productName;

    @QueryLike(attrName="productCode")
    private String productCode;

}
