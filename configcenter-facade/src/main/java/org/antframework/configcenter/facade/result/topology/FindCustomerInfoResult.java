/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:59 创建
 */
package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.AppInfo;
import org.antframework.configcenter.facade.info.topology.BeCustomerInfoDTO;

/**
 * 查找应用result
 */
@Getter
@Setter
public class FindCustomerInfoResult extends AbstractResult {
    private BeCustomerInfoDTO customerInfo;
}
