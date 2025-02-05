package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;

/**
 * @author zongzheng
 * @date 2022/11/18 3:30 PM
 * @project configcenter
 */
@Getter
@Setter
public class CreateNodeCodeResult extends AbstractResult {

    // 节点编码
    private String nodeCode;

    // 结构编码
    private String instId;

    // license详情
    private String licenseDetail;

    private String mid;
}
