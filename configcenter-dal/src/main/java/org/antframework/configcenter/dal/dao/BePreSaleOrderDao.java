/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:14 创建
 */
package org.antframework.configcenter.dal.dao;

import org.antframework.common.util.query.QueryParam;
import org.antframework.configcenter.common.constant.CacheConstant;
import org.antframework.configcenter.dal.entity.BePreSaleOrder;
import org.antframework.configcenter.dal.entity.Profile;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BePreSaleOrder.class, idClass = Long.class)
public interface BePreSaleOrderDao {

    @CacheEvict(cacheNames = CacheConstant.BE_PRE_SALE_ORDER_CACHE_NAME, key = "#p0.id")
    void save(BePreSaleOrder bePreSaleOrder);

    @CacheEvict(cacheNames = CacheConstant.BE_PRE_SALE_ORDER_CACHE_NAME, key = "#p0.id")
    void delete(BePreSaleOrder bePreSaleOrder);

    @Cacheable(cacheNames = CacheConstant.BE_PRE_SALE_ORDER_CACHE_NAME, key = "#p0")
    BePreSaleOrder findById(Long id);

    Page<BePreSaleOrder> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BePreSaleOrder> findByIdIn(List<Long> id);

}
