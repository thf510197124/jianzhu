package com.feijian.dao.projectDetail;

import com.feijian.dao.CompanyRepository;
import com.feijian.domain.Company;
import com.feijian.domain.projectDetail.OverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OverStatusRepository extends JpaRepository<OverStatus,Integer>, JpaSpecificationExecutor<OverStatusRepository> {
}
