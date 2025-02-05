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
import org.antframework.configcenter.dal.entity.BeCustomerInfo;
import org.antframework.configcenter.dal.entity.Branch;
import org.antframework.configcenter.facade.vo.CustomerStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.RepositoryDefinition;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

/**
 * 客户dao
 */
@RepositoryDefinition(domainClass = BeCustomerInfo.class, idClass = Long.class)
public interface BeCustomerInfoDao {

    @CacheEvict(cacheNames = CacheConstant.BE_CUSTOMER_INFO_CACHE_NAME, key = "#p0.customerCode")
    void save(BeCustomerInfo beCustomerInfo);

    @CacheEvict(cacheNames = CacheConstant.BE_CUSTOMER_INFO_CACHE_NAME, key = "#p0.customerCode")
    void delete(BeCustomerInfo beCustomerInfo);

    Page<BeCustomerInfo> query(Collection<QueryParam> queryParams, Pageable pageable);

    List<BeCustomerInfo> findByCustomerStatus(CustomerStatus customerStatus);

    BeCustomerInfo findByCustomerCode(String customerCode);

    BeCustomerInfo findById(Long id);

}
