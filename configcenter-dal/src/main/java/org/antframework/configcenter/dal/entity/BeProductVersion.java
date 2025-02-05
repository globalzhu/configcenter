package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.VersionStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 产品版本信息
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeProductVersion extends AbstractEntity {

        @Column
        private Long productId;

        @Column(length = 255)
        private String versionNum;

        @Column(length = 255)
        private String versionPdName;

        @Column(length = 255)
        private String versionDevName;

        @Column
        private Date releaseTime;

        @Column(length = 255)
        private String releaseUrl;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private VersionStatus versionStatus;

}
