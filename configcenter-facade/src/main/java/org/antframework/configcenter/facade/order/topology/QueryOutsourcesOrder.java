package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.ServiceStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryOutsourcesOrder extends AbstractQueryOrder {

    @QueryEQ(attrName="serviceStatus")
    private ServiceStatus serviceStatus;

    @QueryLike(attrName="name")
    private String name;

    @QueryLike(attrName="penName")
    private String penName;

}
