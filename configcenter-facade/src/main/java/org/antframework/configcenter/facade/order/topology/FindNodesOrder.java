/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-13 15:29 创建
 */
package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class FindNodesOrder extends AbstractOrder {

    @NotNull
    private List<Long> nodeIdList;

}
