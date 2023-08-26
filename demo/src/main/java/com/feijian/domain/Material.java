package com.feijian.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import com.feijian.utils.MaterialCodeGenerator;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * 材料表，各种材料
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 材料代码
     */
    private String code;
    /**
     * 材料名称
     */
    @Column(name = "material_name")
    private String materialName;
    private String texture;
    /**
     * 规格
     */
    private String spec;
    /**
     * 货品种类
     */
    @Column(name = "material_type")
    @Enumerated(value = EnumType.ORDINAL)
    private MaterialType materialType;

    /**
     * 单位
     */
    @Column(name = "unit_type")
    @Enumerated(value = EnumType.ORDINAL)
    private UnitType unitType;
    public Material copy(){
        Material material = new Material();
        material.setMaterialType(materialType);
        material.setSpec(spec);
        material.setMaterialName(materialName);
        material.setTexture(texture);
        material.setUnitType(unitType);
        material.setCode(code);
        return material;
    }
    public Material (){}
    public Material(String code, String materialName,String texture,String spec,MaterialType materialType,UnitType unitType){
        this.code = code;
        if (Objects.equals(this.code, "") || this.code == null){
            this.code = MaterialCodeGenerator.getCode(materialType,materialName,texture,spec,unitType);
        }
        this.materialName = materialName;
        this.texture = texture;
        this.spec = spec;
        this.materialType = materialType;
        this.unitType = unitType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Material material = (Material) o;
        return Objects.equals(id, material.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
