/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-12-08 22:40 创建
 */
package org.antframework.configcenter.facade.order;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.tostring.format.Mask;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.facade.vo.ValueType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增或修改配置value-order
 */
@Getter
@Setter
public class AddOrModifyPropertyValueOrder extends AbstractOrder {
    // 应用id
    @NotBlank
    private String appId;
    // 环境id
    @NotBlank
    private String profileId;
    // 分支id
    @NotBlank
    private String branchId;
    // key
    @NotBlank
    private String key;
    // value
    @NotBlank
    @Mask(secureMask = true)
    private String value;
    // 作用域
    @NotNull(message = "作用域不能为空")
    private Scope scope;
    // 配置项类型
    @NotNull(message = "配置项类型不能为空")
    private ValueType valueType;
}
