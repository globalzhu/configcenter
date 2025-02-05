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
import org.antframework.configcenter.dal.entity.BeDeployOrder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeDeployOrder.class, idClass = Long.class)
public interface BeDeployOrderDao {
    @CacheEvict(cacheNames = CacheConstant.BE_DEPLOY_ORDER_CACHE_NAME, key = "#p0.id")
    void save(BeDeployOrder beDeployOrder);

    @CacheEvict(cacheNames = CacheConstant.BE_DEPLOY_ORDER_CACHE_NAME, key = "#p0.id")
    void delete(BeDeployOrder beDeployOrder);

    @Cacheable(cacheNames = CacheConstant.BE_DEPLOY_ORDER_CACHE_NAME, key = "#p0")
    BeDeployOrder findById(Long id);

    Page<BeDeployOrder> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeDeployOrder> findByIdIn(List<Long> id);

}
