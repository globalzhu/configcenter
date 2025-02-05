package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.antframework.configcenter.facade.vo.RequirementType;
import org.antframework.configcenter.facade.vo.WorkOrderStatus;
import org.antframework.configcenter.facade.vo.WorkOrderType;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyBeWorkOrderOrder extends AbstractOrder {

    private Long id;

    private Long projectId;

    private String beSystemIds;

    private Long projectWorkId;

    private ProjectStatus projectStatus;

    private Long employeeId;

    private String loginName;

    private Long productId;

    private Long productVersionId;

    private RequirementType requirementType;

    private WorkOrderType workOrderType;

    private Date workDate;

    private Double manDay;

    private String workContent;

    private WorkOrderStatus workOrderStatus;

    private String approveProgress;

    private String rejectReason;

    private Date createTime;

}
