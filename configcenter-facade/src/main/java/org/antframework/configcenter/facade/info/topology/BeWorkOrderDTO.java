package org.antframework.configcenter.facade.info.topology;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.antframework.configcenter.facade.vo.RequirementType;
import org.antframework.configcenter.facade.vo.WorkOrderStatus;
import org.antframework.configcenter.facade.vo.WorkOrderType;

import java.util.Date;
import java.util.List;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeWorkOrderDTO extends AbstractInfo {

    private Long id;

    private Long projectId;

    private String beSystemIds;

    private List<BeSystemDTO> beSystemDTOList;

    private Long projectWorkId;

    private BeProjectWorkDTO beProjectWorkDTO;

    private BeProjectDTO beProjectDTO;

    private ProjectStatus projectStatus;

    private Long employeeId;

    private BeEmployeeDTO beEmployeeDTO;

    private Long productId;

    private BeProductDTO beProductDTO;

    private Long productVersionId;

    private BeProductVersionDTO beProductVersionDTO;

    private RequirementType requirementType;

    private WorkOrderType workOrderType;

    private Date workDate;

    // yyyy-mm-dd
    private String workDateStr;

    private Double manDay;

    private String workContent;

    private WorkOrderStatus workOrderStatus;

    private String approveProgress;

    private String rejectReason;

    private Date createTime;

    private Date updateTime;

}
