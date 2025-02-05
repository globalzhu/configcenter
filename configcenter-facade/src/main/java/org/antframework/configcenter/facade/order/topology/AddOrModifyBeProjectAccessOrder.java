package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.AccessLevel;
import org.antframework.configcenter.facade.vo.AccessStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyBeProjectAccessOrder extends AbstractOrder {

    private Long id;

    private Long projectId;

    private Long employeeId;

    private AccessLevel accessLevel;

    private AccessStatus accessStatus;

}
