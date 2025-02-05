package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.CustomerStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeCustomerInfoDTO extends AbstractInfo {

    // 客户id
    private Long id;

    // 客户名称
    private String customerName;

    // 客户编码
    private String customerCode;

    // 客户状态
    private CustomerStatus customerStatus;

    // 创建时间
    private Date createTime;

    // 修改时间
    private Date updateTime;


}
