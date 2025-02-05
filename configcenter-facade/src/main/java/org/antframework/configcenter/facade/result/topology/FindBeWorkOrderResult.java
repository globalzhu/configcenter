/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:59 创建
 */
package org.antframework.configcenter.facade.result.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.configcenter.facade.info.topology.BeOutsourceProjectRecordDTO;
import org.antframework.configcenter.facade.info.topology.BeWorkOrderDTO;

/**
 * 入项记录
 */
@Getter
@Setter
public class FindBeWorkOrderResult extends AbstractResult {
    private BeWorkOrderDTO beWorkOrderDTO;
}
