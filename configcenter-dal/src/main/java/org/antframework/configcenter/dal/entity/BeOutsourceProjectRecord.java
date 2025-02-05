package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.RecordStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 外包人员入项信息
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeOutsourceProjectRecord extends AbstractEntity {

        @Column(length = 255)
        private String applyName;

        @Column(length = 255)
        private String applyTitle;

        @Column
        private Long outsourceId;

        @Column(length = 255)
        private String projectName;

        @Column(length = 255)
        private String stoName;

        @Column(length = 255)
        private String leaderName;

        @Column(length = 255)
        private String pmName;

        @Column(length = 255)
        private String bdName;

        @Column(length = 255)
        private String saName;

        @Column
        private Date planProjectEnterDate;

        @Column
        private Date planProjectLeaveDate;

        @Column
        private Date realProjectEnterDate;

        @Column
        private Date realProjectLeaveDate;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private RecordStatus recordStatus;

        @Column(length = 255)
        private String rejectReason;

        @Column
        private Long score1;

        @Column
        private String score1Content;

        @Column
        private Long score2;

        @Column
        private String score2Content;

        @Column
        private String approvePeople;

}
