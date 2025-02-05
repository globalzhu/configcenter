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
import org.antframework.configcenter.dal.entity.BeReport;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeReport.class, idClass = Long.class)
public interface BeReportDao {

    @CacheEvict(cacheNames = CacheConstant.BE_REPORT_CACHE_NAME, key = "#p0.id")
    void save(BeReport beReport);

    @CacheEvict(cacheNames = CacheConstant.BE_REPORT_CACHE_NAME, key = "#p0.id")
    void delete(BeReport beReport);

    @Cacheable(cacheNames = CacheConstant.BE_REPORT_CACHE_NAME, key = "#p0")
    BeReport findById(Long id);

    Page<BeReport> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeReport> findByIdIn(List<Long> id);

}
