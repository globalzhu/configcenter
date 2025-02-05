package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.BeProjectType;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 项目信息管理
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeProject extends AbstractEntity {

        @Column(length = 255)
        private String projectName;

        @Column(length = 255)
        private String projectCode;

        @Column(length = 255)
        private String projectMemo;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private BeProjectType beProjectType;

        @Column
        private Long customerId;

        @Column(length = 255)
        private String accessPeople;

        @Column(length = 255)
        private String customerContact;

        @Column(length = 255)
        private String customerContactInfo;

        @Column(length = 255)
        private String bdName;

        @Column(length = 255)
        private String saName;

        @Column(length = 255)
        private String pmName;

        @Column(length = 255)
        private String stoName;

        @Column(length = 255)
        private String approvePeople;

        @Column
        private Date beginTime;

        @Column
        private Date endTime;

        @Column(length = 255)
        private String productIds;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ProjectStatus projectStatus;
}
