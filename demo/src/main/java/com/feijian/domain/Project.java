package com.feijian.domain;

import com.feijian.domain.projectDetail.ProjectDetail;
import com.feijian.item.ProjectStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 中标单位
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Company.class)
    @JoinColumn(name = "bidder",referencedColumnName = "id")
    private Company bidder;
    /**
     * 建设单位
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Company.class)
    @JoinColumn(name = "owner",referencedColumnName = "id")
    private Company owner;
    /**
     * 监理单位
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Company.class)
    @JoinColumn(name = "supervisor",referencedColumnName = "id")
    private Company supervisor;
    /**
     * 设计单位
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Company.class)
    @JoinColumn(name = "designer",referencedColumnName = "id")
    private Company designer;
    /**
     * 施工负责人1
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn(name = "director1",referencedColumnName = "id")
    private Employee director1;
    /**
     * 施工负责人2
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn(name = "director2",referencedColumnName = "id")
    private Employee director2;
    /**
     * 施工负责人3
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn(name = "director3",referencedColumnName = "id")
    private Employee director3;
    /**
     * 施工技术负责人
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn(name = "technician",referencedColumnName = "id")
    private Employee technician;
    /**
     * 施工班组
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn(name = "teams",referencedColumnName = "id")
    private Employee teamsOfGroups;
    /**
     * 投标日期
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bidTime;
    /**
     * 中标金额
     */
    private float bidMoney;
    /**
     * 合同开始日
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractBegin;
    /**
     * 合同结束日
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractEnd;
    /**
     * 施工开始日
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date processBegin;
    /**
     * 施工结束日
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date processEnd;
    /**
     * 所有的分项
     */
    @OneToMany(fetch = FetchType.LAZY,targetEntity = ProjectItem.class,mappedBy = "project")
    @ToString.Exclude
    List<ProjectItem> projectItems;
    private String workContext;
    /**
     * 工程的状态，是否结束
     */
    @Enumerated(value = EnumType.STRING)
    private ProjectStatus status;
    /**
     * 创建者
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @Temporal(value = TemporalType.DATE)
    private Date addTime;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = ProjectDetail.class)
    @JoinColumn(name = "project_detail_id",referencedColumnName = "id")
    @ToString.Exclude
    private ProjectDetail projectDetail;

    /**
     * 以下两个参数时为了在查询中能够快速找到分项工程对应的物品条目所在位置
     */

    @Column(name = "material_item_id_begin")
    private int materialItemIdBegin;
    @Column(name = "material_item_id_end")
    private int materialItemIdEnd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return id != 0 && Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
