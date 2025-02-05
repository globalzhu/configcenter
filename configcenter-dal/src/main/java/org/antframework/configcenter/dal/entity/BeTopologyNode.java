package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 1:56 PM
 * @project configcenter
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_nodeName", columnNames = "nodeName"))
@Getter
@Setter
public class BeTopologyNode extends AbstractEntity {

    // 节点名称
    @Column(length = 255)
    private String nodeName;

    // 机构编码，对应mesh编码
    @Column(length = 255)
    private String instId;

    // 节点编码，对应mesh编码
    @Column(length = 255)
    private String nodeCode;

    // 客户id
    @Column(length = 255)
    private String customerId;

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

    // 部署环境
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private DeployEnv deployEnv;

    // 产品版本
    @Column(length = 255)
    private String productVersion;

    // 部署方式
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private DeployType deployType;

    // 项目负责人
    @Column(length = 255)
    private String projectOwner;

    // 运维负责人
    @Column(length = 255)
    private String deployOwner;

    // 部署完成时间
    @Column
    private Date deployTime;

    // 交付测试是否完成
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private FinishTest finishTest;

    // 是否与Glab联通
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private ConnectGlab connectGlab;

    // 机器配置
    @Column
    private String machineConfig;

    // 网络配置
    @Column
    private String networkConfig;

    // 证书详情
    @Column
    private String licenseDetail;

    // mesh id
    @Column
    private String mid;

    // license有效期
    @Column
    private Date licenseValidDate;

    // license失效时间
    @Column
    private Long licenseExpireTime;

    // 需求场景
    @Column
    private String scene;

    // 其他备注
    @Column
    private String remark;

    // 节点状态
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private NodeStatus nodeStatus;

}
