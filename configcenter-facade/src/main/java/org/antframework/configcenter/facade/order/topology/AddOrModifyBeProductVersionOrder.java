package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.ProductDevStatus;
import org.antframework.configcenter.facade.vo.VersionStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyBeProductVersionOrder extends AbstractOrder {

    private Long id;

    private Long productId;

    private String versionNum;

    private String versionPdName;

    private String versionDevName;

    private Date releaseTime;

    private String releaseUrl;

    private VersionStatus versionStatus;

}
