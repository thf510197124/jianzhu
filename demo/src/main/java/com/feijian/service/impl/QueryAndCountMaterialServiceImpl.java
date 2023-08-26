package com.feijian.service.impl;

import com.feijian.dao.MaterialItemRepository;
import com.feijian.domain.Material;
import com.feijian.domain.MaterialItem;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.item.MaterialType;
import com.feijian.service.QueryAndCountMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class QueryAndCountMaterialServiceImpl implements QueryAndCountMaterialService {
    MaterialItemRepository marItemRepository;
    @Autowired
    public QueryAndCountMaterialServiceImpl(MaterialItemRepository marItemRepository) {
        this.marItemRepository = marItemRepository;
    }

    @Override
    public List<MaterialItem> getMaterialItemsByProject(Project project) {
        return marItemRepository.findMaterialItemsByProject(project);
    }
    @Override
    public List<MaterialItem> getMaterialItemsByProjectItem(ProjectItem projectItem) {
        return marItemRepository.findMaterialItemByProjectItem(projectItem);
    }
    @Override
    public List<MaterialItem> getMaterialItemsByProjectAndMaterialName(Project pro, String name) {
        List<MaterialItem> materialItems = getMaterialItemsByProject(pro);
        List<MaterialItem> items = new ArrayList<>();
        for (MaterialItem item : materialItems){
            if (item.getMaterial().getMaterialName().equals(name)){
                items.add(item);
            }
        }
        return items;
    }
    @Override
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterialName(ProjectItem item, String name) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemByProjectItem(item);
        List<MaterialItem> items = new ArrayList<>();
        for (MaterialItem its : materialItems){
            if (its.getMaterial().getMaterialName().equals(name)){
                items.add(its);
            }
        }
        return items;
    }
    @Override
    public List<MaterialItem> getMaterialItemsByProjectAndMaterialType(Project pro, MaterialType type) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemsByProject(pro);
        List<MaterialItem> items = new ArrayList<>();
        for (MaterialItem item : materialItems){
            if (item.getMaterial().getMaterialType().equals(type)){
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterialType(ProjectItem item, MaterialType type) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemByProjectItem(item);
        List<MaterialItem> items = new ArrayList<>();
        for (MaterialItem its : materialItems){
            if (its.getMaterial().getMaterialType().equals(type)){
                items.add(its);
            }
        }
        return items;
    }

    @Override
    public List<MaterialItem> getMaterialItemsByProjectAndMaterial(Project pro, Material material) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemsByProject(pro);
        List<MaterialItem> items = new ArrayList<>();
        for (MaterialItem item : materialItems){
            if (item.getMaterial().equals(material)){
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterial(ProjectItem item, Material material) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemByProjectItem(item);
        List<MaterialItem> items = new ArrayList<>();
        for (MaterialItem its : materialItems){
            if (its.getMaterial().equals(material)){
                items.add(its);
            }
        }
        return items;
    }

    @Override
    public List<MaterialItem> getMaterialItemsByProjectAndMaterials(Project pro, List<Material> materials) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemsByProject(pro);
        List<MaterialItem> smaller = new ArrayList<>();
        for (MaterialItem item : materialItems){
            Material m = item.getMaterial();
            if (materials.contains(m)){
                smaller.add(item);
            }
        }
        return smaller;
    }

    @Override
    public List<MaterialItem> getMaterialItemsByProjectItemAndMaterials(ProjectItem prjItem, List<Material> materials) {
        List<MaterialItem> materialItems = marItemRepository.findMaterialItemByProjectItem(prjItem);
        List<MaterialItem> smaller = new ArrayList<>();
        for (MaterialItem item : materialItems){
            Material m = item.getMaterial();
            if (materials.contains(m)){
                smaller.add(item);
            }
        }
        return smaller;
    }
}
