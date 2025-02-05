package org.antframework.configcenter.facade.vo;

/**
 * 节点状态
 */
public enum NodeStatus {
    /**
     * 待部署
     */
    WAITING,

    /**
     * 正常
     */
    ONLINE,

    /**
     * 异常
     */
    ERROR,

    /**
     * 下线
     */
    OFFLINE

}
