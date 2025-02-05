package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.EmployeeType;
import org.antframework.configcenter.facade.vo.PostType;
import org.antframework.configcenter.facade.vo.ServiceStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 人员管理
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeEmployee extends AbstractEntity {

        @Column(length = 255)
        private String name;

        @Column(length = 255)
        private String penName;

        @Column(length = 255)
        private String workLoginName;

        @Column(length = 255)
        private String phoneNum;

        @Column
        private Long superiorId;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private EmployeeType employeeType;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private PostType postType;

        @Column
        private Long employeeLevel;

        @Column
        private Long unitCost;

        @Column(length = 255)
        private String certNo;

        @Column(length = 255)
        private String sex;

        @Column(length = 255)
        private String education;

        @Column(length = 255)
        private String salary;

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
