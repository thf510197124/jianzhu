package com.feijian.service;

import com.feijian.domain.Material;
import com.feijian.item.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MaterialService {
    public Material saveOrUpdate(Material material);
    public boolean deleteMaterial(Material material);
    public Material getMaterialByCode(String code);
    public boolean isExistCode(String code);
    List<Material> getMaterialsByMaterialName(String materialName);
    List<Material> getMaterialsByMaterialNameLike(String key);
    public List<Material> getMaterialsByMaterialType(MaterialType materialType);
    public Material get(int material);
    public Page<Material> allMaterial(Pageable pageable);

    /**
     * 不包含code的查询
     * @param material
     * @return
     */
    public Material exist(Material material);
    public List<String> getMaterialNames(MaterialType type) throws Exception;
    public List<String> getTextureBy(MaterialType type,String materialName) throws Exception;
    public List<String> getSpecBy(MaterialType type,String materialName,String texture) throws Exception;
    public List<String> getSpecBy(String materialName,String texture) throws Exception;
    List<String> getTextureByName(String name) throws Exception;

    List<String> getTextureByType(MaterialType type) throws NoSuchMethodException, Exception;

    List<String> getSpecByMaterialTypeAndTexture(MaterialType type, String texture) throws Exception;

    List<String> getSpecByMaterialTypeAndName(MaterialType type, String name) throws Exception;

    List<String> getSpecByTexture(String texture) throws Exception;

    List<String> getSpecByName(String name) throws Exception;

    List<String> getSpecAll() throws Exception;

    List<String> getSpecByMaterialType(MaterialType type) throws Exception;

    List<String> getTextureAll() throws Exception;
    String getMaterialCode(Material material);
}
