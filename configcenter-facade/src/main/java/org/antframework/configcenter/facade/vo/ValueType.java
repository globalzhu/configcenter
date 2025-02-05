package org.antframework.configcenter.facade.vo;

/**
 * 配置项类型
 */
public enum ValueType {
    /**
     * 产品功能变量：STO在环境变量中心完成配置，在客户现场不允许配置；若有变动，重新在环境变量中心配置再发版；
     */
    PRODUCT_FEATURE_VALUE,

    /**
     * 客户环境变量：运维同学基于现场实际情况定制化配置，例如MYSQl，REDIS等；
     */
    TOB_ENV_VALUE,

    /**
     * 客户集成变量：在客户侧集成基于现场实际情况定制化配置，例如同步登陆等；
     */
    TOB_INTEGRATE_VALUE
}
