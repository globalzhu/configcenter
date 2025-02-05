package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.ProductClass;
import org.antframework.configcenter.facade.vo.ProductDevStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 产品信息
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeProduct extends AbstractEntity {

        @Column
        private Long parentId;

        @Column(length = 255)
        private String productName;

        @Column(length = 255)
        private String productCode;

        @Column(length = 255)
        private String pdName;

        @Column(length = 255)
        private String devName;

        @Column(length = 255)
        private String approvePeople;

        @Column(length = 255)
        private String quotationScope;

        @Column(length = 255)
        private String maturityDegree;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ProductDevStatus productDevStatus;

        @Column(length = 255)
        private String relSystemIds;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ProductClass productClass;

}
