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
public class BeProjectWork extends AbstractEntity {

        @Column
        private Long projectId;

        @Column(length = 255)
        private String workTitle;

        @Column
        private Long createEmployeeId;

}
