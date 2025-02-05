package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.ProductClass;
import org.antframework.configcenter.facade.vo.ProductDevStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeProductDTO extends AbstractInfo {

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

    private Date createTime;

    private Date updateTime;

    private String relSystemIds;

    private ProductClass productClass;

}
