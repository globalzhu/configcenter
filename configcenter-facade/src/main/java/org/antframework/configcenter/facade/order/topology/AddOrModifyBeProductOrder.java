package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.ProductClass;
import org.antframework.configcenter.facade.vo.ProductDevStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyBeProductOrder extends AbstractOrder {

    private Long id;

    private Long parentId;

    private String productName;

    private String productCode;

    private String pdName;

    private String devName;

    private String approvePeople;

    private String quotationScope;

    private String maturityDegree;

    private ProductDevStatus productDevStatus;

    private ProductClass productClass;

}
