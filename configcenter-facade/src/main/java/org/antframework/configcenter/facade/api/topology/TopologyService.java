package org.antframework.configcenter.facade.api.topology;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.order.topology.*;
import org.antframework.configcenter.facade.result.topology.*;

/**
 * @author zongzheng
 * @date 2022/10/20 7:19 PM
 * @project configcenter
 */
public interface TopologyService {

    QueryCustomerResult queryCustomerList(QueryCustomerOrder order);

    QueryNodesResult queryNodeList(QueryNodesOrder order);

    QueryLinksResult queryLinkList(QueryLinksOrder order);

    EmptyResult addOrModifyCustomerInfo(AddOrModifyCustomerOrder order);

    EmptyResult addOrModifyNode(AddOrModifyNodeOrder order);

    EmptyResult addOrModifyLink(AddOrModifyLinkOrder order);

    FindCustomerInfoResult findCustomerInfo(FindCustomerInfoOrder order);

    FindNodesResult findNodes(FindNodesOrder order) ;

    QueryOrderResult queryOrderList(QueryOrdersOrder order);

    QueryPreSaleOrderResult queryPreSaleOrderList(QueryPreSaleOrdersOrder order);

    QueryOutsourceProjectRecordResult queryOutsourceProjectRecordList(QueryOutsourceProjectRecordsOrder order);

    QueryOutsourceResult queryOutsourceList(QueryOutsourcesOrder order);

    QueryReportResult queryReportList(QueryReportsOrder order);

    QueryBeWorkOrderResult queryBeWorkOrderList(QueryBeWorkOrderOrderWrapper order);

    QueryBeWorkOrderResult queryBeWorkOrderListByLoginName(QueryBeWorkOrderOrderWrapper order);

    EmptyResult deleteBeWorkOrder(DeleteBeWorkOrderOrder order);

    EmptyResult deleteBeProjectAccess(DeleteBeProjectAccessOrder order);

    EmptyResult deleteBeEmployee(DeleteBeEmployeeOrder order);

    EmptyResult deleteBeProjectWork(DeleteBeProjectWorkOrder order);

    EmptyResult deleteBeProduct(DeleteBeProductOrder order);

    EmptyResult deleteBeProductVersion(DeleteBeProductVersionOrder order);

    EmptyResult deleteBeProject(DeleteBeProjectOrder order);

    QueryBeProjectResult queryBeProjectList(QueryBeProjectOrderWrapper order);

    QueryBeProjectWorkResult queryBeProjectWorkList(QueryBeProjectWorkOrder order);

    QueryBeSystemResult queryBeSystemList(QueryBeSystemOrder order);

    QueryBeProductResult queryBeProductList(QueryBeProductOrder order);

    QueryBeProjectAccessResult queryBeProjectAccessListByLoginName(QueryBeProjectAccessOrderWrapper order);

    QueryBeProductVersionResult queryBeProductVersionList(QueryBeProductVersionOrder order);

    QueryBeEmployeeResult queryBeEmployeeList(QueryBeEmployeeOrder order);

    EmptyResult addOrModifyOrder(AddOrModifyOrderOrder order);

    FindDeployOrderResult findDeployOrderService(FindDeployOrderOrder order);

    EmptyResult addOrModifyPreSaleOrder(AddOrModifyPreSaleOrderOrder order);

    EmptyResult addOrModifyOutsource(AddOrModifyBeOutsourceOrder order);

    EmptyResult addOrModifyBeEmployee(AddOrModifyBeEmployeeOrder order);

    EmptyResult addOrModifyBeProduct(AddOrModifyBeProductOrder order);

    EmptyResult addOrModifyBeProjectWork(AddOrModifyBeProjectWorkOrder order);

    EmptyResult addOrModifyBeProductVersion(AddOrModifyBeProductVersionOrder order);

    EmptyResult addOrModifyBeProject(AddOrModifyBeProjectOrder order);

    WeekFinishResult isFinishedThisWeek(WeekFinishWorkOrderOrder order);

    EmptyResult addOrModifyOutsourceProjectRecord(AddOrModifyOutsourceProjectRecordOrder order);

    FindPreSaleOrderResult findPreSaleOrder(FindPreSaleOrderOrder order);

    FindOutsourceResult findOutsource(FindOutsourceOrder order);

    FindBeEmployeeResult findBeEmployee(FindBeEmployeeOrder order);

    FindBeProductResult findBeProduct(FindBeProductOrder order);

    FindBeProjectWorkResult findBeProjectWork(FindBeProjectWorkOrder order);

    FindBeProductVersionResult findBeProductVersion(FindBeProductVersionOrder order);

    FindBeProjectResult findBeProject(FindBeProjectOrder order);

    FindOutsourceProjectRecordResult findOutsourceProjectRecord(FindOutsourceProjectRecordOrder order);

    FindBeWorkOrderResult findBeWorkOrder(FindBeWorkOrderOrder order);

    FindBeProjectAccessResult findBeProjectAccess(FindBeProjectAccessOrder order);

    EmptyResult addOrModifyBeWorkOrder(AddOrModifyBeWorkOrderOrder order);

    EmptyResult batchApproveOrRejectBeWorkOrder(BatchUpdateBeWorkOrderOrder order);

    EmptyResult addOrModifyBeProjectAccess(AddOrModifyBeProjectAccessOrder order);

}
