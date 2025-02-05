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
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.dal.entity.BeSystem;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeSystem.class, idClass = Long.class)
public interface BeSystemDao {

    @CacheEvict(cacheNames = CacheConstant.BE_SYSTEM_CACHE_NAME, key = "#p0.id")
    void save(BeSystem beSystem);

    @CacheEvict(cacheNames = CacheConstant.BE_SYSTEM_CACHE_NAME, key = "#p0.id")
    void delete(BeSystem beSystem);

    @Cacheable(cacheNames = CacheConstant.BE_SYSTEM_CACHE_NAME, key = "#p0")
    BeSystem findById(Long id);

    Page<BeSystem> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeSystem> findByIdIn(List<Long> id);

    List<BeSystem> findAll();

}
