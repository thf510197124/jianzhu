package com.feijian.dao;

import com.feijian.domain.Company;
import com.feijian.item.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer>, JpaSpecificationExecutor<CompanyRepository> {
    public Company findByCompanyName(String companyName);
    @Query(value = "from Company c where c.companyName like concat('%',?1,'%')")
    public List<Company> findCompaniesByCompanyNameLike(String companyName);
    @Query(value = "select company_name from company as c where c.company_name like concat('%',:key,'%') limit 10",nativeQuery = true)
    public List<String> getCompanyNamesByCompanyNameLike(String key);
    public Page<Company> findAll(Pageable pageable);
    @Query(value = "select distinct c from Company as c left join c.contactors as co where (c.owner=?1 or co.name=?1)")
    public List<Company> findCompaniesByEmployee(String name);
    public List<Company> getCompaniesByType(CompanyType type);
}
