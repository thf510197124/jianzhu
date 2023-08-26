package com.feijian.dao;

import com.feijian.domain.Project;
import com.feijian.domain.projectDetail.ProjectDetail;
import com.feijian.item.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> , JpaSpecificationExecutor<Project> {
    Page<Project> findProjectByStatusOrderByIdDesc(ProjectStatus status,Pageable pageable);
    List<Project> findProjectByBidTimeBetween(Date start, Date end);
    @Query("select p from Project p where p.processBegin > ?1 or p.processEnd < ?2")
    List<Project> findProjectByWorkTime(Date start,Date end);
    Project findByProjectName(String projectItem);
    List<Project> findByProjectNameLike(String nameKey);
    Page<Project> findAll(Pageable pageable);
    @Query("select p from Project p where p.projectDetail = ?1")
    Project getProjectByProjectDetail(ProjectDetail projectDetail);
    @Modifying
    @Query(value = "update project as p set p.material_item_id_begin = ?2 where p.id = ?1",nativeQuery = true)
    @Transactional
    void updateProjectMaterialBegin(int projectId,int startIndex);
    @Modifying
    @Query(value = "update project as p set p.material_item_id_end = ?2 where p.id = ?1",nativeQuery = true)
    @Transactional
    public void updateProjectMaterialEnd(int projectId,int endIndex);

}
