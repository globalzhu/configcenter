package org.antframework.configcenter.facade.info.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractInfo;

/**
 * @author zongzheng
 * @date 2022/11/18 4:26 PM
 * @project configcenter
 */
@Getter
@Setter
public class JanusNodeRequestDTO extends AbstractInfo {

    //node_type:0-GLab、1-GAIA、2-GLite
    String node_type;

    //inst_type: 机构类型：00-蓝象、01-普通机构、02-SaaS管理机构、03-联盟盟主机构
    String inst_type;

    String inst_name;
}
