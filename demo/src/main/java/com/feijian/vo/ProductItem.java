package com.feijian.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 把订单的内容映射成这条信息进行输出
 */
@Data
@ExcelTarget("productItem")
public class ProductItem implements Serializable {
    @Excel(name = "订单编号")
    private String orderCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "货单日期")
    private Date orderTime;
    @Excel(name = "所属工程")
    private String project;
    @Excel(name = "所属分项")
    private String projectItem;
    @Excel(name = "销售公司")
    private String buyFrom;
    @Excel(name = "订单类型")
    private String billType;
    @Excel(name = "领用班组")
    private String employee;

    @Excel(name = "材料种类")
    private String materialType;
    @Excel(name = "材料代码")
    private String code;
    @Excel(name = "材料名称")
    private String name;
    @Excel(name = "材质")
    private String texture;
    @Excel(name = "规格")
    private String spec;
    @Excel(name = "单位")
    private String unit;

    @Excel(name = "数量")
    private float amount;
    @Excel(name = "单价")
    private float price;
    @Excel(name = "金额")
    private float summary;




}
