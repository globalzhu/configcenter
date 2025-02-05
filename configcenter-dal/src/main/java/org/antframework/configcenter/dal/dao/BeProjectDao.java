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
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeProject.class, idClass = Long.class)
public interface BeProjectDao {

    @CacheEvict(cacheNames = CacheConstant.BE_PROJECT_CACHE_NAME, key = "#p0.id")
    void save(BeProject beProject);

    @CacheEvict(cacheNames = CacheConstant.BE_PROJECT_CACHE_NAME, key = "#p0.id")
    void delete(BeProject beProject);

    @Cacheable(cacheNames = CacheConstant.BE_PROJECT_CACHE_NAME, key = "#p0")
    BeProject findById(Long id);

    Page<BeProject> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeProject> findByIdIn(List<Long> id);

    List<BeProject> findAllByBdName(String bdName);

    List<BeProject> findAllByApprovePeopleAndProjectStatus(String approvePeople, ProjectStatus projectStatus);

    @Cacheable(cacheNames = CacheConstant.BE_PROJECT_CACHE_NAME, key = "#p0")
    BeProject findByProjectName(String projectName);

}
