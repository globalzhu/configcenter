package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.CustomerStatus;

import javax.persistence.*;

/**
 * @author zongzheng
 * @date 2022/10/20 1:56 PM
 * @project configcenter
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_customerCode", columnNames = "customerCode"))
@Getter
@Setter
public class BeCustomerInfo extends AbstractEntity {

    // 客户名称
    @Column(length = 255)
    private String customerName;

    // 客户编码
    @Column(length = 255)
    private String customerCode;

    // 客户状态
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

}
