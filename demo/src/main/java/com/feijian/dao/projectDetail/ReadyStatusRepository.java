package com.feijian.dao.projectDetail;

import com.feijian.domain.projectDetail.ReadyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadyStatusRepository extends JpaRepository<ReadyStatus,Integer>, JpaSpecificationExecutor<ReadyStatusRepository> {
}
