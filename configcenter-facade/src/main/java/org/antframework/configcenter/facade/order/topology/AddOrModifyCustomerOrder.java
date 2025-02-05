package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.CustomerStatus;

import javax.validation.constraints.NotBlank;

/**
 * @author zongzheng
 * @date 2022/10/20 7:32 PM
 * @project configcenter
 */

@Getter
@Setter
public class AddOrModifyCustomerOrder extends AbstractOrder {

    private Long id;

    // 客户名称
    private String customerName;

    // 机构编码
    @NotBlank
    private String customerCode;

    // 客户状态
    private CustomerStatus customerStatus;

}
