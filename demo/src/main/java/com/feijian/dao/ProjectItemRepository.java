package com.feijian.dao;

import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem,Integer> , JpaSpecificationExecutor<ProjectItem> {
    public List<ProjectItem> getProjectItemByProject(Project project);
    @Modifying
    @Query("update ProjectItem p set p.materialItemIdBegin = ?2 where p.id = ?1")
    @Transactional
    public void updateBeginMaterialId(int projectItemId,int materialIdBegin);
    @Modifying
    @Query("update ProjectItem p set p.materialItemIdEnd =?2 where p.id = ?1")
    @Transactional
    public void updateEndMaterialId(int projectItemId,int materialIdEnd);
    @Modifying
    @Query(value = "update project_item as p set p.material_item_id_begin = ?2 where p.id = ?1",nativeQuery = true)
    @Transactional
    public void updateProjectItemMaterialBegin(int projectItemId,int startIndex);
    @Modifying
    @Query(value = "update project_item as p set p.material_item_id_end = ?2 where p.id = ?1",nativeQuery = true)
    @Transactional
    public void updateProjectItemMaterialEnd(int projectItemId,int endIndex);
}
