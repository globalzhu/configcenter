package org.antframework.configcenter.dal.entity;


import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.LanguageType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 人员管理
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeSystem extends AbstractEntity {

        @Column(length = 255)
        private String systemCode;

        @Column(length = 255)
        private String systemName;

        @Column(length = 255)
        private String systemOwner;

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private LanguageType languageType;

}
