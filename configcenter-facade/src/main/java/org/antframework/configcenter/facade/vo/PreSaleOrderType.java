package org.antframework.configcenter.facade.vo;

/**
 * 部署工单类型
 */
public enum PreSaleOrderType {
    /**
     * 文档类
     */
    DOC,

    /**
     * 资源沟通类
     */
    COMMUNICATE,

    /**
     * 定制需求
     */
    SPECIAL,

    /**
     * 项目上线
     */
    PROJECT_PUBLISH,

    /**
     * 环境迁移/演练
     */
    PROJECT_TRANSFER,

    /**
     * 网络策略调整/联调
     */
    NETWORK_WORK,

    /**
     * 漏洞修复
     */
    LOOPHOLE_FIX,

    /**
     * 修复bug
     */
    BUG_FIX,

    /**
     * 硬件资源升级
     */
    HARDWARE_UPDATE,

    /**
     * 长期运维驻场
     */
    DEVOPS_LOCAL,

    /**
     * 其他需求
     */
    OTHER


}
