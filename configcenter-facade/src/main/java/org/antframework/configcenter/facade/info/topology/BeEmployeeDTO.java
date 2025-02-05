package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.EmployeeType;
import org.antframework.configcenter.facade.vo.PostType;
import org.antframework.configcenter.facade.vo.ServiceStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeEmployeeDTO extends AbstractInfo {

    private Long id;

    private String name;

    private String penName;

    private String workLoginName;

    private String phoneNum;

    private Long superiorId;

    private BeEmployeeDTO superiorDTO;

    private EmployeeType employeeType;

    private PostType postType;

    private Long employeeLevel;

    private Long unitCost;

    private String certNo;

    private String sex;

    private String education;

    private String salary;

    private String baseSite;

    private String major;

    private Long workAge;

    private String resumeFile;

    private Date entryTime;

    private Date leaveTime;

    private ServiceStatus serviceStatus;

    private Date createTime;

    private Date updateTime;

}
