package org.antframework.configcenter.biz.provider.topology;

import lombok.AllArgsConstructor;
import org.antframework.boot.bekit.CommonQueries;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.biz.util.QueryUtils;
import org.antframework.configcenter.dal.dao.*;
import org.antframework.configcenter.facade.api.topology.TopologyService;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.order.topology.*;
import org.antframework.configcenter.facade.result.topology.*;
import org.bekit.service.ServiceEngine;
import org.springframework.stereotype.Service;

/**
 * @author zongzheng
 * @date 2022/10/20 7:19 PM
 * @project configcenter
 */
@Service
@AllArgsConstructor
public class TopologyServiceProvider implements TopologyService {

    // 服务引擎
    private final ServiceEngine serviceEngine;

    @Override
    public QueryCustomerResult queryCustomerList(QueryCustomerOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeCustomerInfoDao.class));
        return result.convertTo(QueryCustomerResult.class);
    }

    @Override
    public QueryNodesResult queryNodeList(QueryNodesOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeTopologyNodeDao.class));
        return result.convertTo(QueryNodesResult.class);
    }

    @Override
    public QueryLinksResult queryLinkList(QueryLinksOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeTopologyLinkDao.class));
        return result.convertTo(QueryLinksResult.class);
    }

    @Override
    public EmptyResult addOrModifyCustomerInfo(AddOrModifyCustomerOrder order) {
        return serviceEngine.execute("addOrModifyCustomerService", order);
    }

    @Override
    public EmptyResult addOrModifyNode(AddOrModifyNodeOrder order) {
        return serviceEngine.execute("addOrModifyNodeService", order);
    }

    @Override
    public EmptyResult addOrModifyLink(AddOrModifyLinkOrder order) {
        return serviceEngine.execute("addOrModifyLinkService", order);
    }

    @Override
    public FindCustomerInfoResult findCustomerInfo(FindCustomerInfoOrder order) {
        return serviceEngine.execute("findCustomerInfoService", order);
    }

    @Override
    public FindNodesResult findNodes(FindNodesOrder order) {
        return serviceEngine.execute("findNodesService", order);
    }

    @Override
    public QueryOrderResult queryOrderList(QueryOrdersOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeDeployOrderDao.class));
        return result.convertTo(QueryOrderResult.class);
    }

    @Override
    public EmptyResult addOrModifyOrder(AddOrModifyOrderOrder order) {
        return serviceEngine.execute("addOrModifyOrderService", order);
    }

    @Override
    public FindDeployOrderResult findDeployOrderService(FindDeployOrderOrder order) {
        return serviceEngine.execute("findDeployOrderService", order);
    }

    @Override
    public EmptyResult addOrModifyPreSaleOrder(AddOrModifyPreSaleOrderOrder order) {
        return serviceEngine.execute("addOrModifyPreSaleOrderService", order);
    }

    @Override
    public EmptyResult addOrModifyOutsource(AddOrModifyBeOutsourceOrder order) {
        return serviceEngine.execute("addOrModifyOutsourceService", order);
    }

    @Override
    public EmptyResult addOrModifyBeEmployee(AddOrModifyBeEmployeeOrder order) {
        return serviceEngine.execute("addOrModifyBeEmployeeService", order);
    }

    @Override
    public EmptyResult addOrModifyBeProduct(AddOrModifyBeProductOrder order) {
        return serviceEngine.execute("addOrModifyBeProductService", order);
    }

    @Override
    public EmptyResult addOrModifyBeProjectWork(AddOrModifyBeProjectWorkOrder order) {
        return serviceEngine.execute("addOrModifyBeProjectWorkService", order);
    }

    @Override
    public EmptyResult addOrModifyBeProductVersion(AddOrModifyBeProductVersionOrder order) {
        return serviceEngine.execute("addOrModifyBeProductVersionService", order);
    }

    @Override
    public EmptyResult addOrModifyBeProject(AddOrModifyBeProjectOrder order) {
        return serviceEngine.execute("addOrModifyBeProjectService", order);
    }

    @Override
    public WeekFinishResult isFinishedThisWeek(WeekFinishWorkOrderOrder order) {
        return serviceEngine.execute("isFinishedThisWeekService", order);
    }

    @Override
    public EmptyResult addOrModifyOutsourceProjectRecord(AddOrModifyOutsourceProjectRecordOrder order) {
        return serviceEngine.execute("addOrModifyOutsourceProjectRecordService", order);
    }

    @Override
    public QueryPreSaleOrderResult queryPreSaleOrderList(QueryPreSaleOrdersOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BePreSaleOrderDao.class));
        return result.convertTo(QueryPreSaleOrderResult.class);
    }

    @Override
    public QueryOutsourceProjectRecordResult queryOutsourceProjectRecordList(QueryOutsourceProjectRecordsOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeOutsourceProjectRecordDao.class));
        return result.convertTo(QueryOutsourceProjectRecordResult.class);
    }

    @Override
    public QueryOutsourceResult queryOutsourceList(QueryOutsourcesOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeOutsourceDao.class));
        return result.convertTo(QueryOutsourceResult.class);
    }

    @Override
    public QueryReportResult queryReportList(QueryReportsOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeReportDao.class));
        return result.convertTo(QueryReportResult.class);
    }

    @Override
    public QueryBeWorkOrderResult queryBeWorkOrderList(QueryBeWorkOrderOrderWrapper order) {
        return serviceEngine.execute("queryBeWorkOrderListService", order);

//        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeWorkOrderDao.class));
//        return result.convertTo(QueryBeWorkOrderResult.class);
    }

    @Override
    public QueryBeWorkOrderResult queryBeWorkOrderListByLoginName(QueryBeWorkOrderOrderWrapper order) {
        return serviceEngine.execute("queryBeWorkOrderListByLoginNameService", order);

//        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeWorkOrderDao.class));
//        return result.convertTo(QueryBeWorkOrderResult.class);
    }

    @Override
    public EmptyResult deleteBeWorkOrder(DeleteBeWorkOrderOrder order) {
        return serviceEngine.execute("deleteBeWorkOrderService", order);
    }

    @Override
    public EmptyResult deleteBeProjectAccess(DeleteBeProjectAccessOrder order) {
        return serviceEngine.execute("deleteBeProjectAccessService", order);
    }

    @Override
    public EmptyResult deleteBeEmployee(DeleteBeEmployeeOrder order) {
        return serviceEngine.execute("deleteBeEmployeeService", order);
    }

    @Override
    public EmptyResult deleteBeProjectWork(DeleteBeProjectWorkOrder order) {
        return serviceEngine.execute("deleteBeProjectWorkService", order);
    }

    @Override
    public EmptyResult deleteBeProduct(DeleteBeProductOrder order) {
        return serviceEngine.execute("deleteBeProductService", order);
    }

    @Override
    public EmptyResult deleteBeProductVersion(DeleteBeProductVersionOrder order) {
        return serviceEngine.execute("deleteBeProductVersionService", order);
    }

    @Override
    public EmptyResult deleteBeProject(DeleteBeProjectOrder order) {
        return serviceEngine.execute("deleteBeProjectService", order);
    }

    @Override
    public QueryBeProjectResult queryBeProjectList(QueryBeProjectOrderWrapper order) {
        return serviceEngine.execute("queryBeProjectListService", order);
//        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeProjectDao.class));
//        return result.convertTo(QueryBeProjectResult.class);
    }

    @Override
    public QueryBeProjectWorkResult queryBeProjectWorkList(QueryBeProjectWorkOrder order) {
        return serviceEngine.execute("queryBeProjectWorkListService", order);
//        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeProjectDao.class));
//        return result.convertTo(QueryBeProjectResult.class);
    }

    @Override
    public QueryBeSystemResult queryBeSystemList(QueryBeSystemOrder order) {
        return serviceEngine.execute("queryBeSystemListService", order);
    }

    @Override
    public QueryBeProductResult queryBeProductList(QueryBeProductOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeProductDao.class));
        return result.convertTo(QueryBeProductResult.class);
    }

    @Override
    public QueryBeProjectAccessResult queryBeProjectAccessListByLoginName(QueryBeProjectAccessOrderWrapper order) {
        return serviceEngine.execute("queryBeProjectAccessListService", order);
//        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeProjectAccessDao.class));
//        return result.convertTo(QueryBeProjectAccessResult.class);
    }

    @Override
    public QueryBeProductVersionResult queryBeProductVersionList(QueryBeProductVersionOrder order) {
        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeProductVersionDao.class));
        return result.convertTo(QueryBeProductVersionResult.class);
    }

    @Override
    public QueryBeEmployeeResult queryBeEmployeeList(QueryBeEmployeeOrder order) {
        return serviceEngine.execute("queryBeEmployeeListService", order);
//        CommonQueries.CommonQueryResult result = serviceEngine.execute(CommonQueries.SERVICE_NAME, order, QueryUtils.buildCommonQueryAttachmentByUpdateTime(BeEmployeeDao.class));
//        return result.convertTo(QueryBeEmployeeResult.class);
    }

    @Override
    public FindPreSaleOrderResult findPreSaleOrder(FindPreSaleOrderOrder order) {
        return serviceEngine.execute("findPreSaleOrderService", order);
    }

    @Override
    public FindOutsourceResult findOutsource(FindOutsourceOrder order) {
        return serviceEngine.execute("findOutsourceService", order);
    }

    @Override
    public FindBeEmployeeResult findBeEmployee(FindBeEmployeeOrder order) {
        return serviceEngine.execute("findBeEmployeeService", order);
    }

    @Override
    public FindBeProductResult findBeProduct(FindBeProductOrder order) {
        return serviceEngine.execute("findBeProductService", order);
    }

    @Override
    public FindBeProjectWorkResult findBeProjectWork(FindBeProjectWorkOrder order) {
        return serviceEngine.execute("findBeProjectWorkService", order);
    }

    @Override
    public FindBeProductVersionResult findBeProductVersion(FindBeProductVersionOrder order) {
        return serviceEngine.execute("findBeProductVersionService", order);
    }

    @Override
    public FindBeProjectResult findBeProject(FindBeProjectOrder order) {
        return serviceEngine.execute("findBeProjectService", order);
    }

    @Override
    public FindOutsourceProjectRecordResult findOutsourceProjectRecord(FindOutsourceProjectRecordOrder order) {
        return serviceEngine.execute("findOutsourceProjectRecordService", order);
    }

    @Override
    public FindBeWorkOrderResult findBeWorkOrder(FindBeWorkOrderOrder order) {
        return serviceEngine.execute("findBeWorkOrderService", order);
    }

    @Override
    public FindBeProjectAccessResult findBeProjectAccess(FindBeProjectAccessOrder order) {
        return serviceEngine.execute("findBeProjectAccessService", order);
    }

    @Override
    public EmptyResult addOrModifyBeWorkOrder(AddOrModifyBeWorkOrderOrder order) {
        return serviceEngine.execute("addOrModifyBeWorkOrderService", order);
    }

    @Override
    public EmptyResult batchApproveOrRejectBeWorkOrder(BatchUpdateBeWorkOrderOrder order) {
        return serviceEngine.execute("batchApproveOrRejectBeWorkOrderService", order);
    }

    @Override
    public EmptyResult addOrModifyBeProjectAccess(AddOrModifyBeProjectAccessOrder order) {
        return serviceEngine.execute("addOrModifyBeProjectAccessService", order);
    }
}
