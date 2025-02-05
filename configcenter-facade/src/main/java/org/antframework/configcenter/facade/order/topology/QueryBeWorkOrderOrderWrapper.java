package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeWorkOrderOrderWrapper {

    private QueryBeWorkOrderOrder queryBeWorkOrderOrder;

    private String loginName;

    private Long filteredEmployeeId;

}
