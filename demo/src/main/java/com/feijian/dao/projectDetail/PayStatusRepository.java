package com.feijian.dao.projectDetail;

import com.feijian.domain.projectDetail.PayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PayStatusRepository extends JpaRepository<PayStatus,Integer>, JpaSpecificationExecutor<PayStatusRepository> {
}
