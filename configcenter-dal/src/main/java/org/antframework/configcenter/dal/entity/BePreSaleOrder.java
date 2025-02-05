package org.antframework.configcenter.dal.entity;

/**
 * @author zongzheng
 * @date 2023/12/12 3:50 PM
 * @project configcenter
 */

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 售前工单
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BePreSaleOrder extends AbstractEntity {

        // 客户id
        @Column(length = 255)
        private String customerId;

        // 工单类型
        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private PreSaleOrderType preSaleOrderType;

        /********** 申请阶段，由业务人员提供 ***********/

        // 项目名称，必填
        @Column
        private String projectName;

        // 需求场景，必填
        @Column
        private String scene;

        // 关联产品，非必填
        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private ProductType productType;

        // 项目负责人
        @Column(length = 255)
        private String projectOwner;

        // 工作需求描述
        @Column
        private String content;

        // 计划完成时间
        @Column
        private Date planFinishDate;

        // 工单初次创建时间
        @Column
        private Date orderFirstCreateDate;

        /********** 准备阶段，待指派运维 ***********/

        // 派单人
        @Column(length = 255)
        private String dispatchOwner;

        // 指派的运维负责人
        @Column(length = 255)
        private String deployOwner;

        // 派单时间
        @Column
        private Date orderDispatchDate;

        /********** 实施阶段，由运维人员提供 ***********/

        // 工作完成内容说明
        @Column
        private String finishContent;

        // 完成时间
        @Column
        private Date finishDate;

        /********** 工单状态机 ***********/

        @Column(length = 255)
        @Enumerated(EnumType.STRING)
        private PreSaleOrderStatus preSaleOrderStatus;

        @Column
        private Long score1;

        @Column
        private String score1Content;

        @Column
        private Long score2;

        @Column
        private String score2Content;

        @Column
        private String rejectReason;

}
