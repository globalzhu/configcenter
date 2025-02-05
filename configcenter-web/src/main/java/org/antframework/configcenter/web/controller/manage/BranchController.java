/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-09-14 19:18 创建
 */
package org.antframework.configcenter.web.controller.manage;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.*;
import org.antframework.configcenter.biz.util.*;
import org.antframework.configcenter.facade.api.BranchService;
import org.antframework.configcenter.facade.info.BranchInfo;
import org.antframework.configcenter.facade.info.PropertyChange;
import org.antframework.configcenter.facade.info.PropertyDifference;
import org.antframework.configcenter.facade.info.ReleaseInfo;
import org.antframework.configcenter.facade.order.*;
import org.antframework.configcenter.facade.result.FindBranchResult;
import org.antframework.configcenter.facade.result.FindBranchesResult;
import org.antframework.configcenter.facade.result.MergeBranchResult;
import org.antframework.configcenter.facade.vo.BranchConstants;
import org.antframework.configcenter.facade.vo.Property;
import org.antframework.configcenter.web.common.AppPropertyTypes;
import org.antframework.configcenter.web.common.ManagerApps;
import org.antframework.manager.facade.enums.ManagerType;
import org.antframework.manager.facade.info.ManagerInfo;
import org.antframework.manager.web.CurrentManagerAssert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 分支controller
 */
@RestController
@RequestMapping("/manage/branch")
@AllArgsConstructor
public class BranchController {
    // 分支服务
    private final BranchService branchService;

//    public static void main(String[] args) {
//        String aaa = "GAIA-1.5.4.8-主线/XX客户定制功能";
//        String bbb = "GAIA-1.5.";
//        String ccc = "GAIA-1.5.4.8.1-主线/XX客户定制功能";
//        String ddd = "GAIA-1.5.4.8.102-主线/XX客户定制功能";
//
//
//        EmptyResult result1 = isGAIABranchIdValid("gaia-release",aaa);System.out.println(result1.isSuccess());
//        EmptyResult result2 = isGAIABranchIdValid("gaia-release",bbb);System.out.println(result2.isSuccess());
//        EmptyResult result3 = isGAIABranchIdValid("gaia-release",ccc);System.out.println(result3.isSuccess());
//        EmptyResult result4 = isGAIABranchIdValid("gaia-release",ddd);System.out.println(result4.isSuccess());
//    }

    /**
     * GAIA版本号是否合法判断
     *
     * GAIA版本号规则：GAIA-版本号-备注说明
     *
     * 校验规则：
     * 1、其中必须以"GAIA-"开头
     * 2、版本号如果是五位，则为bugfix版本，第五位必须从101开始
     *
     * @param branchId
     * @return
     */
    private EmptyResult isGAIABranchIdValid(String appId,String branchId){
        EmptyResult result  = new EmptyResult();
        result.setStatus(Status.SUCCESS);

        if(appId != null && appId.equals("gaia-release")){
            String GAIA_ERROR_MSG = "校验失败，GAIA版本号规则：1、其中必须以\"GAIA-\"开头；2、版本号如果是五位，则为bugfix版本，第五位必须从101开始，例如1.5.4.1.101";

            if(!branchId.startsWith("GAIA-")){
                result.setStatus(Status.FAIL);
                result.setMessage(GAIA_ERROR_MSG);
                return result;
            }

            try{
                String[] branchSplitArray = branchId.split("-");
                if(branchSplitArray.length != 3){
                    result.setStatus(Status.FAIL);
                    result.setMessage(GAIA_ERROR_MSG);
                    return result;
                }

                String version = branchSplitArray[1];
                String[] subVersionArray = version.split("\\.");
                if(subVersionArray.length == 5){
                    String fifthSubVersion = subVersionArray[4];
                    if(Long.parseLong(fifthSubVersion) < 101){
                        result.setStatus(Status.FAIL);
                        result.setMessage(GAIA_ERROR_MSG);
                        return result;
                    }
                }
            }catch(Throwable t){
                result.setStatus(Status.FAIL);
                result.setMessage(GAIA_ERROR_MSG);
                return result;
            }
        }

        return result;
    }

    /**
     * 添加分支
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param releaseVersion 发布版本
     */
    @RequestMapping("/addBranch")
    public EmptyResult addBranch(String appId,
                                 String profileId,
                                 String branchId,
                                 Long releaseVersion) {
        ManagerApps.assertAdminOrHaveApp(appId);
        AddBranchOrder order = new AddBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setReleaseVersion(releaseVersion);

        // branchId这里加个判断：如果要新增第五位版本，则必须要以101开始
        EmptyResult isValidResult = isGAIABranchIdValid(appId,branchId);
        if(!isValidResult.isSuccess()) return isValidResult;

        EmptyResult result = branchService.addBranch(order);
        if (result.isSuccess()) {
            PropertyValues.revertPropertyValues(appId, profileId, branchId, releaseVersion);
        }
        return result;
    }

    /**
     * 发布分支
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param propertyChange 配置变动
     * @param memo           备注
     */
    @RequestMapping("/releaseBranch")
    public ReleaseBranchResult releaseBranch(String appId,
                                             String profileId,
                                             String branchId,
                                             @RequestParam PropertyChange propertyChange,
                                             String memo) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            Set<String> keys = propertyChange.getAddedOrModifiedProperties().stream().map(Property::getKey).collect(Collectors.toSet());
            keys.addAll(propertyChange.getDeletedKeys());
            AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, keys);
        }

        // memo 不能为空，且必须5个字符以上
        if(memo == null || memo.length() < 5){
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), CommonResultCode.INVALID_PARAMETER.getMessage()+" memo 不能为空，且必须5个字符以上");
        }

        List<String> imageList = Lists.newArrayList();

        if(branchId.contains("1.5.1")
                || branchId.contains("1.5.2")
                || branchId.contains("1.5.3")
                || branchId.contains("1.5.4")){
            // 特殊逻辑，对gaia 1.5.5之前的版本不做判断
        }else{

            // 对value是否规范做校验
            if(propertyChange != null){
                AtomicBoolean isPropertyValueValid = new AtomicBoolean(true);
                StringBuffer errorMessage = new StringBuffer("规范请参考https://blue-elephant.yuque.com/blue-elephant/toko36/afshng改造，不规范明细：");

                propertyChange.getAddedOrModifiedProperties().stream().forEach(property -> {
                    String key = property.getKey();
                    String value = property.getValue();
                    imageList.add(value);

                    if(key.contains("dataseed")
                            || key.contains("datachange")
                            || key.contains("ido")
                            || key.contains("k8s")
                            || key.contains("mysql")
                            || key.contains("redis")
                            || value.contains("k8s")
                            || value.contains("datachange")
                            || value.contains("dataseed")
                            || value.contains("ido")
                            || value.contains("mysql")
                            || value.contains("redis")
                            || "sftp".equals(key)
                            || "tikv".equals(key)
                            || "dsdc".equals(key)){
                        // 不做判断
                    }else{
                        String pattern = "(([1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\:5000/\\w+\\S+:(\\d+\\.(\\d+\\.)*\\d+)-(beta|stable|alpha)-[A-Za-z0-9]{7}";
                        if(!Pattern.matches(pattern, value)){
                            errorMessage.append(value).append(";");
                            isPropertyValueValid.set(false);
                        }
                    }
                });

                if(!isPropertyValueValid.get()){
                    throw new BizException(
                            Status.FAIL,
                            CommonResultCode.INVALID_PARAMETER.getCode(),
                            CommonResultCode.INVALID_PARAMETER.getMessage()+"，镜像地址不规范，"+errorMessage.toString());
                }

            }

        }

        ManagerInfo manager = CurrentManagerAssert.current();

        ReleaseBranchOrder order = new ReleaseBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setPropertyChange(propertyChange);
        order.setMemo(manager.getName()+"_"+memo);

        ReleaseBranchResult result = branchService.releaseBranch(order);
        if (result.isSuccess()) {
            // 同步到配置value
            PropertyValues.changePropertyValues(
                    appId,
                    profileId,
                    branchId,
                    propertyChange);
            // 对敏感配置掩码
            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                maskRelease(result.getBranch().getRelease());
            }

            // 如果是gaia-release，发送钉钉通知给豆腐和飘絮
            if(appId != null){
                StringBuffer noticeMessage = new StringBuffer();
                noticeMessage.append(String.format("%s 镜像[",appId)).append(branchId).append("]由").append(manager.getName()).append("操作变更，变更详情：");
                if(propertyChange != null){
                    StringBuffer changeDetail = new StringBuffer();
                    if(propertyChange.getAddedOrModifiedProperties().size() > 0){
                        changeDetail.append("修改或新增镜像：");
                        propertyChange.getAddedOrModifiedProperties().stream().forEach(property -> {
                            changeDetail.append(property.getKey()).append(" ");
                        });
                    }
                    if(propertyChange.getDeletedKeys().size() > 0){
                        changeDetail.append("删除镜像：");
                        propertyChange.getDeletedKeys().stream().forEach(key -> {
                            changeDetail.append(key).append(" ");
                        });
                    }

                    noticeMessage.append(changeDetail);
                }

                noticeMessage.append("；").append("变更原因：").append(memo);

                List<String> peopleList = Lists.newArrayList();
//                peopleList.add("doufu");
                peopleList.add("piaoxu");
                ConfigDingdingUtils.sendMessageAtChosePerson(noticeMessage.toString(),peopleList,imageList,branchId,manager.getName());
            }

        }
        return result;
    }

    /**
     * 回滚分支
     *
     * @param appId                应用id
     * @param profileId            环境id
     * @param branchId             分支id
     * @param targetReleaseVersion 回滚到的目标发布版本
     */
    @RequestMapping("/revertBranch")
    public EmptyResult revertBranch(String appId,
                                    String profileId,
                                    String branchId,
                                    Long targetReleaseVersion) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN && Objects.equals(branchId, BranchConstants.DEFAULT_BRANCH_ID)) {
            Set<String> keys = new HashSet<>();
            Set<Property> startProperties = Branches.findBranch(appId, profileId, branchId).getRelease().getProperties();
            Set<Property> endProperties = Releases.findRelease(appId, profileId, targetReleaseVersion).getProperties();
            PropertyDifference difference = Properties.compare(endProperties, startProperties);
            keys.addAll(difference.getAddedKeys());
            keys.addAll(difference.getModifiedValueKeys());
            keys.addAll(difference.getModifiedScopeKeys());
            keys.addAll(difference.getDeletedKeys());
            AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, keys);
        }

        RevertBranchOrder order = new RevertBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setTargetReleaseVersion(targetReleaseVersion);

        EmptyResult result = branchService.revertBranch(order);
        if (result.isSuccess()) {
            PropertyValues.revertPropertyValues(
                    appId,
                    profileId,
                    branchId,
                    targetReleaseVersion);
        }
        return result;
    }

    /**
     * 合并分支
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param sourceBranchId 源分支id
     */
    @RequestMapping("/mergeBranch")
    public MergeBranchResult mergeBranch(String appId,
                                         String profileId,
                                         String branchId,
                                         String sourceBranchId) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN && Objects.equals(branchId, BranchConstants.DEFAULT_BRANCH_ID)) {
            Set<String> keys = new HashSet<>();
            ComputeBranchMergenceResult computeBranchMergenceResult = computeBranchMergence(
                    appId,
                    profileId,
                    branchId,
                    sourceBranchId);
            FacadeUtils.assertSuccess(computeBranchMergenceResult);
            keys.addAll(computeBranchMergenceResult.getDifference().getAddedKeys());
            keys.addAll(computeBranchMergenceResult.getDifference().getModifiedValueKeys());
            keys.addAll(computeBranchMergenceResult.getDifference().getModifiedScopeKeys());
            keys.addAll(computeBranchMergenceResult.getDifference().getDeletedKeys());
            AppPropertyTypes.assertAdminOrOnlyReadWrite(appId, keys);
        }

        MergeBranchOrder order = new MergeBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);
        order.setSourceBranchId(sourceBranchId);

        MergeBranchResult result = branchService.mergeBranch(order);
        if (result.isSuccess()) {
            // 同步到配置value
            PropertyValues.changePropertyValues(
                    appId,
                    profileId,
                    branchId,
                    result.getPropertyChange());
            // 对敏感配置掩码
            if (CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
                Set<Property> properties = result.getPropertyChange().getAddedOrModifiedProperties();
                Set<Property> maskedProperties = AppPropertyTypes.maskProperties(appId, properties);
                properties.clear();
                properties.addAll(maskedProperties);
            }
        }
        return result;
    }

    /**
     * 计算分支合并
     *
     * @param appId          应用id
     * @param profileId      环境id
     * @param branchId       分支id
     * @param sourceBranchId 源分支id
     */
    @RequestMapping("/computeBranchMergence")
    public ComputeBranchMergenceResult computeBranchMergence(String appId,
                                                             String profileId,
                                                             String branchId,
                                                             String sourceBranchId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        // 计算分支合并的配置变动
        PropertyChange propertyChange = Branches.computeBranchMergence(appId, profileId, branchId, sourceBranchId);
        Set<Property> oldProperties = Branches.findBranch(appId, profileId, branchId).getRelease().getProperties();
        Set<Property> changedProperties = new HashSet<>();
        // 计算真正新增或修改的配置
        PropertyDifference difference = Properties.compare(propertyChange.getAddedOrModifiedProperties(), oldProperties);
        difference.getDeletedKeys().clear();
        propertyChange.getAddedOrModifiedProperties().stream()
                .filter(property -> difference.getAddedKeys().contains(property.getKey())
                        || difference.getModifiedValueKeys().contains(property.getKey())
                        || difference.getModifiedScopeKeys().contains(property.getKey()))
                .forEach(changedProperties::add);
        // 计算真正删除的配置
        oldProperties.stream()
                .filter(property -> propertyChange.getDeletedKeys().contains(property.getKey()))
                .forEach(property -> {
                    difference.addDeletedKeys(property.getKey());
                    changedProperties.add(property);
                });

        ComputeBranchMergenceResult result = FacadeUtils.buildSuccess(ComputeBranchMergenceResult.class);
        if (CurrentManagerAssert.current().getType() == ManagerType.ADMIN) {
            result.setChangedProperties(changedProperties);
        } else {
            // 对敏感配置掩码
            result.setChangedProperties(AppPropertyTypes.maskProperties(appId, changedProperties));
        }
        result.setDifference(difference);
        return result;
    }

    /**
     * 删除分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    @RequestMapping("/deleteBranch")
    public EmptyResult deleteBranch(String appId, String profileId, String branchId) {
        // 验权
        ManagerApps.assertAdminOrHaveApp(appId);
        if (Objects.equals(branchId, BranchConstants.DEFAULT_BRANCH_ID)) {
            CurrentManagerAssert.admin();
        }

        DeleteBranchOrder order = new DeleteBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        return branchService.deleteBranch(order);
    }

    /**
     * 查找分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     * @param branchId  分支id
     */
    @RequestMapping("/findBranch")
    public FindBranchResult findBranch(String appId, String profileId, String branchId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindBranchOrder order = new FindBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        FindBranchResult result = branchService.findBranch(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            maskRelease(result.getBranch().getRelease());
        }
        return result;
    }

    /**
     * 查找应用在环境下的所有分支
     *
     * @param appId     应用id
     * @param profileId 环境id
     */
    @RequestMapping("/findBranches")
    public FindBranchesResult findBranches(String appId, String profileId) {
        ManagerApps.assertAdminOrHaveApp(appId);
        FindBranchesOrder order = new FindBranchesOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);

        FindBranchesResult result = branchService.findBranches(order);
        if (result.isSuccess() && CurrentManagerAssert.current().getType() != ManagerType.ADMIN) {
            result.getBranches().stream().map(BranchInfo::getRelease).forEach(this::maskRelease);
        }
        return result;
    }

    @RequestMapping("/lockBranchVersion")
    public EmptyResult lockBranchVersion(String appId, String profileId,String branchId) {
        ManagerApps.assertAdminOrHaveApp(appId);

        ManagerInfo managerInfo = CurrentManagerAssert.current();
        String name = managerInfo.getName();

        if((!name.equals("zongzheng"))
                && (!name.equals("宗政"))
                && (!name.equals("piaoxu"))
                && (!name.equals("飘絮"))
                && (!name.equals("yuhang"))
                && (!name.equals("danpo"))
                && (!name.equals("丹魄"))){
            throw new BizException(Status.FAIL, CommonResultCode.UNAUTHORIZED.getCode(), String.format("当前管理员[%s]没有权限操作，封版请联系飘絮或宗政", managerInfo.getManagerId()));
        }

        ReleaseBranchResult releaseBranchResult = releaseBranch(appId, profileId, branchId, new PropertyChange(), "进行了封版操作");
        if (!releaseBranchResult.isSuccess()) {
            throw new BizException(Status.FAIL, CommonResultCode.UNKNOWN_ERROR.getCode(), "提交封版记录失败");
        }

        LockBranchOrder order = new LockBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        EmptyResult result = branchService.lockBranch(order);

        return result;
    }

    @RequestMapping("/unLockBranchVersion")
    public EmptyResult unLockBranchVersion(String appId, String profileId,String branchId) {
        ManagerApps.assertAdminOrHaveApp(appId);

        ManagerInfo managerInfo = CurrentManagerAssert.current();
        String name = managerInfo.getName();

        if((!name.equals("zongzheng"))
                && (!name.equals("宗政"))
                && (!name.equals("piaoxu"))
                && (!name.equals("飘絮"))
                && (!name.equals("yuhang"))
                && (!name.equals("danpo"))
                && (!name.equals("丹魄"))){
            throw new BizException(Status.FAIL, CommonResultCode.UNAUTHORIZED.getCode(), String.format("当前管理员[%s]没有权限操作，解除封版请联系飘絮或宗政", managerInfo.getManagerId()));
        }

        ReleaseBranchResult releaseBranchResult = releaseBranch(appId, profileId, branchId, new PropertyChange(), "进行了取消封版操作");
        if (!releaseBranchResult.isSuccess()) {
            throw new BizException(Status.FAIL, CommonResultCode.UNKNOWN_ERROR.getCode(), "提交封版记录失败");
        }

        UnLockBranchOrder order = new UnLockBranchOrder();
        order.setAppId(appId);
        order.setProfileId(profileId);
        order.setBranchId(branchId);

        EmptyResult result = branchService.unLockBranch(order);

        return result;
    }

    // 掩码敏感配置
    private void maskRelease(ReleaseInfo release) {
        Set<Property> maskedProperties = AppPropertyTypes.maskProperties(release.getAppId(), release.getProperties());
        release.setProperties(maskedProperties);
    }

    /**
     * 计算分支合并result
     */
    @Getter
    @Setter
    public static class ComputeBranchMergenceResult extends AbstractResult {
        // 变动的配置
        private Set<Property> changedProperties;
        // 配置差异
        private PropertyDifference difference;
    }
}
