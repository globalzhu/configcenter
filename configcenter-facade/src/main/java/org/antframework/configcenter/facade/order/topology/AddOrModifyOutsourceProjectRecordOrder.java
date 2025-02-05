package org.antframework.configcenter.facade.order.topology;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.configcenter.facade.vo.RecordStatus;

import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 8:19 PM
 * @project configcenter
 */
@Getter
@Setter
public class AddOrModifyOutsourceProjectRecordOrder extends AbstractOrder {

    private Long id;

    private String applyName;

    private String applyTitle;

    private Long outsourceId;

    private String projectName;

    private String stoName;

    private String leaderName;

    private String pmName;

    private String bdName;

    private String saName;

    private Date planProjectEnterDate;

    private Date planProjectLeaveDate;

    private Date realProjectEnterDate;

    private Date realProjectLeaveDate;

    private RecordStatus recordStatus;

    private String rejectReason;

    private Long score1;

    private String score1Content;

    private Long score2;

    private String score2Content;

    private String approvePeople;

}
