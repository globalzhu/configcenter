/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-13 15:31 创建
 */
package org.antframework.configcenter.facade.result;

import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.PropertyValueInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 比较branch的差异结果
 */
@Getter
@Setter
public class DiffBranchesResult extends AbstractResult {

    private List<PropertyValueInfo> basePropertyValues = Lists.newArrayList();

    private List<PropertyValueInfo> diffPropertyValues = Lists.newArrayList();

    // 新增的键值对
    private Map<String, String> addPropertyValues = Maps.newHashMap();

    // 找出更新的键值对
    private Map<String, MapDifference.ValueDifference<String>> modifiedPropertyValues = Maps.newHashMap();

    // 找出删除的键值对
    private Map<String, String> deletedPropertyValues = Maps.newHashMap();

}
