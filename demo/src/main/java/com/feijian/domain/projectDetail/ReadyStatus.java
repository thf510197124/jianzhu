package com.feijian.domain.projectDetail;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "ready_status")
public class ReadyStatus extends Status {

    //中标通知书是否齐备
    @Column(name = "bidder_notice_ready")
    private boolean bidderNoticeReady;
    //工程合同是否齐备
    @Column(name = "project_contract_ready")
    private boolean projectContractReady;
    //中标清单是否齐备
    @Column(name = "bidder_list_ready")
    private boolean bidderListReady;

    //施工图纸是否齐备
    @Column(name = "blue_print_ready")
    private boolean blueprintReady;
    //技术重点
    private String keynote;
    //进度影响因素
    private String factors;
    //进度计划
    private String schedule;
    //重点材料情况
    @Column(name = "material_keynote")
    private String materialKeyNote;
    @OneToOne(targetEntity = ProjectDetail.class,mappedBy = "readyStatus")
    @ToString.Exclude
    private ProjectDetail projectDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReadyStatus that = (ReadyStatus) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
