package com.feijian.dao;

import com.feijian.domain.Company;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.domain.WareBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WareBillRepository extends JpaRepository<WareBill,Integer> , JpaSpecificationExecutor<WareBill> {

    public Page<WareBill> getWareBillsByProjectItem(ProjectItem item,Pageable pageable);
    public List<WareBill> getWareBillByBuyFrom(Company company);
    public Page<WareBill> getWareBillsByProject(Project project,Pageable pageable);
    public List<WareBill> getWareBillsByProject(Project project);
}
