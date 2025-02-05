/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 15:05 创建
 */
package org.antframework.configcenter.biz.service.topology;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.BeEmployeeDao;
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.dao.BeProjectDao;
import org.antframework.configcenter.dal.dao.BeProjectWorkDao;
import org.antframework.configcenter.dal.entity.BeEmployee;
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.order.topology.AddOrModifyBeProjectOrder;
import org.antframework.configcenter.facade.vo.AccessLevel;
import org.antframework.configcenter.facade.vo.AccessStatus;
import org.antframework.configcenter.facade.vo.BeProjectType;
import org.antframework.configcenter.facade.vo.ProjectStatus;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;

/**
 * 添加或修改外包人员信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class AddOrModifyBeProjectService {

    private final BeProjectDao beProjectDao;

    private final BeProjectAccessDao beProjectAccessDao;

    private final BeProjectWorkDao beProjectWorkDao;

    private final BeEmployeeDao beEmployeeDao;

    @ServiceBefore
    public void before(ServiceContext<AddOrModifyBeProjectOrder, EmptyResult> context) {
    }

    @ServiceExecute
    public void execute(ServiceContext<AddOrModifyBeProjectOrder, EmptyResult> context) {
        AddOrModifyBeProjectOrder order = context.getOrder();

        BeProject beProject = new BeProject();

        beProject.setId(order.getId());
        beProject.setProjectName(order.getProjectName());
        beProject.setProjectCode(order.getProjectCode());
        beProject.setProjectMemo(order.getProjectMemo());
        beProject.setBeProjectType(order.getBeProjectType());
        beProject.setCustomerId(order.getCustomerId());
        beProject.setAccessPeople(order.getAccessPeople());
        beProject.setCustomerContact(order.getCustomerContact());
        beProject.setCustomerContactInfo(order.getCustomerContactInfo());
        beProject.setBdName(order.getBdName());
        beProject.setSaName(order.getSaName());
        beProject.setPmName(order.getPmName());
        beProject.setStoName(order.getStoName());
        beProject.setApprovePeople(order.getApprovePeople());
        beProject.setBeginTime(order.getBeginTime());
        beProject.setEndTime(order.getEndTime());
        beProject.setProductIds(order.getProductIds());
        beProject.setProjectStatus(order.getProjectStatus());

        beProjectDao.save(beProject);

        // 如果为新增项目，并且项目状态为售前状态，则初始化bd的项目管理权限
        if(order.getId() == null && ProjectStatus.PRE_SALE.equals(order.getProjectStatus())){

            BeEmployee beEmployee = beEmployeeDao.findByPenName(order.getBdName());

            BeProjectAccess beProjectAccess1 = new BeProjectAccess();
            beProjectAccess1.setProjectId(beProject.getId());
            beProjectAccess1.setAccessLevel(AccessLevel.ADMIN);
            beProjectAccess1.setAccessStatus(AccessStatus.NORMAL);
            beProjectAccess1.setEmployeeId(beEmployee.getId());

            beProjectAccessDao.save(beProjectAccess1);

            if(beEmployee.getId().longValue() != 6){
                BeProjectAccess beProjectAccess2 = new BeProjectAccess();
                beProjectAccess2.setProjectId(beProject.getId());
                beProjectAccess2.setAccessLevel(AccessLevel.ADMIN);
                beProjectAccess2.setAccessStatus(AccessStatus.NORMAL);
                beProjectAccess2.setEmployeeId(6L);
                beProjectAccessDao.save(beProjectAccess2);
            }


            if(beEmployee.getId().longValue() != 2){
                BeProjectAccess beProjectAccess3 = new BeProjectAccess();
                beProjectAccess3.setProjectId(beProject.getId());
                beProjectAccess3.setAccessLevel(AccessLevel.ADMIN);
                beProjectAccess3.setAccessStatus(AccessStatus.NORMAL);
                beProjectAccess3.setEmployeeId(2L);
                beProjectAccessDao.save(beProjectAccess3);
            }

        }

        // 如果为新增项目，初始化项目工作模板
        if(order.getId() == null){
            switch(order.getBeProjectType()){
                case STANDARD:
                case INTEGRATION:
                    initCommonProjectWork(beProject.getId());
                    break;
                case EVOLUTION:
                    initEvolutionProjectWork(beProject.getId());
                    break;
                case DATA:
                    initDataProjectWork(beProject.getId());
                    break;
                default:
                    System.out.println("do nothing");
            }
        }
    }

    private void initCommonProjectWork(Long projectId){
        String[] workArray = {
                "售前支持工作",
                "项目管理-沟通管理",
                "项目管理-客户支持",
                "项目管理-项目文档",
                "测试-功能测试",
                "测试-性能测试",
                "测试-稳定性测试",
                "系统上线-测试环境部署",
                "系统上线-生产环境部署",
                "系统上线-联调测试支持",
                "系统维护-生产问题排查",
                "系统维护-生产问题修复",
                "系统维护-漏洞修复",
                "系统维护-CICD",
                "知识转移-客户培训",
                "售后支持"};

        for(String work : workArray){
            BeProjectWork beProjectWork = new BeProjectWork();
            beProjectWork.setProjectId(projectId);
            beProjectWork.setWorkTitle(work);
            beProjectWork.setCreateEmployeeId(93L);
            beProjectWorkDao.save(beProjectWork);
        }
    }

    private void initEvolutionProjectWork(Long projectId){
        String[] workArray = {
                "产品设计-产品调研",
                "产品设计-功能沟通",
                "产品设计-产品设计",
                "产品设计-宣传资料",
                "产品研发-方案设计",
                "研发-系分",
                "研发-编码",
                "研发-bugfix",
                "研发-自测",
                "研发-讨论沟通",
                "测试-测试分析",
                "测试-测试用例编写",
                "测试-测试用例评审",
                "测试-测试用例执行",
                "测试-缺陷跟进",
                "运维-产品部署"};

        for(String work : workArray){
            BeProjectWork beProjectWork = new BeProjectWork();
            beProjectWork.setProjectId(projectId);
            beProjectWork.setWorkTitle(work);
            beProjectWork.setCreateEmployeeId(93L);
            beProjectWorkDao.save(beProjectWork);
        }
    }

    private void initDataProjectWork(Long projectId){
        String[] workArray = {
                "售前支持工作",
                "项目管理-需求沟通",
                "项目管理-项目文档",
                "数据分析-行内业务分析",
                "数据分析-行内数据分析",
                "数据分析-行外数据分析及引入",
                "模型设计-样本选择",
                "模型设计-y标定义",
                "模型设计-模型设计文档",
                "模型开发-样本数据特征回溯",
                "模型开发-模型开发",
                "模型开发-模型评估及验证",
                "模型汇报及交付-模型开发文档编写",
                "模型汇报及交付-模型评审",
                "模型汇报及交付-模型调优及定稿",
                "知识转移-客户培训",
                "售后支持"};

        for(String work : workArray){
            BeProjectWork beProjectWork = new BeProjectWork();
            beProjectWork.setProjectId(projectId);
            beProjectWork.setWorkTitle(work);
            beProjectWork.setCreateEmployeeId(93L);
            beProjectWorkDao.save(beProjectWork);
        }
    }

}
