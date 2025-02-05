package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.EmployeeType;
import org.antframework.configcenter.facade.vo.ServiceStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeEmployeeOrder extends AbstractQueryOrder {

    @QueryEQ(attrName="id")
    private Long id;

    @QueryEQ(attrName="superiorId")
    private Long superiorId;

    @QueryLike(attrName="penName")
    private String penName;

    @QueryEQ(attrName="serviceStatus")
    private ServiceStatus serviceStatus;

    @QueryEQ(attrName="employeeType")
    private EmployeeType employeeType;

}
