package com.feijian.service.impl;

import com.feijian.dao.MaterialItemRepository;
import com.feijian.dao.MaterialRepository;
import com.feijian.domain.Material;
import com.feijian.domain.MaterialItem;
import com.feijian.item.MaterialType;
import com.feijian.service.MaterialService;
import com.feijian.utils.MaterialCodeGenerator;
import com.feijian.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.feijian.vo.Utils.isNull;

@Service
public class MaterialServiceImpl implements MaterialService {
    MaterialRepository materialRepository;
    MaterialItemRepository materialItemRepository;
    @Autowired
    public void setMaterialRepository(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }
    @Autowired
    public void setMaterialItemRepository(MaterialItemRepository materialItemRepository) {
        this.materialItemRepository = materialItemRepository;
    }
    public Material saveOrUpdate(Material material){
        trimMaterial(material);
        Material m = exist(material);
        if (m != null){
            return m;
        }else{
            if (material.getCode() != null){
                boolean existCode = isExistCode(material.getCode());
                if (existCode){
                    //code已经存在，那就改写
                    material.setCode(MaterialCodeGenerator.getCode(material));
                }
            }else{
                //没有code，那就添加code
                material.setCode(MaterialCodeGenerator.getCode(material));
            }
            materialRepository.save(material);
            return materialRepository.getOne(material.getId());
        }
    }

    private void trimMaterial(Material material) {
        material.setMaterialName(material.getMaterialName().trim());
        material.setSpec(material.getSpec().trim());
        material.setTexture(material.getTexture().trim());
        material.setCode(material.getCode().trim());
    }

    /**
     * 如果已经有MaterialItem时，不能删除
     * @param material
     * @return
     */
    public boolean deleteMaterial(Material material){
        List<MaterialItem> materialItems = materialItemRepository.findMaterialItemsByMaterial(material);
        if (materialItems != null && materialItems.size() > 0){
            return false;
        }else{
            materialRepository.delete(material);
            return true;
        }
    }
    public Material getMaterialByCode(String code){
        List<Material> materials = materialRepository.getMaterialsByCodeLike(code);
        if (materials.size() == 0){
            return null;
        }else{
            return materials.get(0);
        }
    }

    @Override
    public boolean isExistCode(String code) {
        return getMaterialByCode(code) != null;
    }

    public List<Material> getMaterialsByMaterialName(String materialName){
        return materialRepository.getMaterialsByMaterialName(materialName);
    }
    public List<Material> getMaterialsByMaterialNameLike(String key){
        return materialRepository.getMaterialsByMaterialNameLike(key);
    }
    public List<Material> getMaterialsByMaterialType(MaterialType materialType){
        return materialRepository.getMaterialsByMaterialType(materialType);
    }

    @Override
    public Material get(int materialId) {
        return materialRepository.getOne(materialId);
    }

    @Override
    public Page<Material> allMaterial(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    /**
     * 认为code是唯一的，比较的时候不比较code；
     * 同时，单位也可能是不一样的，所以也没考虑单位问题
     * @param m
     * @return
     */
    @Override
    public Material exist(Material m) {
        List<Material> materials = materialRepository.existsByMaterial(m.getMaterialName(), m.getTexture(), m.getSpec(),m.getMaterialType());
        return materials != null && materials.size() > 0 ? materials.get(0):null;
    }

    @Override
    public List<String> getMaterialNames(MaterialType type) throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialType(type);
        return getFields(materials,Material.class.getDeclaredMethod("getMaterialName"));
    }

    @Override
    public List<String> getTextureBy(MaterialType type, String materialName) throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialTypeAndMaterialName(type,materialName);
        return getFields(materials,Material.class.getDeclaredMethod("getTexture"));
    }

    @Override
    public List<String> getSpecBy(MaterialType type, String materialName, String texture) throws  Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialTypeAndMaterialNameAndTexture(type,materialName,texture);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getSpecBy(String materialName, String texture) throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialNameAndTexture(materialName,texture);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getTextureByName(String name) throws Exception{
        List<Material> materials = materialRepository.getMaterialsByMaterialName(name);
        return getFields(materials,Material.class.getDeclaredMethod("getTexture"));
    }

    @Override
    public List<String> getTextureByType(MaterialType type) throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialType(type);
        return getFields(materials,Material.class.getDeclaredMethod("getTexture"));
    }

    @Override
    public List<String> getSpecByMaterialTypeAndTexture(MaterialType type, String texture)  throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialTypeAndTexture(type,texture);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getSpecByMaterialTypeAndName(MaterialType type, String name) throws Exception{
        List<Material> materials = materialRepository.getMaterialsByMaterialTypeAndMaterialName(type,name);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getSpecByTexture(String texture) throws Exception{
        List<Material> materials = materialRepository.getMaterialsByTexture(texture);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getSpecByName(String name) throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialName(name);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getSpecAll() throws Exception {
        List<Material> materials = materialRepository.findAll();
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getSpecByMaterialType(MaterialType type)  throws Exception {
        List<Material> materials = materialRepository.getMaterialsByMaterialType(type);
        return getFields(materials,Material.class.getDeclaredMethod("getSpec"));
    }

    @Override
    public List<String> getTextureAll()  throws Exception {
        List<Material> materials = materialRepository.findAll();
        return getFields(materials,Material.class.getDeclaredMethod("getTexture"));
    }

    @Override
    public String getMaterialCode(Material material) {
        return materialRepository.getMaterialCode(material.getMaterialType(),material.getTexture(),
                material.getCode(),material.getSpec(),material.getUnitType());
    }

    private List<String> getFields(List<Material> materials, Method method) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String,Integer> fields = new HashMap<>();
        for (Material material : materials){
            String f = (String) method.invoke(material);
            Integer count = fields.get(f);
            if (count == null){
                count = 0;
            }
            if (!Utils.isNullString(f)){
                fields.put(f,++count);
            }
        }
        if (fields.size() <= 10){
            return new ArrayList<>(fields.keySet());
        }
        Map<String,Integer> sortMapByValues = Utils.sortMapByValues(fields);
        List<String> strings = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : sortMapByValues.entrySet()){
            if (strings.size() < 10){
                strings.add(entry.getKey());
            }
        }
        return strings;
    }
}
