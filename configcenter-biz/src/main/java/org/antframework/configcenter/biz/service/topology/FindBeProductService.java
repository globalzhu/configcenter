/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-17 22:45 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.dal.dao.BeProductDao;
import org.antframework.configcenter.dal.entity.BeProduct;
import org.antframework.configcenter.facade.info.topology.BeProductDTO;
import org.antframework.configcenter.facade.order.topology.FindBeProductOrder;
import org.antframework.configcenter.facade.result.topology.FindBeProductResult;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

/**
 * 查找外包服务
 */
@Service
@AllArgsConstructor
public class FindBeProductService {

    // 分支dao
    private final BeProductDao beProductDao;

    @ServiceBefore
    public void before(ServiceContext<FindBeProductOrder, FindBeProductResult> context) {
        FindBeProductOrder order = context.getOrder();

        Long id = order.getId();
        BeProduct beProduct = beProductDao.findById(id);
        if (beProduct == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("产品[%s]不存在", id));
        }

    }

    @ServiceExecute
    public void execute(ServiceContext<FindBeProductOrder, FindBeProductResult> context) {
        FindBeProductResult result = context.getResult();

        BeProduct beProduct = beProductDao.findById(context.getOrder().getId());

        BeProductDTO beProductDTO = new BeProductDTO();

        beProductDTO.setId(beProduct.getId());
        beProductDTO.setParentId(beProduct.getParentId());
        beProductDTO.setProductName(beProduct.getProductName());
        beProductDTO.setProductCode(beProduct.getProductCode());
        beProductDTO.setPdName(beProduct.getPdName());
        beProductDTO.setDevName(beProduct.getDevName());
        beProductDTO.setApprovePeople(beProduct.getApprovePeople());
        beProductDTO.setQuotationScope(beProduct.getQuotationScope());
        beProductDTO.setMaturityDegree(beProduct.getMaturityDegree());
        beProductDTO.setProductDevStatus(beProduct.getProductDevStatus());
        beProductDTO.setRelSystemIds(beProduct.getRelSystemIds());
        beProductDTO.setProductClass(beProduct.getProductClass());

        result.setBeProductDTO(beProductDTO);
    }
}
