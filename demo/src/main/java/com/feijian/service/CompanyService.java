package com.feijian.service;

import com.feijian.domain.Company;
import com.feijian.domain.Contactor;
import com.feijian.item.CompanyType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
public interface CompanyService {
    public Company get(int companyId);
    public void saveOrUpdate(Company company);
    public List<Company> findByCompanyNameLike(String key);
    public Company getByCompanyName(String companyName);
    public List<String> getByCompaniesNameLike(String key);
    public void delete(int id);
    public Contactor save(Contactor contactor);
    public Contactor getContactor(int contactorId);
    public List<Contactor> findContactorByCompany(Company company);
    public void deleteContactor(int contactorId);
    public Page<Company> findCompanyByPage(Pageable pageable);
    public boolean exist(String company);
    public List<Company> findCompanyByEmployee(String name);
    public List<Company> findCompanyByType(CompanyType type);
}
