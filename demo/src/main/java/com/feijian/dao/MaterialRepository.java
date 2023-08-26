package com.feijian.dao;

import com.feijian.domain.Material;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MaterialRepository extends JpaRepository<Material,Integer> , JpaSpecificationExecutor<Material> {
    public List<Material> getMaterialsByCodeLike(String code);
    public List<Material> getMaterialsByMaterialName(String materialName);
    public List<Material> getMaterialsByMaterialNameLike(String key);
    public List<Material> getMaterialsByMaterialType(MaterialType materialType);
    public Page<Material> findAll(Pageable pageable);
    //单位可以不统一
    @Query(value = "from Material m where m.materialName = :materialName " +
            "and m.texture = :texture and m.spec = :spec and m.materialType = :materialType")
    public List<Material> existsByMaterial(String materialName,String texture,String spec,MaterialType materialType);
    public List<Material> getMaterialsByMaterialTypeAndMaterialName(MaterialType materialType,String materialName);
    public List<Material> getMaterialsByMaterialTypeAndMaterialNameAndTexture(MaterialType materialType,String materialName,String texture);
    List<Material> getMaterialsByMaterialNameAndTexture(String materialName, String texture);
    List<Material> getMaterialsByTexture(String texture);
    List<Material> getMaterialsByMaterialTypeAndTexture(MaterialType type, String texture);

    @Query(value = "select m.code from Material m where m.materialType = :type and m.texture = :texture " +
            "and m.materialName = :name and m.spec = :spec and m.unitType = :unitType")
    String getMaterialCode(MaterialType type,String texture,String name,String spec,UnitType unitType);
}
