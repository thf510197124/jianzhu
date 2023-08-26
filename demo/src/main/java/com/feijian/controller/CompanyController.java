package com.feijian.controller;

import com.feijian.domain.Company;
import com.feijian.domain.Contactor;
import com.feijian.domain.WareBill;
import com.feijian.exceptions.ExistException;
import com.feijian.item.CompanyType;
import com.feijian.service.CompanyService;
import com.feijian.service.WareBillService;
import com.feijian.utils.Utils;
import com.feijian.vo.CompanyFindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/company")
public class CompanyController {
    CompanyService companyService;
    WareBillService wareBillService;

    @Autowired
    public CompanyController(CompanyService companyService, WareBillService wareBillService) {
        this.companyService = companyService;
        this.wareBillService = wareBillService;
    }

    @GetMapping("/add")
    public String addCompany(Model model,HttpSession session){
        Company company = new Company();
        model.addAttribute("company",company);
        model.addAttribute("attr","添加");
        session.setAttribute("companyId",0);
        return "/company/companyForm";
    }
    @GetMapping("/save")
    public String saveCompany(){
        return "redirect:/company/add";
    }
    @PostMapping("/save")
    public String saveCompany(Company company,Model model,HttpSession session){
        session.removeAttribute("companyId");
        int id = (int) session.getAttribute("companyId");
        if (id != 0){
            company.setId(id);
        }
        try{
            companyService.saveOrUpdate(company);
        }catch (ExistException e){
            model.addAttribute("error_msg",e.getMessage());
            return addCompany(model,session);
        }
        model.addAttribute("company",company);
        return "/company/company";
    }
    @GetMapping("/{companyId}")
    public String companyDetail(@PathVariable int companyId,Model model){
        Company company = companyService.get(companyId);
        model.addAttribute("company",company);
        List<Contactor> contactors = company.getContactors();
        model.addAttribute("contactors",contactors);
        return "/company/company";
    }
    @GetMapping("/update/{companyId}")
    public String update(@PathVariable int companyId, Model model, HttpSession session){
        Company company = companyService.get(companyId);
        session.setAttribute("companyId",companyId);
        model.addAttribute("company",company);
        model.addAttribute("attr","修改");
        return "/company/companyForm";
    }
    @GetMapping("/delete/{companyId}")
    public ResponseEntity<Boolean> deleteCompany(@PathVariable int companyId){
        Company company = companyService.get(companyId);
        List<WareBill> wareBillList = wareBillService.getWareBillByCompany(company);
        if (wareBillList != null && wareBillList.size() > 0 ){
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
        List<Contactor> contactors = companyService.findContactorByCompany(company);
        if (contactors != null && contactors.size() > 0){
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
        companyService.delete(companyId);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }
    @GetMapping("/forceDelete/{companyId}")
    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_root') or hasRole('ROLE_manager')")
    public ResponseEntity<Boolean> forceDeleteCompany(@PathVariable int companyId){
        Company company = companyService.get(companyId);
        List<WareBill> wareBillList = wareBillService.getWareBillByCompany(company);
        if (wareBillList != null && wareBillList.size() > 0 ){
            for (WareBill wb : wareBillList){
                wareBillService.deleteWare(wb);
            }
        }
        List<Contactor> contactors = companyService.findContactorByCompany(company);
        if (contactors != null && contactors.size() > 0){
            for (Contactor c : contactors){
                companyService.deleteContactor(c.getId());
            }
        }
        companyService.delete(companyId);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }
    @GetMapping("/all/{pageNum}")
    public String all(@PathVariable int pageNum,Model model,HttpSession session){
        int pageSize = session.getAttribute("companyPageSize") == null? 10: (int) session.getAttribute("companyPageSize");
        session.setAttribute("companyPageSize",pageSize);
        PageRequest pr = PageRequest.of(pageNum-1,pageSize);
        session.setAttribute("companyPageNum",pageNum);
        Page<Company> page = companyService.findCompanyByPage(pr);
        model.addAttribute("page",page);
        return "/company/companyList";
    }
    @GetMapping("/resetPageSize")
    public String setPageSize(@RequestParam int pageSize, HttpSession session){
        int pageNum = (int) session.getAttribute("companyPageNum");
        session.setAttribute("companyPageSize",pageSize);
        return "redirect:/company/all/" + pageNum;
    }
    @RequestMapping(value = "/nameList",method = RequestMethod.POST)
    public ResponseEntity<List<String>> getCompanyNameList(@RequestBody String name){
        List<String> names = companyService.getByCompaniesNameLike(name);
        return new ResponseEntity<>(names,HttpStatus.OK);
    }
    @GetMapping("/find")
    public String findCompany(Model model){
        CompanyFindVo findVo = new CompanyFindVo();
        model.addAttribute("findVo",findVo);
        model.addAttribute("companies",null);
        return "/company/find";
    }
    @GetMapping( "/findCompany")
    public String getCompanyNameList(){
        return "redirect:/company/find";
    }
    @PostMapping( "/findCompany")
    public String getCompanyNameList(CompanyFindVo findVo,Model model){
        List<Company> companies = null;
        if (!Utils.isNullString(findVo.getEmployee())){
            companies = companyService.findCompanyByEmployee(findVo.getEmployee().trim());
        }
        if (!Utils.isNullString(findVo.getName())){
            if (companies == null) {
                companies = companyService.findByCompanyNameLike(findVo.getName().trim());
            }else{
                companies = filterCompanyByName(companies,findVo.getName().trim());
            }
        }
        if (!Utils.isNullString(findVo.getCompanyType())){
            try {
                CompanyType type = CompanyType.valueOf(findVo.getCompanyType().trim());
                if (companies == null) {
                    companies = companyService.findCompanyByType(type);
                } else {
                    companies = filterCompanyByType(companies, type);
                }
            }catch (IllegalArgumentException ignored){
            }

        }
        model.addAttribute("companies",companies);
        model.addAttribute("findVo",findVo);
        return "/company/find";
    }
    @PostMapping("/isExisted")
    public ResponseEntity<Integer> isMaterialCodeExist(@RequestBody String name){
        name = name.substring(1,name.length()-1);
        List<Company> companies= companyService.findByCompanyNameLike(name);
        return new ResponseEntity<>(companies.size(), HttpStatus.OK);
    }
    private List<Company> filterCompanyByName(List<Company> companies,String name){
        List<Company> companies1 = new ArrayList<>();
        for (Company company : companies){
            if (company.getCompanyName().contains(name.trim())){
                companies1.add(company);
            }
        }
        return companies1;
    }
    private List<Company> filterCompanyByType(List<Company> companies,CompanyType type){
        List<Company> companies1 = new ArrayList<>();
        for (Company company : companies){
            if (company.getType() == type){
                companies1.add(company);
            }
        }
        return companies1;
    }
}
