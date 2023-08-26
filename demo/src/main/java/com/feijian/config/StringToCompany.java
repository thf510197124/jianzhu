package com.feijian.config;

import com.feijian.domain.Company;
import com.feijian.service.CompanyService;
import com.feijian.service.EmployeeService;
import com.feijian.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCompany implements Converter<String, Company> {
    private  CompanyService companyService;
    public void setService() {
        this.companyService = SpringContextUtil.getBean(CompanyService.class);
    }

    @Override
    public Company convert(String s) {
        setService();
        if (s.trim().length() == 0){
            return null;
        }
        if (companyService.exist(s)){
            return companyService.getByCompanyName(s);
        }else{
            Company company = new Company();
            company.setCompanyName(s);
            companyService.saveOrUpdate(company);
            return company;
        }
    }
}
