package org.antframework.configcenter.web.controller.manage;

import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.api.PropertyValueService;
import org.antframework.configcenter.facade.info.PropertyValueInfo;
import org.antframework.configcenter.facade.order.FindBranchOrder;
import org.antframework.configcenter.facade.order.FindBranchesOrder;
import org.antframework.configcenter.facade.order.FindPropertyValuesOrder;
import org.antframework.configcenter.facade.result.DiffBranchesResult;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.FindBranchesResult;
import org.antframework.configcenter.facade.result.FindPropertyValuesResult;
import org.antframework.configcenter.facade.vo.Scope;
import org.antframework.configcenter.web.common.AppPropertyTypes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zongzheng
 * @date 2022/6/29 4:09 PM
 * @project configcenter
 */
@RestController
@RequestMapping("/manage/devops")
@AllArgsConstructor
public class BeDevOpsController {

    private final PropertyValueService propertyValueService;

    private final BranchService branchService;

    @RequestMapping("/fetchAppConfig")
    public FindPropertyValuesResult fetchAppConfig(String appId, String version,String env) {
        FindPropertyValuesOrder order = new FindPropertyValuesOrder();
        order.setAppId(appId);
        order.setProfileId(env);
        order.setBranchId(version);
        order.setMinScope(Scope.PRIVATE);

        FindPropertyValuesResult result = propertyValueService.findPropertyValues(order);
        if (result.isSuccess()) {
            List<PropertyValueInfo> maskedPropertyValues = AppPropertyTypes.maskPropertyValues(appId, result.getPropertyValues());
            result.getPropertyValues().clear();

            for(PropertyValueInfo info : maskedPropertyValues){
                info.setGroup(appToGroupMapping.get(info.getAppId()));
            }

            result.getPropertyValues().addAll(maskedPropertyValues);
        }
        return result;
    }

    @RequestMapping("/fetchAppVersions")
    public FindBranchesResult fetchAppVersions(String appId, String env,String branchId) {
        FindBranchesOrder order = new FindBranchesOrder();
        order.setAppId(appId);
        order.setProfileId(env == null?"dev":env);

        FindBranchesResult result = branchService.findBranches(order);

        // 支持根据version进行过滤
        if(StringUtils.isEmpty(branchId) || !result.isSuccess()){
            return result;
        }else{
            FindBranchesResult filterResult = new FindBranchesResult();
            filterResult.setCode(result.getCode());
            filterResult.setMessage(result.getMessage());
            filterResult.setStatus(result.getStatus());

            result.getBranches().stream().forEach(branch -> {
                if(branch != null && branch.getBranchId() != null && branch.getBranchId().equals(branchId)){
                    filterResult.addBranch(branch);
                }
            });

            return filterResult;
        }
    }

    @RequestMapping("/diffVersion")
    public DiffBranchesResult diffVersion(String appId, String baseVersion, String diffVersion) {
        DiffBranchesResult result = new DiffBranchesResult();
        if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(baseVersion) || StringUtils.isEmpty(diffVersion)){
            result.setStatus(Status.FAIL);
            result.setMessage("入参非法");
            return result;
        }

        // 查询基线版本
        FindPropertyValuesOrder order = new FindPropertyValuesOrder();
        order.setAppId(appId.trim());
        order.setProfileId("dev");
        order.setBranchId(baseVersion.trim());
        order.setMinScope(Scope.PRIVATE);

        FindPropertyValuesResult baseResult = propertyValueService.findPropertyValues(order);
        List<PropertyValueInfo> basePropertyValues = null;
        if (baseResult.isSuccess()) {
            basePropertyValues = AppPropertyTypes.maskPropertyValues(appId, baseResult.getPropertyValues());

            for(PropertyValueInfo info : basePropertyValues){
                info.setGroup(appToGroupMapping.get(info.getAppId()));
            }

            result.setBasePropertyValues(basePropertyValues);
        } else {
            result.setStatus(baseResult.getStatus());
            result.setMessage(baseResult.getMessage());
            return result;
        }

        // 查询对比版本
        order.setBranchId(diffVersion.trim());
        FindPropertyValuesResult diffResult = propertyValueService.findPropertyValues(order);
        List<PropertyValueInfo> diffPropertyValues = null;
        if (diffResult.isSuccess()) {
            diffPropertyValues = AppPropertyTypes.maskPropertyValues(appId, diffResult.getPropertyValues());

            for(PropertyValueInfo info : diffPropertyValues){
                info.setGroup(appToGroupMapping.get(info.getAppId()));
            }

            result.setDiffPropertyValues(diffPropertyValues);
        } else {
            result.setStatus(diffResult.getStatus());
            result.setMessage(diffResult.getMessage());
            return result;
        }

        Map<String,String> basePropertyValuesMap = basePropertyValues.stream().collect(
                Collectors.toMap(PropertyValueInfo::getKey,each->each.getValue(),(value1,value2) -> value1));
        Map<String,String> diffPropertyValuesMap = diffPropertyValues.stream().collect(
                Collectors.toMap(PropertyValueInfo::getKey,each->each.getValue(),(value1,value2) -> value1));

        MapDifference<String, String> difference = Maps.difference(diffPropertyValuesMap, basePropertyValuesMap);

        // 找出新增的键值对
        Map<String, String> addPropertyValues = difference.entriesOnlyOnLeft();

        // 找出更新的键值对
        Map<String, MapDifference.ValueDifference<String>> modifiedPropertyValues = difference.entriesDiffering();
//        for (Map.Entry<String, MapDifference.ValueDifference<String>> entry : modifiedPropertyValues.entrySet()) {
//            System.out.println("更新的键值对：" + entry.getKey() + "=" + entry.getValue().leftValue());
//        }

        // 找出删除的键值对
        Map<String, String> deletedPropertyValues = difference.entriesOnlyOnRight();

        result.setAddPropertyValues(addPropertyValues);
        result.setDeletedPropertyValues(deletedPropertyValues);
        result.setModifiedPropertyValues(modifiedPropertyValues);
        result.setStatus(Status.SUCCESS);
        return result;
    }


    /**
     * 因为系统信息不会经常变化，这里做hardcode
     */
    private static Map<String,String> appToGroupMapping = new HashMap<String,String>();
    static{
        appToGroupMapping.put("Pi","Alphabet");
        appToGroupMapping.put("pi","Alphabet");
        appToGroupMapping.put("edge-asset","EDGE");
        appToGroupMapping.put("edge-model","EDGE");
        appToGroupMapping.put("gaia-datax","middleware");
        appToGroupMapping.put("gaia-mesh","middleware");
        appToGroupMapping.put("edge-serving","EDGE");
        appToGroupMapping.put("gaia-pandora-dataseed","middleware");
        appToGroupMapping.put("gaia-pandora","middleware");
        appToGroupMapping.put("cube_engine","cube");
        appToGroupMapping.put("omega","Alphabet");
        appToGroupMapping.put("Omega","Alphabet");
        appToGroupMapping.put("datachange","Alphabet");
        appToGroupMapping.put("gaia-way","middleware");
        appToGroupMapping.put("gaia-janus","middleware");
        appToGroupMapping.put("configcenter","pub");
        appToGroupMapping.put("lambda","Alphabet");
        appToGroupMapping.put("Lambda","Alphabet");
        appToGroupMapping.put("gaia-kms","middleware");
        appToGroupMapping.put("omega-dataseed","Alphabet");
        appToGroupMapping.put("edge-dataseed","EDGE");
        appToGroupMapping.put("engine_websocket","cube");
        appToGroupMapping.put("gaia-runtime-image","Common");
        appToGroupMapping.put("cube_data_support","cube");
        appToGroupMapping.put("engine_grpc","cube");
        appToGroupMapping.put("quartz-dataseed","EDGE");
        appToGroupMapping.put("idata_server","cube");
        appToGroupMapping.put("cube_eng_build","cube");
        appToGroupMapping.put("App Common","Common");
        appToGroupMapping.put("gaia-ark","middleware");
        appToGroupMapping.put("pi-dataseed","Alphabet");
        appToGroupMapping.put("lambda-dataseed","Alphabet");
        appToGroupMapping.put("micro-component","cube-micro");
        appToGroupMapping.put("model-center","cube");
        appToGroupMapping.put("edgeX-admin","Alphabet");
        appToGroupMapping.put("datachange","EDGE");
        appToGroupMapping.put("Fed-Graph","gaia-graph");
        appToGroupMapping.put("edge-studio-ui","EDGE");
    }

}
