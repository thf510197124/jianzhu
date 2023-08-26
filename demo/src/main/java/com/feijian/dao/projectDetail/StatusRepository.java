package com.feijian.dao.projectDetail;

import com.feijian.domain.FileDescription;
import com.feijian.domain.projectDetail.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StatusRepository extends JpaRepository<Status,Integer>, JpaSpecificationExecutor<StatusRepository> {
}
