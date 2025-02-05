package org.antframework.configcenter.facade.vo;

/**
 * 工单状态
 */
public enum OrderStatus {
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
     * 部署前准备中
     */
    WAITING_PREPARE,

    /**
     * 部署中
     */
    DEPLOYING,

    /**
     * 完成部署
     */
    FINISHED,

    /**
     * 废弃
     */
    DELETED

}
