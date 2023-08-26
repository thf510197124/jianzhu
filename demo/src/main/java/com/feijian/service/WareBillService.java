package com.feijian.service;

import com.feijian.domain.Company;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.domain.WareBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface WareBillService {
    public WareBill saveWare(WareBill bill);
    public boolean deleteWare(WareBill bill);
    public WareBill get(int wareId);
    public Page<WareBill> getWareBillByProjectItem(ProjectItem item,Pageable pageable);
    public List<WareBill> getWareBillByCompany(Company company);
    public Page<WareBill> getWareBillsByProject(Project project, Pageable pageable);
    public List<WareBill> getWareBillsByProject(Project project);
    List<String> getCompaniesByProject(Project project);

    List<String> getEmployeesByProject(Project project);

}
