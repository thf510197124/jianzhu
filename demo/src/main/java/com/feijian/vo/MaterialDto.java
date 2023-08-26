package com.feijian.vo;

import com.feijian.domain.Material;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import lombok.Data;

@Data
public class MaterialDto {
    private String type;
    private String name;
    private String texture;
    private String unit;
    private String spec;
    public static Material getMaterial(MaterialDto materialDto){
        Material material = new Material();
        material.setMaterialName(materialDto.getName());
        material.setSpec(materialDto.getSpec());
        material.setTexture(materialDto.getTexture());
        material.setMaterialType(MaterialType.get(materialDto.getType()));
        material.setUnitType(UnitType.get(materialDto.getUnit()));
        return material;
    }
}
