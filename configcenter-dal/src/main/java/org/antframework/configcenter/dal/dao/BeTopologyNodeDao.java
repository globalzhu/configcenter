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
import org.antframework.configcenter.dal.entity.BeTopologyNode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeTopologyNode.class, idClass = Long.class)
public interface BeTopologyNodeDao {
    @CacheEvict(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0.nodeName")
    void save(BeTopologyNode beTopologyNode);

    @CacheEvict(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0.nodeName")
    void delete(BeTopologyNode beTopologyNode);

    @Cacheable(cacheNames = CacheConstant.BE_TOPOLOGY_NODE_CACHE_NAME, key = "#p0")
    BeTopologyNode findById(Long id);

    Page<BeTopologyNode> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeTopologyNode> findByIdIn(List<Long> id);

}
