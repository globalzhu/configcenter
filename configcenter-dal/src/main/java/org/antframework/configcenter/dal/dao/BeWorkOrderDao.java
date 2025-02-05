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
import org.antframework.configcenter.dal.entity.BeWorkOrder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@RepositoryDefinition(domainClass = BeWorkOrder.class, idClass = Long.class)
public interface BeWorkOrderDao {

    @CacheEvict(cacheNames = CacheConstant.BE_WORK_ORDER_CACHE_NAME, key = "#p0.id")
    void save(BeWorkOrder beWorkOrder);

    @CacheEvict(cacheNames = CacheConstant.BE_WORK_ORDER_CACHE_NAME, key = "#p0.id")
    void delete(BeWorkOrder beWorkOrder);

    @Cacheable(cacheNames = CacheConstant.BE_WORK_ORDER_CACHE_NAME, key = "#p0")
    BeWorkOrder findById(Long id);

    List<BeWorkOrder> findAllByWorkDateAndEmployeeId(Date workDate,Long employeeId);

    Page<BeWorkOrder> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeWorkOrder> findByIdIn(List<Long> id);

}
