package com.feijian.service;

import com.feijian.dao.MaterialRepository;
import com.feijian.dao.ProjectItemRepository;
import com.feijian.domain.Material;
import com.feijian.domain.MaterialItem;
import com.feijian.dao.MaterialItemRepository;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.exceptions.DifferentUnitException;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface QueryAndCountMaterialService {
    public List<MaterialItem> getMaterialItemsByProjectAndMaterialName(Project pro, String name);
    /**
     * 分项工程某项材料进出记录
     * @param item
     * @param materialName
     * @return
     */
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterialName(ProjectItem item, String materialName);
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterialType(ProjectItem item, MaterialType type);
    public List<MaterialItem> getMaterialItemsByProjectAndMaterialType(Project item,MaterialType type);
    public List<MaterialItem> getMaterialItemsByProjectAndMaterial(Project project, Material material);
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterial(ProjectItem item,Material material);
    public List<MaterialItem> getMaterialItemsByProjectAndMaterials(Project project,List<Material> materials);
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterials(ProjectItem item,List<Material> materials);

    /**
     * 工程材料进出记录
     * @param project
     * @return
     */
    public List<MaterialItem> getMaterialItemsByProject(Project project);

    /**
     * 分项工程材料进出记录
     * @param projectItem
     * @return
     */
    public List<MaterialItem> getMaterialItemsByProjectItem(ProjectItem projectItem);
    default float countAmountInWare(List<MaterialItem> items){
        float in = 0f;
        UnitType type = items.get(0).getMaterial().getUnitType();
        for (MaterialItem item : items){
            if (item.getMaterial().getUnitType() != type){
                throw new DifferentUnitException("货物的单位不一致，不能统计");
            }
            if (item.getAmount() > 0){
                in += item.getAmount();
            }
        }
        return in;
    }
    default float countAmountOutWare(List<MaterialItem> items){
        float out = 0f;
        UnitType type = items.get(0).getMaterial().getUnitType();
        for (MaterialItem item : items){
            if (item.getMaterial().getUnitType() != type){
                throw new DifferentUnitException("货物的单位不一致，不能统计");
            }
            if (item.getAmount() < 0){
                out += item.getAmount();
            }
        }
        return out;
    }
    default float amountInWare(List<MaterialItem> items){
        float amount = 0f;
        UnitType type = items.get(0).getMaterial().getUnitType();
        for (MaterialItem item : items){
            if (item.getMaterial().getUnitType() != type){
                throw new DifferentUnitException("货物的单位不一致，不能统计");
            }
            amount += item.getAmount();
        }
        return amount;
    }
    default float cost(List<MaterialItem> items){
        float cost = 0f;
        for (MaterialItem item : items){
            if (item.getSummary() > 0){
                cost += item.getSummary();
            }
        }
        return cost;
    }
}
