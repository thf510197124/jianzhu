package com.feijian.service.impl;

import com.feijian.dao.CompanyRepository;
import com.feijian.dao.ContactorRepository;
import com.feijian.domain.Company;
import com.feijian.domain.Contactor;
import com.feijian.exceptions.ExistException;
import com.feijian.item.CompanyType;
import com.feijian.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;
    ContactorRepository contactorRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, ContactorRepository contactorRepository) {
        this.companyRepository = companyRepository;
        this.contactorRepository = contactorRepository;
    }

    @Override
    public Company get(int companyId) {
        return companyRepository.getOne(companyId);
    }

    public void saveOrUpdate(Company company) throws ExistException {
        if (exist(company.getCompanyName()) && company.getId() == 0){
            throw new ExistException("公司名称已经存在,不能重复添加");
        }else{
            List<Company> company1 = companyRepository.findCompaniesByCompanyNameLike(company.getCompanyName());
            if (company1.size() > 1 || (company1.size() == 1 && company1.get(0).getId() != company.getId()) ){
                throw new ExistException("您想更改的名字与别的公司名称重复，不能修改");
            }
        }
        companyRepository.save(company);
        companyRepository.findByCompanyName(company.getCompanyName());
    }

    @Override
    public List<Company> findByCompanyNameLike(String key) {
        return companyRepository.findCompaniesByCompanyNameLike(key);
    }

    public Company getByCompanyName(String companyName){
        return companyRepository.findByCompanyName(companyName);
    }
    public List<String> getByCompaniesNameLike(String key){
        return companyRepository.getCompanyNamesByCompanyNameLike(key);
    }

    @Override
    public void delete(int id) {
        companyRepository.deleteById(id);
    }

    @Override
    public Contactor save(Contactor contactor) {
        contactorRepository.save(contactor);
        return contactor;
    }

    @Override
    public Contactor getContactor(int contactorId) {
        return contactorRepository.getOne(contactorId);
    }

    @Override
    public List<Contactor> findContactorByCompany(Company company) {
        return contactorRepository.getContactorByCompany(company);
    }

    @Override
    public void deleteContactor(int contactorId) {
        contactorRepository.deleteById(contactorId);
    }

    @Override
    public Page<Company> findCompanyByPage(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public boolean exist(String company) {
        return companyRepository.findCompaniesByCompanyNameLike(company).size() != 0;
    }

    @Override
    public List<Company> findCompanyByEmployee(String name) {
        return companyRepository.findCompaniesByEmployee(name);
    }

    @Override
    public List<Company> findCompanyByType(CompanyType type) {
        return companyRepository.getCompaniesByType(type);
    }
}
