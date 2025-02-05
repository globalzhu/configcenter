/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-08-28 22:58 创建
 */
package org.antframework.configcenter.biz.service.topology;

import lombok.AllArgsConstructor;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.configcenter.dal.dao.BeProjectAccessDao;
import org.antframework.configcenter.dal.dao.BeProjectDao;
import org.antframework.configcenter.dal.dao.BeProjectWorkDao;
import org.antframework.configcenter.dal.entity.BeProject;
import org.antframework.configcenter.dal.entity.BeProjectAccess;
import org.antframework.configcenter.dal.entity.BeProjectWork;
import org.antframework.configcenter.facade.order.DeleteBeProjectOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceBefore;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;

import java.util.List;

/**
 * 删除项目信息
 */
@Service(enableTx = true)
@AllArgsConstructor
public class DeleteBeProjectService {
    private final BeProjectDao beProjectDao;
    private final BeProjectWorkDao beProjectWorkDao;
    private final BeProjectAccessDao beProjectAccessDao;

    @ServiceBefore
    public void before(ServiceContext<DeleteBeProjectOrder, EmptyResult> context) {

    }

    @ServiceExecute
    public void execute(ServiceContext<DeleteBeProjectOrder, EmptyResult> context) {
        Long projectId = context.getOrder().getId();

        BeProject deleteBeProject = new BeProject();
        deleteBeProject.setId(projectId);
        beProjectDao.delete(deleteBeProject);

        List<BeProjectWork> deleteBeProjectWorkList = beProjectWorkDao.findAllByProjectId(projectId);
        if(deleteBeProjectWorkList != null){
            deleteBeProjectWorkList.stream().forEach(deleteBeProjectWork ->{
                beProjectWorkDao.delete(deleteBeProjectWork);
            });
        }

        List<BeProjectAccess> deleteBeProjectAccessList =  beProjectAccessDao.findByProjectId(projectId);
        if(deleteBeProjectAccessList != null){
            deleteBeProjectAccessList.stream().forEach(deleteBeProjectAccess ->{
                beProjectAccessDao.delete(deleteBeProjectAccess);
            });
        }

    }
}
