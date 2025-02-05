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
import org.antframework.configcenter.dal.entity.BeProduct;
import org.antframework.configcenter.dal.entity.BeProductVersion;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeProductVersion.class, idClass = Long.class)
public interface BeProductVersionDao {

    @CacheEvict(cacheNames = CacheConstant.BE_PRODUCT_VERSION_CACHE_NAME, key = "#p0.id")
    void save(BeProductVersion beProductVersion);

    @CacheEvict(cacheNames = CacheConstant.BE_PRODUCT_VERSION_CACHE_NAME, key = "#p0.id")
    void delete(BeProductVersion beProductVersion);

    @Cacheable(cacheNames = CacheConstant.BE_PRODUCT_VERSION_CACHE_NAME, key = "#p0")
    BeProductVersion findById(Long id);

    Page<BeProductVersion> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeProductVersion> findByIdIn(List<Long> id);

}
