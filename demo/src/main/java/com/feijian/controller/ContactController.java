package com.feijian.controller;

import com.feijian.domain.Company;
import com.feijian.domain.Contactor;
import com.feijian.service.CompanyService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/contact")
public class ContactController {
    private final CompanyService companyService;

    @Autowired
    public ContactController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/add/{companyId}")
    public String addContact(@PathVariable int companyId, Model model, HttpSession session){
        session.setAttribute("companyId",companyId);
        session.setAttribute("contactorId",0);
        Contactor contactor= new Contactor();
        model.addAttribute("contactor",contactor);
        model.addAttribute("attr","添加");
        return "/contact/contactorForm";
    }

    /**
     * 根据contactorID 是否等于0，判断是更新还是添加
     * @param contactor
     * @param session
     * @return
     */
    @PostMapping("/save")
    public String saveContact(Contactor contactor,HttpSession session){
        int cId = (int)session.getAttribute("contactorId");
        int companyId;
        if (cId == 0){
            companyId = (int) session.getAttribute("companyId");
            Company company = companyService.get(companyId);
            contactor.setCompany(company);
        }else{
            contactor.setId(cId);
            companyId = contactor.getCompany().getId();
        }
        companyService.save(contactor);
        session.removeAttribute("contactorId");
        session.removeAttribute("companyId");
        return "redirect:/company/" + companyId;
    }
    @GetMapping("/update/{contactId}")
    public String update(@PathVariable int contactId, Model model,HttpSession session){
        session.setAttribute("contactorId",contactId);
        Contactor contactor= companyService.getContactor(contactId);
        model.addAttribute("contactor",contactor);
        model.addAttribute("attr","修改");
        return "/contact/contactorForm";
    }

}
