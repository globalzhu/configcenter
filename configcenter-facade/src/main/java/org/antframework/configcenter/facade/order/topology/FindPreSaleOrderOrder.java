/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:59 创建
 */
package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

/**
 * 查找售前工单
 */
@Getter
@Setter
public class FindPreSaleOrderOrder extends AbstractOrder {
    private Long preSaleOrderId;
}
