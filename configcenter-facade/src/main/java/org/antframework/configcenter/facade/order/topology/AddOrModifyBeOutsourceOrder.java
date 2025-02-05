package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.PreSaleOrderStatus;
import org.antframework.configcenter.facade.vo.PreSaleOrderType;
import org.antframework.configcenter.facade.vo.ProductType;
import org.antframework.configcenter.facade.vo.ServiceStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyBeOutsourceOrder extends AbstractOrder {

    private Long id;

    private String name;

    private String penName;

    private String phoneNum;

    private String certNo;

    private String sex;

    private String education;

    private Long salary;

    private String baseSite;

    private String major;

    private Long workAge;

    private String resumeFile;

    private Date entryTime;

    private Date leaveTime;

    private ServiceStatus serviceStatus;

}
