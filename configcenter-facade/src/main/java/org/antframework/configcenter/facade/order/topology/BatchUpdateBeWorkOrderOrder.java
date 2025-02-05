package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.antframework.configcenter.facade.vo.RequirementType;
import org.antframework.configcenter.facade.vo.WorkOrderStatus;
import org.antframework.configcenter.facade.vo.WorkOrderType;

import java.util.Date;
import java.util.List;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class BatchUpdateBeWorkOrderOrder extends AbstractOrder {

    private List<Long> idList;

    private WorkOrderStatus workOrderStatus;

    private String approveProgress;

    private String rejectReason;

}
