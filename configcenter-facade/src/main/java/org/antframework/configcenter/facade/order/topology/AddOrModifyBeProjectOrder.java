package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.BeProjectType;
import org.antframework.configcenter.facade.vo.ProjectStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyBeProjectOrder extends AbstractOrder {

    private Long id;

    private String projectName;

    private String projectCode;

    private String projectMemo;

    private BeProjectType beProjectType;

    private Long customerId;

    private String accessPeople;

    private String customerContact;

    private String customerContactInfo;

    private String bdName;

    private String saName;

    private String pmName;

    private String stoName;

    private String approvePeople;

    private Date beginTime;

    private Date endTime;

    private String productIds;

    private ProjectStatus ProjectStatus;

}
