package com.feijian.domain;

import com.feijian.item.ProjectStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 分项工程
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "project_item")
public class ProjectItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 分项工程名称
     */
    private String itemName;

    /**
     * 预算
     */
    private float preCost;
    private float cost;
    private ProjectStatus status;
    /**
     * 所属工程
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Project.class)
    @JoinColumn(name = "project_id",referencedColumnName = "id")
    @ToString.Exclude
    private Project project;
    @OneToMany(fetch = FetchType.LAZY,targetEntity = WareBill.class,mappedBy = "projectItem")
    @ToString.Exclude
    private List<WareBill> wareBills;
    /**
     * 以下两个参数时为了在查询中能够快速找到分项工程对应的物品条目所在位置
     */

    @Column(name = "material_item_id_begin")
    private int materialItemIdBegin;
    @Column(name = "material_item_id_end")
    private int materialItemIdEnd;
    /**
     * 创建者
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProjectItem that = (ProjectItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
