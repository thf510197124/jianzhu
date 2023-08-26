package com.feijian.vo;

import com.feijian.item.BillType;
import com.feijian.item.MaterialType;
import lombok.Data;

@Data
public class CountVo {
    private String project;
    private BillType billType;
    private MaterialType materialType;
    private String spec;
    private String name;
    private String projectItem;
    private String company;
    private String employee;

}
