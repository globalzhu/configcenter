package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;

import javax.validation.constraints.NotBlank;

/**
 * @author zongzheng
 * @date 2022/10/20 4:50 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryGraphOrder extends AbstractOrder {

    // 是否查询全图
    private boolean isQueryAllGraph;

    // 节点id，本方节点或对方节点
    @NotBlank
    private long nodeId;

    // 探索度数
    @NotBlank
    private int degree;

}