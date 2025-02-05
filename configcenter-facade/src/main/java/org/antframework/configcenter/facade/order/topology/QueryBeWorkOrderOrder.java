package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryGTE;
import org.antframework.common.util.query.annotation.operator.QueryIn;
import org.antframework.common.util.query.annotation.operator.QueryLTE;
import org.antframework.configcenter.facade.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeWorkOrderOrder extends AbstractQueryOrder {

    @QueryEQ(attrName="requirementType")
    private RequirementType requirementType;

    @QueryEQ(attrName="workOrderType")
    private WorkOrderType workOrderType;

    @QueryEQ(attrName="workOrderStatus")
    private WorkOrderStatus workOrderStatus;

    @QueryIn(attrName="projectId")
    private List<Long> projectId;

    @QueryIn(attrName="employeeId")
    private List<Long> employeeId;

    @QueryEQ(attrName="productId")
    private Long productId;

    @QueryEQ(attrName="productVersionId")
    private Long productVersionId;

    @QueryGTE(attrName="workDate")
    Date workStartTime;

    @QueryLTE(attrName="workDate")
    Date workEndTime;

    @QueryIn(attrName="projectStatus")
    private List<ProjectStatus> projectStatus;

}
