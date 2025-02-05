package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.LanguageType;
import org.antframework.configcenter.facade.vo.ProductDevStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeSystemDTO extends AbstractInfo {

    private Long id;

    private String systemCode;

    private String systemName;

    private String systemOwner;

    private BeEmployeeDTO systemOwnerDTO;

    private LanguageType languageType;

    private Date createTime;

    private Date updateTime;

}
