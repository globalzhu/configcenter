package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 1:56 PM
 * @project configcenter
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate(true)
public class BeDeployOrder extends AbstractEntity {

    // 客户id
    @Column(length = 255)
    private String customerId;

    // 工单类型
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    // 如果是升级，关联节点id
    @Column(length = 255)
    private String withNodeId;

    // 交付类型
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private DeliverType deliverType;

    /********** 申请阶段，由业务人员提供 ***********/
    // 需求场景
    @Column
    private String scene;

    // 节点名称
    @Column
    private String nodeName;

    // 节点类型
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private NodeType nodeType;

    // 项目类型
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    // 部署产品
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    // 产品版本
    @Column(length = 255)
    private String productVersion;

    // 是否与Glab联通
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private ConnectGlab connectGlab;

    // 项目负责人
    @Column(length = 255)
    private String projectOwner;

    // 审批人列表
    @Column(length = 255)
    private String approveUser;

    // 审批消息
    @Column(length = 255)
    private String message;

    /********** 准备阶段，待指派运维 ***********/

    // 运维负责人
    @Column(length = 255)
    private String deployOwner;

    /********** 准备阶段，由运维人员提供 ***********/

    // 部署环境
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private DeployEnv deployEnv;

    // 部署方式
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private DeployType deployType;

    // 机器配置
    @Column
    private String machineConfig;

    // 网络配置
    @Column
    private String networkConfig;

    // 部署架构，语雀地址，针对非标架构部署提供
    @Column
    private String deployArchDoc;

    // 机构编码
    @Column
    private String instId;

    // 节点编码
    @Column
    private String nodeCode;

    // 证书详情
    @Column
    private String licenseDetail;

    // mesh mid
    @Column
    private String mid;

    // license有效期
    @Column
    private Date licenseValidDate;

    // license失效时间
    @Column
    private Long licenseExpireTime;

    /********** 部署阶段，由运维人员提供 ***********/

    // 部署完成时间
    @Column
    private Date deployTime;

    // 部署花费时间，以小时为单位
    @Column
    private Long deployCostTime;

    // 交付测试是否完成
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private FinishTest finishTest;

    // 交付测试花费时间，以小时为单位
    @Column
    private Long testCostTime;

    // 是否完成落盘
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private FinishLuopan finishLuopan;

    // 预期交付时间
    @Column
    private Date planDeployTime;

    // 信息确认函
    @Column
    private String confirmLetter;

    // 证书详情
    @Column
    private String certificateDetail;

    // 落盘文档
    @Column
    private String luopanDetail;

    // 定制化改动
    @Column
    private String customizeDetail;

    /********** 部署状态机 ***********/

    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
