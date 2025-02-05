package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.configcenter.dal.dao.BeCustomerInfoDao;
import org.antframework.configcenter.dal.dao.BeTopologyLinkDao;
import org.antframework.configcenter.dal.dao.BeTopologyNodeDao;
import org.antframework.configcenter.facade.order.topology.QueryNodesOrder;
import org.antframework.configcenter.facade.result.topology.QueryNodesResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * @author zongzheng
 * @date 2022/10/20 4:14 PM
 * @project configcenter
 */
@Service
@AllArgsConstructor
public class QueryGraphService {

    private final BeTopologyLinkDao beTopologyLinkDao;

    private final BeTopologyNodeDao beTopologyNodeDao;

    private final BeCustomerInfoDao beCustomerInfoDao;

    @ServiceExecute
    public void execute(ServiceContext<QueryNodesOrder, QueryNodesResult> context) {
        QueryNodesOrder order = context.getOrder();
        QueryNodesResult result = context.getResult();
//
//        App app = appDao.findByAppId(order.getAppId());
//        if (app != null) {
//            result.setApp(CONVERTER.convert(app));
//        }
    }

}
