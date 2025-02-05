package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.AccessType;
import org.antframework.configcenter.facade.vo.ReportStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 外包人员信息管理
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeReport extends AbstractEntity {

        @Column(length = 255)
        private String reportName;

        @Column(length = 255)
        private String reportUrl;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private AccessType accessType;

        @Column(length = 255)
        private String accessPeople;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ReportStatus reportStatus;

        @Column
        private Long position;
}
