package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.antframework.configcenter.facade.vo.RequirementType;
import org.antframework.configcenter.facade.vo.WorkOrderStatus;
import org.antframework.configcenter.facade.vo.WorkOrderType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 工时工单信息
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeWorkOrder extends AbstractEntity {

        @Column
        private Long projectId;

        @Column(length = 255)
        private String beSystemIds;

        @Column
        private Long projectWorkId;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ProjectStatus projectStatus;

        @Column
        private Long employeeId;

        @Column
        private Long productId;

        @Column
        private Long productVersionId;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private RequirementType requirementType;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private WorkOrderType workOrderType;

        @Column
        private Date workDate;

        @Column
        private Double manDay;

        @Column(length = 2048)
        private String workContent;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private WorkOrderStatus workOrderStatus;

        @Column(length = 255)
        private String approveProgress;

        @Column(length = 255)
        private String rejectReason;

}
