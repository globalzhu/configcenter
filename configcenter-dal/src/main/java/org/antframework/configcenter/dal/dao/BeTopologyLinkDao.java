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
import org.antframework.configcenter.dal.entity.BeTopologyLink;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;

/**
 * 应用dao
 */
@RepositoryDefinition(domainClass = BeTopologyLink.class, idClass = Long.class)
public interface BeTopologyLinkDao {

    @CacheEvict(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0.id")
    void save(BeTopologyLink beTopologyLink);

    @CacheEvict(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0.id")
    void delete(BeTopologyLink beTopologyLink);

    @Cacheable(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0")
    BeTopologyLink findByHostNodeId(String hostNodeId);

    Page<BeTopologyLink> query(Collection<QueryParam> queryParams, Pageable pageable);

    @Cacheable(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0")
    BeTopologyLink findById(Long id);
}
