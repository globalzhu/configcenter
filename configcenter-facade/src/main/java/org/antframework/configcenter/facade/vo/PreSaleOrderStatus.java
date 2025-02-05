package org.antframework.configcenter.facade.vo;

/**
 * 工单状态
 */
public enum PreSaleOrderStatus {
    /**
     * 待审批
     */
    WAITING_APPROVE,

    /**
     * 审批拒绝
     */
    APPROVE_REJECT,

    /**
     * 待分配运维
     */
    WAITING_DEPLOY_OWNER,

    /**
     * 待处理
     */
    WAITING_PREPARE,

    /**
     * 进行中
     */
    ORDER_DOING,

    /**
     * 完成部署
     */
    FINISHED,

    /**
     * 废弃
     */
    DELETED

}
