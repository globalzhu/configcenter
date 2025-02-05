package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.ServiceStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 外包人员信息管理
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeOutsource extends AbstractEntity {

        @Column(length = 255)
        private String name;

        @Column(length = 255)
        private String penName;

        @Column(length = 255)
        private String phoneNum;

        @Column(length = 255)
        private String certNo;

        @Column(length = 255)
        private String sex;

        @Column(length = 255)
        private String education;

        @Column
        private Long salary;

        @Column(length = 255)
        private String baseSite;

        @Column(length = 255)
        private String major;

        @Column
        private Long workAge;

        @Column(length = 255)
        private String resumeFile;

        @Column
        private Date entryTime;

        @Column
        private Date leaveTime;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ServiceStatus serviceStatus;

}
