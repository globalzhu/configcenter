package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.*;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyPreSaleOrderOrder extends AbstractOrder {

    private Long id;

    // 客户id
    private String customerId;

    // 工单类型
    private PreSaleOrderType preSaleOrderType;

    // 项目名称，必填
    private String projectName;

    // 需求场景，必填
    private String scene;

    // 关联产品，非必填
    private ProductType productType;

    // 项目负责人
    private String projectOwner;

    // 工作需求描述
    private String content;

    // 计划完成时间
    private Date planFinishDate;

    // 工单初次创建时间
    private Date orderFirstCreateDate;

    // 派单人
    private String dispatchOwner;

    // 指派的运维负责人
    private String deployOwner;

    // 派单时间
    private Date orderDispatchDate;

    // 工作完成内容说明
    private String finishContent;

    // 完成时间
    private Date finishDate;

    private PreSaleOrderStatus preSaleOrderStatus;

    private Long score1;// 业务负责人评分
    private String score1Content;// 业务负责人评分备注
    private Long score2;// 运维负责人评分
    private String score2Content;// 运维负责人评分
    private String rejectReason;// 拒绝原因

}
