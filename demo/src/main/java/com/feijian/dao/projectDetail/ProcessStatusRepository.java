package com.feijian.dao.projectDetail;

import com.feijian.domain.projectDetail.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessStatusRepository extends JpaRepository<ProcessStatus,Integer>, JpaSpecificationExecutor<ProcessStatusRepository> {
}
