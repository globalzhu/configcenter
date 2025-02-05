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
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeProjectWork.class, idClass = Long.class)
public interface BeProjectWorkDao {

    @CacheEvict(cacheNames = CacheConstant.BE_PROJECT_WORK_CACHE_NAME, key = "#p0.id")
    void save(BeProjectWork beProjectWork);

    @CacheEvict(cacheNames = CacheConstant.BE_PROJECT_WORK_CACHE_NAME, key = "#p0.id")
    void delete(BeProjectWork beProjectWork);

    @Cacheable(cacheNames = CacheConstant.BE_PROJECT_WORK_CACHE_NAME, key = "#p0")
    BeProjectWork findById(Long id);

    Page<BeProjectWork> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeProjectWork> findByIdIn(List<Long> id);

    List<BeProjectWork> findAllByProjectId(Long projectId);

}
