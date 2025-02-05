package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;
import org.antframework.configcenter.facade.vo.VersionStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 4:56 PM
 * @project configcenter
 */
@Getter
@Setter
public class BeProductVersionDTO extends AbstractInfo {

    private Long id;

    private Long productId;

    private String versionNum;

    private String versionPdName;

    private String versionDevName;

    private Date releaseTime;

    private String releaseUrl;

    private VersionStatus versionStatus;

    private Date createTime;

    private Date updateTime;

}
