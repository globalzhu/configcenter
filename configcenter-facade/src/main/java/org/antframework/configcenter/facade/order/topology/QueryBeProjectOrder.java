package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.query.annotation.operator.QueryEQ;
import org.antframework.common.util.query.annotation.operator.QueryLike;
import org.antframework.configcenter.facade.vo.BeProjectType;
import org.antframework.configcenter.facade.vo.ProjectStatus;

/**
 * @author zongzheng
 * @date 2022/10/20 6:37 PM
 * @project configcenter
 */
@Getter
@Setter
public class QueryBeProjectOrder extends AbstractQueryOrder {

    @QueryEQ(attrName="beProjectType")
    private BeProjectType beProjectType;

    @QueryEQ(attrName="projectStatus")
    private ProjectStatus projectStatus;

    @QueryLike(attrName="projectName")
    private String projectName;

}
