package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.configcenter.facade.vo.VersionStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeProductVersionOrder extends AbstractQueryOrder {

    @QueryEQ(attrName="versionStatus")
    private VersionStatus versionStatus;

    @QueryEQ(attrName="productId")
    private Long productId;

}
