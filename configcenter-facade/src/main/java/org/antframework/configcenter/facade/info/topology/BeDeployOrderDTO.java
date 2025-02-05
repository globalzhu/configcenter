package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.*;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeDeployOrderDTO extends AbstractInfo {

    // 工单id
    private Long id;

    // 客户id
    private String customerId;

    // 客户信息
    private BeCustomerInfoDTO customerInfo;

    // 工单类型
    private OrderType orderType;

    // 如果是升级，关联节点id
    private String withNodeId;

    // 关联节点信息
    private BeTopologyNodeDTO withNode;

    // 交付类型
    private DeliverType deliverType;

    /********** 申请阶段，由业务人员提供 ***********/
    private String scene;

    // 节点类型
    private NodeType nodeType;

    // 项目类型
    private ProjectType projectType;

    // 部署产品
    private ProductType productType;

    // 产品版本
    private String productVersion;

    // 是否与Glab联通
    private ConnectGlab connectGlab;

    // 项目负责人
    private String projectOwner;

    // 审批人列表
    private String approveUser;

    private String instId;

    private String nodeName;

    /********** 准备阶段，待指派运维 ***********/

    // 运维负责人
    private String deployOwner;

    /********** 准备阶段，由运维人员提供 ***********/

    // 部署环境
    private DeployEnv deployEnv;

    // 部署方式
    private DeployType deployType;

    // 机器配置
    private String machineConfig;

    // 网络配置
    private String networkConfig;

    // 部署架构，语雀地址，针对非标架构部署提供
    private String deployArchDoc;

    // 节点编码
    private String nodeCode;

    // license有效期
    private Date licenseValidDate;

    // license失效时间
    private Long licenseExpireTime;

    /********** 部署阶段，由运维人员提供 ***********/

    // 部署完成时间
    private Date deployTime;

    // 部署花费时间，以小时为单位
    private Long deployCostTime;

    // 交付测试是否完成
    private FinishTest finishTest;

    // 交付测试花费时间，以小时为单位
    private Long testCostTime;

    // 是否完成落盘
    private FinishLuopan finishLuopan;

    /********** 部署状态机 ***********/

    private OrderStatus orderStatus;

    // 状态消息
    private String message;

    // 创建时间
    private Date createTime;

    // 修改时间
    private Date updateTime;

    // 预期交付时间
    private Date planDeployTime;

    // 信息确认函
    private String confirmLetter;

    // 证书详情
    private String certificateDetail;

    // 落盘文档
    private String luopanDetail;

    // 定制化改动
    private String customizeDetail;

}
