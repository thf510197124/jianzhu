package com.feijian.domain;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.feijian.item.BillType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 本类用来记录产品出库单和入库单的主要内容，
 * 货品记录保存在MaterialItem中
 */
@Data
@Entity
@Table(name = "ware_bill")
@ExcelTarget("订单")
public class WareBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "订单ID")
    private int id;
    /**
     * 入库还是出库
     * 如果为true，出库为false
     */
    @Excel(name = "订单类型")
    private BillType billType;
    /**
     * 货单号
     */
    @Column(name = "order_code")
    @Excel(name = "订单号")
    private String orderCode;

    /**
     * 从哪个公司进货的，或者出货到哪个公司
     */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Company.class)
    @JoinColumn(name = "buy_form_id",referencedColumnName = "id")
    @ExcelIgnore
    private Company buyFrom;
    /**
     * 出入库时间
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订货时间")
    private Date orderTime;

    /**
     * 货品详情
     */
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,
            targetEntity = MaterialItem.class,mappedBy = "wareBill")
    @ExcelCollection(name = "商品列表")
    private List<MaterialItem> entryList;
    /**
     * 总金额
     */
    @Excel(name = "总金额")
    private float totalMoney;
    @Excel(name = "不含税金额")
    private float unTaxMoney;
    /**
     * 工程所属分项
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ProjectItem.class)
    @JoinColumn(name = "project_item_id",referencedColumnName = "id")
    @ExcelIgnore
    private ProjectItem projectItem;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Project.class)
    @JoinColumn(name = "project__id",referencedColumnName = "id")
    @ExcelIgnore
    private Project project;
    /**
     * 出入库记录人员
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @ExcelIgnore
    private User user;
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "添加时间")
    private Date addTime;
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn(name = "employee_id",referencedColumnName = "id")
    private Employee employee;
}
