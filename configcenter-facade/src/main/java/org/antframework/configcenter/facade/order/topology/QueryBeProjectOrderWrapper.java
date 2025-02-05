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
public class QueryBeProjectOrderWrapper{

    private QueryBeProjectOrder QueryBeProjectOrder;

    private String loginUserName;

    private Boolean isFiltered = Boolean.TRUE;

}
