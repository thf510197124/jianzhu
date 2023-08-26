package com.feijian.dao;

import com.feijian.domain.Company;
import com.feijian.domain.Contactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
@Repository
public interface ContactorRepository extends JpaRepository<Contactor,Integer> , JpaSpecificationExecutor<Contactor> {
    public List<Contactor> getContactorByCompany(Company company);
}
