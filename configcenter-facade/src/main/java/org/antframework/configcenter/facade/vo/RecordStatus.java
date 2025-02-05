package org.antframework.configcenter.facade.vo;

/**
 * 入项工单状态
 */
public enum RecordStatus {
    /**
     * 待审批
     */
    WAITING_APPROVE,

    /**
     * 审批拒绝
     */
    APPROVE_REJECT,

    /**
     * 通过
     */
    APPROVED,

    /**
     * 项目延期
     */
    DELAY,

    /**
     * 项目结束
     */
    FINISHED,

    /**
     * 废弃
     */
    DELETED

}
