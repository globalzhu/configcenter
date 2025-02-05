package org.antframework.configcenter.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.jpa.AbstractEntity;
import org.antframework.configcenter.facade.vo.MeshStatus;
import org.antframework.configcenter.facade.vo.NodeStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zongzheng
 * @date 2022/10/20 1:57 PM
 * @project configcenter
 */
@Entity
@Table
@Getter
@Setter
public class BeTopologyLink  extends AbstractEntity {

    // 本方hostId
    @Column
    private Long hostNodeId;

    // 合作方hostId
    @Column
    private Long guestNodeId;

    // 组网时间
    @Column
    private Date meshDate;

    // 组网场景
    @Column(length = 255)
    private String meshScene;

    // 组网状态
    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private MeshStatus meshStatus;






}
