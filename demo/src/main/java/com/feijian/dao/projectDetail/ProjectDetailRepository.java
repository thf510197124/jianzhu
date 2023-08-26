package com.feijian.dao.projectDetail;

import com.feijian.domain.projectDetail.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail,Integer>, JpaSpecificationExecutor<ProjectDetailRepository> {
    @Query(value = "select * from project_detail where ready_status_id = ?1 or process_status_id = ?1 " +
            " or over_status_id = ?1 or pay_status_id = ?1",nativeQuery = true)
    public ProjectDetail getProjectDetailByStatus(int statusId);
}
