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
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = BeEmployee.class, idClass = Long.class)
public interface BeEmployeeDao {

    @CacheEvict(cacheNames = CacheConstant.BE_EMPLOYEE_CACHE_NAME, key = "#p0.id")
    void save(BeEmployee beEmployee);

    @CacheEvict(cacheNames = CacheConstant.BE_EMPLOYEE_CACHE_NAME, key = "#p0.id")
    void delete(BeEmployee beEmployee);

    @Cacheable(cacheNames = CacheConstant.BE_EMPLOYEE_CACHE_NAME, key = "#p0")
    BeEmployee findById(Long id);

    @Cacheable(cacheNames = CacheConstant.BE_EMPLOYEE_CACHE_NAME, key = "#p0")
    BeEmployee findByWorkLoginName(String workLoginName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<BeEmployee> findAllBySuperiorId(Long superiorId);

    Page<BeEmployee> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeEmployee> findByIdIn(List<Long> id);

    @Cacheable(cacheNames = CacheConstant.BE_EMPLOYEE_CACHE_NAME, key = "#p0")
    BeEmployee findByPenName(String penName);

}
