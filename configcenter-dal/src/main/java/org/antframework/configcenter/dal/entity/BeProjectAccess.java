package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.AccessLevel;
import org.antframework.configcenter.facade.vo.AccessStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 项目员工权限表
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeProjectAccess extends AbstractEntity {

        @Column
        private Long projectId;

        @Column
        private Long employeeId;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private AccessLevel accessLevel;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private AccessStatus accessStatus;

}
