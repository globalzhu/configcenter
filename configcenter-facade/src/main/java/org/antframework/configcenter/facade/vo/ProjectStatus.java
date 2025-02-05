package org.antframework.configcenter.facade.vo;

/**
 * 项目状态
 */
public enum ProjectStatus {
    /**
     * 售前阶段
     */
    PRE_SALE,

    /**
     * 交付中未签约
     */
    DELIVER_UNSIGNED
    ,
    /**
     * 交付中有签约
     */
    DELIVER_SIGNED
    ,

    /**
     * 售后维保
     */
    AFTER_SALE
    ,

    /**
     * 废弃
     */
    DELETED
    ,

    /**
     * 暂停
     */
    SUSPEND
    ,

    /**
     * 已完结
     */
    FINISHED

}
