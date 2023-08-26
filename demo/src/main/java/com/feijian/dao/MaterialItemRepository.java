package com.feijian.dao;

import com.feijian.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MaterialItemRepository extends JpaRepository<MaterialItem,Integer>, JpaSpecificationExecutor<MaterialItem> {
    public List<MaterialItem> findMaterialItemsByMaterial(Material material);
    public List<MaterialItem> getMaterialItemsByWareBill(WareBill wareBill);
    @Query(value = "select mn" +
            " from material_item as mn join material m on mn.material_id = m.id where m.material_name = ?1"
            ,nativeQuery = true)
    public List<MaterialItem> findMaterialItemByMaterialName(String name);
    @Query(value = "select mn from MaterialItem mn where " +
            " mn.id >= ?2 and mn.id <= ?3 " +
            "and mn.projectItem = ?1")
    public List<MaterialItem> findMaterialItemByProjectItem(ProjectItem item,int mItemIdBegin,int mItemIdEnd);
    default List<MaterialItem> findMaterialItemByProjectItem(ProjectItem item){
        return findMaterialItemByProjectItem(item,item.getMaterialItemIdBegin(),item.getMaterialItemIdEnd());
    }
    /**
     * 这个查询可能有问题，测试后再说
     * 直接在数据库查询
     * @param pro
     * @return
     */
    @Query(value = "select mn from MaterialItem mn where mn.project = ?1")
    public List<MaterialItem> findMaterialItemsByProject(Project  pro);
    @Modifying
    @Query(value = "delete from material_item where id = ?1",nativeQuery = true)
    @Transactional
    public void deleteMaterialItemById(int materialId);
}
