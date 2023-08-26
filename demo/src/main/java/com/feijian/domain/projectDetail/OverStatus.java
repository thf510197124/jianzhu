package com.feijian.domain.projectDetail;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "over_status")
public class OverStatus extends Status {

    //工序资料
    @Column(name = "gong_xu_zi_liao")
    private boolean gongXuZiLiao;
    //竣工图
    @Column(name = "jun_gong_tu")
    private boolean junGongTu;
    //签证汇总
    @Column(name = "qian_zheng_hui_zong")
    private boolean qianZhengHuiZong;
    //竣工验收证明
    @Column(name = "jun_gong_yan_shou_zheng_ming")
    private boolean junGongYanShouZhengMing;
    //工程量汇总
    @Column(name = "gong_cheng_liang_hui_zong")
    private boolean gongChengLiangHuiZong;
    //送审日期
    @Column(name = "song_shen_date")
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date songShenDate;
    //送审金额
    @Column(name = "song_shen_money")
    private float songShenMoney;
    //审定金额
    @Column(name = "he_ding_money")
    private float heDingMoney;
    @Column(name = "shen_ding_dan")
    private String shenDingDan;
    //施工班组工程量
    @Column(name = "gong_cheng_liang")
    private boolean gongChengLiang;
    // 材料汇总
    @Column(name = "cai_liao_hui_zong")
    // 施工机械时间
    private boolean caiLiaoHuiZong;
    @Column(name = "shi_gong_ji_xie_time")
    private boolean shiGongJiXieTime;
    @OneToOne(targetEntity = ProjectDetail.class,mappedBy = "overStatus")
    @ToString.Exclude
    private ProjectDetail projectDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OverStatus that = (OverStatus) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
