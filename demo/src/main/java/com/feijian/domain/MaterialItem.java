package com.feijian.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "material_item")
public class MaterialItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "订单列表ID")
    private int id;
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下单日期")
    private Date recordDate;
    /**
     * 材料
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Material.class,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "material_id",referencedColumnName = "id")
    @ExcelEntity(name = "产品信息")
    private Material material;
    /**
     * 产品进出口记录
     * 一个入库单或者出库单涉及到多条 货品记录
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = WareBill.class)
    @JoinColumn(name = "ware_bill_id",referencedColumnName = "id")
    @ToString.Exclude
    @ExcelEntity(name = "订单详情")
    private WareBill wareBill;
    /**
     * 数量
     */
    @Excel(name="数量")
    private float amount;
    /**
     * 含税单价
     */
    @Excel(name = "含税单价")
    private float price;
    /**
     * 单项金额,含税
     */
    @Excel(name = "含税金额")
    private float summary;

    @Excel(name = "税率")
    private float tax;
    private float unTaxedSum;
    /**
     * 工程所属分项
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ProjectItem.class)
    @JoinColumn(name = "project_item_id",referencedColumnName = "id")
    @ToString.Exclude
    private ProjectItem projectItem;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Project.class)
    @JoinColumn(name = "project__id",referencedColumnName = "id")
    @ToString.Exclude
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MaterialItem that = (MaterialItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
