package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.info.topology.BeCustomerInfoDTO;
import org.antframework.configcenter.facade.vo.*;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyNodeOrder extends AbstractOrder {

    private Long id;

    // 节点名称
    private String nodeName;

    // 机构编码，对应mesh编码
    private String instId;

    // 节点编码，对应mesh编码
    private String nodeCode;

    // 客户信息
    private BeCustomerInfoDTO customerInfo;

    // 客户id
    private String customerId;

    // 节点类型
    private NodeType nodeType;

    // 项目类型
    private ProjectType projectType;

    // 部署产品
    private ProductType productType;

    // 部署环境
    private DeployEnv deployEnv;

    // 产品版本
    private String productVersion;

    // 部署方式
    private DeployType deployType;

    // 项目负责人
    private String projectOwner;

    // 运维负责人
    private String deployOwner;

    // 部署完成时间
    private Date deployTime;

    // 交付测试是否完成
    private FinishTest finishTest;

    // 是否与Glab联通
    private ConnectGlab connectGlab;

    // 机器配置
    private String machineConfig;

    // 网络配置
    private String networkConfig;

    // 证书详情
    private String licenseDetail;

    // mesh mid
    private String mid;

    // license有效期
    private Date licenseValidDate;

    // license失效时间
    private Long licenseExpireTime;

    // 需求场景
    private String scene;

    // 其他备注
    private String remark;

    // 节点状态
    private NodeStatus nodeStatus;

}
