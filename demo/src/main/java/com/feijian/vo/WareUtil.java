package com.feijian.vo;

import com.feijian.domain.*;
import com.feijian.item.BillType;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import com.feijian.service.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.feijian.constant.CommonConstants.TAX_RATE;
import static com.feijian.vo.Utils.isNull;

public class WareUtil {
    CompanyService companyService;
    ProjectService projectService;
    MaterialService materialService;
    MaterialItemService materialItemService;
    EmployeeService employeeService;

    public WareUtil(CompanyService companyService, ProjectService projectService,
                    MaterialService materialService,EmployeeService employeeService) {
        this.companyService = companyService;
        this.projectService = projectService;
        this.materialService = materialService;
        this.employeeService = employeeService;
    }
    public WareUtil(CompanyService companyService,ProjectService projectService,
                    MaterialService materialService,MaterialItemService materialItemService,
                    EmployeeService employeeService){
        this.companyService = companyService;
        this.projectService = projectService;
        this.materialService = materialService;
        this.materialItemService = materialItemService;
        this.employeeService = employeeService;
    }

    /**
     * 把Ware封装成WareBill,
     * @param ware 生成的Ware
     * @param wareBill 源wareBill，如果wareBill为空，表明是添加wareBill。否则是为了更新。
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public WareBill wareBill(Ware ware,WareBill wareBill) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (wareBill == null){
            wareBill = new WareBill();
            wareBill.setAddTime(new Date());//已经有wareBill时不需要更改该属性
        }
        if (ware.getOrderTime() == null){
            wareBill.setOrderTime(new Date());
        }else {
            wareBill.setOrderTime(ware.getOrderTime());
        }
        if (isNull(ware.getOrderCode())){
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            wareBill.setOrderCode(sdf.format(new Date()));
        }
        wareBill.setBillType(BillType.get(ware.getBillType()));
        String buyFrom = ware.getBuyFrom();
        wareBill.setBuyFrom(!isNull(ware.getEmployee()) ? companyService.getByCompanyName(buyFrom) : null);
        wareBill.setEmployee(isNull(ware.getEmployee()) ? null : employeeService.getEmployeeByName(ware.getEmployee()));
        //处理工程问题
        String projectName = ware.getProject();
        Project project = buyFrom != null && !Objects.equals(projectName, "")  ? projectService.getProjectByName(projectName) : null;
        ProjectItem projectItem = null;
        if (!isNull(ware.getProjectItem())){
            projectItem = projectService.getByItemName(project,ware.getProjectItem());
            wareBill.setProjectItem(projectItem);
            if (project == null){
                project = projectItem.getProject();
            }
        }
        wareBill.setProject(project);
        //oriItems可能为null，添加warebill时，还没有设置。如果是更新的话，则不存在这个问题
        List<MaterialItem> oriItems = wareBill.getEntryList();
        List<MaterialItem> items = warpItems(ware,project,projectItem);
        if (oriItems ==null){
            wareBill.setEntryList(items);
        }else{
            //这一条是专注于处理更新时的问题
            List<MaterialItem> resultItems  = handleItems(oriItems,items);
            wareBill.setEntryList(resultItems);
        }
        return wareBill;
    }

    /**
     * 两个list已经经过去重复
     * 该方法有两个作用
     * 1、删除原始wareBill中已经被删除的条目
     * 2、给存在的条目添加Id，用于更新。
     * 对于生成的集合，由wareBill负责保存。
     * @param oriItems 可能为null
     * @param items
     */
    private List<MaterialItem> handleItems(List<MaterialItem> oriItems, List<MaterialItem> items) {
        //targetMap是更新后的warebill组成的map
        Map<Material,MaterialItem> targetMap = new HashMap<>();
        for (MaterialItem item:items){
            targetMap.put(item.getMaterial(),item);
        }
        for (MaterialItem item : oriItems){
            MaterialItem tar = targetMap.get(item.getMaterial());//获取的是更新后的materialItem
            if (tar == null){//表明目标中没有该item，在更新后已经被删除
                materialItemService.deleteMaterialItem(item);
            }else{//表明更新后的列表中仍有该项，可能只是更改了价格，数量，金额等信息
                tar.setId(item.getId());//保留id，只是更新
            }
        }
        return new ArrayList<>(targetMap.values());
    }

    /**
     * 根据ware的属性，生成不带Id的MaterialItem
     *
     * @param ware
     * @param project 由于materialItem保留了该属性，所以需要
     * @param projectItem 由于materialItem保留了该属性，所以需要
     * @return 生成不带id的MaterialItem的list
     * @throws NoSuchMethodException 没有该方法
     * @throws InvocationTargetException  调用目标错误
     * @throws IllegalAccessException private
     */
    private List<MaterialItem> warpItems(Ware ware, Project project, ProjectItem projectItem) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        List<MaterialItem> items = new ArrayList<>();
        for (int i = 0;i < 20;i++){
            float amount = (float) Ware.class.getMethod("getAmount" + i).invoke(ware);
            if (amount == 0){//某个条目的数量为零，那么就表明该条目没有意义了。
                continue;
            }
            String type = (String) ware.getClass().getMethod("getType"+i).invoke(ware);
            String code = (String) ware.getClass().getMethod("getCode"+i).invoke(ware);
            String name = (String) ware.getClass().getMethod("getName"+i).invoke(ware);
            String texture = (String) ware.getClass().getMethod("getTexture"+i).invoke(ware);
            String spec = (String) ware.getClass().getMethod("getSpec"+i).invoke(ware);
            String unit = (String) ware.getClass().getMethod("getUnit"+i).invoke(ware);
            float price = (float) ware.getClass().getMethod("getPrice"+i).invoke(ware);
            float summary = (float) ware.getClass().getMethod("getSum"+i).invoke(ware);
            float tax = (float) ware.getClass().getMethod("getTax" + i).invoke(ware);
            if (tax == 0){
                tax = TAX_RATE;
            }
            MaterialItem item = warp(type,code,name,texture,spec,unit,amount,price,summary,tax);
            item.setProject(project);
            item.setProjectItem(projectItem);
            items.add(item);
        }
        return handleRepeat(items);
    }

    /**
     * 处理添加中的重复条目，有时候添加了一种物品，另一个同样规格种类的动心再次添加，浪费，这里进行合并。
     * @param items 需要去重的items
     * @return 没有重复的items
     */
    private List<MaterialItem> handleRepeat(List<MaterialItem> items){
        Map<Material,MaterialItem> maps = new HashMap<>();
        for (MaterialItem item : items){
            Material material = item.getMaterial();
            MaterialItem ori = maps.get(material);
            if (ori == null){ //在map中找不到，说明没有保存，进行保存
                maps.put(material,item);
            }else{ //在map中找到，需要进行合并
                float oriAmount = ori.getAmount();
                float oriPrice = ori.getPrice();
                float oriSummary = ori.getSummary();
                float itemPrice = item.getPrice();
                ori.setAmount(oriAmount + item.getAmount());
                ori.setSummary(oriSummary + item.getSummary());
                if (oriPrice != itemPrice){
                    ori.setPrice(ori.getSummary() / ori.getAmount());
                }
            }
        }
        return new ArrayList<>(maps.values());
    }

    /**
     * 1、根据给定属性，生产Item以及相关的material
     * @param type
     * @param code
     * @param name
     * @param texture
     * @param spec
     * @param unit
     * @param amount
     * @param price
     * @param summary
     * @return
     */
    private MaterialItem warp(String type, String code, String name, String texture, String spec, String unit, float amount, float price, float summary,float tax){
        MaterialItem item = new MaterialItem();
        MaterialAndAmount ma = handleMaterial(code,name,texture,spec,MaterialType.get(type),UnitType.get(unit));
        item.setAmount(amount * ma.getBei());
        item.setPrice(price / ma.getBei());
        if (summary == 0){
            item.setSummary(item.getAmount() * item.getPrice());
        }else {
            item.setSummary(summary);
        }
        item.setRecordDate(new Date());
        item.setMaterial(ma.getMaterial());//material已经保存
        item.setTax(tax);
        item.setUnTaxedSum(item.getSummary()/(1 + item.getTax()));
        return item;
    }

    /**
     * 本方法二个作用，
     * 1、查找材料是否保存过，没有添加过的就保存
     * 2、如果材料保存过，那就比较单位，生成单位的倍数，便于新添加的item进行数量和价格的转换
     * @param code
     * @param materialName
     * @param texture
     * @param spec
     * @param materialType
     * @param unitType
     * @return
     */
    private MaterialAndAmount handleMaterial(String code, String materialName,String texture,String spec,MaterialType materialType,UnitType unitType){
        Material orig = new Material(code,materialName,texture,spec,materialType,unitType);
        Material target = materialService.exist(orig);
        if (target != null){//数据库已经存在
            float bei = handleUnit(orig.getUnitType(),target.getUnitType());
            if (bei != 0){
                return new MaterialAndAmount(target,bei);
            }else{
                //数据库存在，但是单位不匹配
                Material finalM = materialService.saveOrUpdate(orig);
                return new MaterialAndAmount(finalM,1);
            }
        }else{
            //数据库不存在
            Material finalM = materialService.saveOrUpdate(orig);
            return new MaterialAndAmount(finalM,1);
        }
    }

    /**
     * 该类是一个辅助类，保留两个参数，仅此而已
     */
    @Data
    private static class MaterialAndAmount{
        private Material material;
        private float bei;

        public MaterialAndAmount(Material material, float bei) {
            this.material = material;
            this.bei = bei;
        }
    }
    /**
     * 单位转换
     * @param from 输入的单位
     * @param to 输出的单位，用于保存到数据库的数据
     * @return
     */
    private float handleUnit(UnitType from,UnitType to){
        if (from == to){
            return 1.0f;
        }
        if (from == UnitType.吨 && to == UnitType.公斤){
            return 1000.0f;
        }
        if (from == UnitType.公斤 && to == UnitType.吨){
            return 0.001f;
        }
        if (from == UnitType.只 && to == UnitType.百个){
            return 0.01f;
        }
        if (from == UnitType.百个 && to == UnitType.只){
            return 100f;
        }
        return 0;
    }

    public static Ware get(WareBill wareBill) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Ware ware = new  Ware();
        ware.setProject(wareBill.getProject().getProjectName());
        ware.setProjectItem(wareBill.getProjectItem() == null ? null : wareBill.getProjectItem().getItemName());
        ware.setOrderCode(wareBill.getOrderCode());
        ware.setOrderTime(wareBill.getOrderTime());
        ware.setBuyFrom(wareBill.getBuyFrom() != null ? wareBill.getBuyFrom().getCompanyName() : null);
        ware.setBillType(wareBill.getBillType().getName());
        for (int i = 0;i < wareBill.getEntryList().size();i++){
            MaterialItem item = wareBill.getEntryList().get(i);
            Ware.class.getMethod("setAmount" + i, float.class).invoke(ware,item.getAmount());
            Ware.class.getMethod("setPrice"+i,float.class).invoke(ware,item.getPrice());
            Ware.class.getMethod("setSum"+i,float.class).invoke(ware,item.getSummary());

            Ware.class.getMethod("setType" + i, String.class).invoke(ware,item.getMaterial().getMaterialType().getName());
            Ware.class.getMethod("setCode"+i, String.class).invoke(ware,item.getMaterial().getCode());
            Ware.class.getMethod("setName" + i, String.class).invoke(ware,item.getMaterial().getMaterialName());
            Ware.class.getMethod("setTexture"+i,String.class).invoke(ware,item.getMaterial().getTexture());
            Ware.class.getMethod("setSpec"+i,String.class).invoke(ware,item.getMaterial().getSpec());
            Ware.class.getMethod("setUnit"+i,String.class).invoke(ware,item.getMaterial().getUnitType().toString());
            Ware.class.getMethod("setTax" + i, float.class).invoke(ware,item.getTax());
        }
        return ware;
    }
}
