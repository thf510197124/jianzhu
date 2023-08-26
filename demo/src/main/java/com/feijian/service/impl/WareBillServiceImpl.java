package com.feijian.service.impl;

import com.feijian.dao.MaterialRepository;
import com.feijian.dao.ProjectItemRepository;
import com.feijian.dao.WareBillRepository;
import com.feijian.domain.*;
import com.feijian.service.MaterialService;
import com.feijian.service.WareBillService;
import com.feijian.utils.MaterialCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.feijian.constant.CommonConstants.TAX_RATE;
import static com.feijian.vo.Utils.isNull;

@Service
public class WareBillServiceImpl implements WareBillService {
    private final WareBillRepository wareBillRepository;
    private final MaterialRepository materialRepository;
    @Autowired
    public WareBillServiceImpl(WareBillRepository wareBillRepository,MaterialRepository materialRepository) {
        this.wareBillRepository = wareBillRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public WareBill saveWare(WareBill bill) {
        float totalMoney = 0;
        float totalUnTax = 0f;
        for (MaterialItem item : bill.getEntryList()){
            item.setAmount(Math.abs(item.getAmount()));
            item.setPrice(Math.abs(item.getPrice()));
            if (item.getSummary() == 0){
                item.setSummary(item.getAmount() * item.getPrice());
            }
            totalMoney += item.getSummary();
            if (item.getUnTaxedSum() == 0){
                if (item.getTax() == 0f){
                    item.setTax(TAX_RATE);
                }
                item.setUnTaxedSum(item.getSummary() / (1 + item.getTax()));
            }
            totalUnTax += item.getUnTaxedSum();
            item.setWareBill(bill);
            combine(item);//处理材料的重复添加问题
        }
        bill.setTotalMoney(totalMoney);
        bill.setUnTaxMoney(totalUnTax);
        wareBillRepository.save(bill);
        //updatePi(bill);
        return bill;
    }
    private MaterialItem combine(MaterialItem orig){
        Material orgM = orig.getMaterial();
        if (!isNull(orgM.getCode())){
            List<Material> exist = materialRepository.getMaterialsByCodeLike(orgM.getCode());
            if (exist.size() > 0){
                //已经存在的话，设置之前用过的，直接返回
                Material tarM = exist.get(0);
                if (Objects.equals(tarM.getMaterialName(), orgM.getMaterialName())
                        && tarM.getMaterialType() == orgM.getMaterialType()
                        && Objects.equals(tarM.getMaterialName(), orgM.getMaterialName())
                        && tarM.getTexture().equals(orgM.getTexture())
                        && tarM.getUnitType() == orgM.getUnitType()){
                    orig.setMaterial(tarM);
                }else{
                    //说明code设置是错误的，那就重新设置
                    String code = MaterialCodeGenerator.getCode(orgM.getMaterialType(),orgM.getMaterialName(),orgM.getTexture(),orgM.getSpec(),orgM.getUnitType());
                    orig.getMaterial().setCode(code);
                }
            }//material不存在，那就不管，直接返回该material
        }else{
            String code = MaterialCodeGenerator.getCode(orgM.getMaterialType(),orgM.getMaterialName(),orgM.getTexture(),orgM.getSpec(),orgM.getUnitType());
            orig.getMaterial().setCode(code);
        }
        return orig;
    }
    @Override
    public boolean deleteWare(WareBill bill) {
        wareBillRepository.delete(bill);
        return true;
    }

    @Override
    public WareBill get(int wareId) {
        return wareBillRepository.getOne(wareId);
    }

    @Override
    public Page<WareBill> getWareBillByProjectItem(ProjectItem item, Pageable pageable) {
        return wareBillRepository.getWareBillsByProjectItem(item,pageable);
    }
    @Override
    public List<WareBill> getWareBillByCompany(Company company) {
        return wareBillRepository.getWareBillByBuyFrom(company);
    }
    @Override
    public Page<WareBill> getWareBillsByProject(Project project, Pageable pageable) {
        return wareBillRepository.getWareBillsByProject(project,pageable);
    }
    @Override
    public List<WareBill> getWareBillsByProject(Project project) {
        return wareBillRepository.getWareBillsByProject(project);
    }
    @Override
    public List<String> getCompaniesByProject(Project project) {
        List<WareBill> wareBills = wareBillRepository.getWareBillsByProject(project);
        List<String> companies = new ArrayList<>();
        for (WareBill bill : wareBills){
            if (bill.getBuyFrom() != null){
                companies.add(bill.getBuyFrom().getCompanyName());
            }
        }
        return companies;
    }
    @Override
    public List<String> getEmployeesByProject(Project project) {
        List<WareBill> wareBills = wareBillRepository.getWareBillsByProject(project);
        List<String> employees = new ArrayList<>();
        for (WareBill bill : wareBills){
            if (bill.getEmployee() != null){
                employees.add(bill.getEmployee().getName());
            }
        }
        return employees;
    }
}
