package com.feijian.controller;

import com.feijian.domain.*;
import com.feijian.service.*;
import com.feijian.vo.Ware;
import com.feijian.vo.WareUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping("/ware")
public class WareController {
    MaterialService materialService;
    MaterialItemService materialItemService;
    WareBillService wareBillService;
    ProjectService projectService;
    CompanyService companyService;
    EmployeeService employeeService;

    public WareController(MaterialService materialService,
                          MaterialItemService materialItemService, WareBillService wareBillService,
                          ProjectService projectService, CompanyService companyService,
                          EmployeeService employeeService) {
        this.materialService = materialService;
        this.materialItemService = materialItemService;
        this.wareBillService = wareBillService;
        this.projectService = projectService;
        this.companyService = companyService;
        this.employeeService = employeeService;
    }

    @GetMapping("/project/add/{projectId}")
    public String addWare(@PathVariable int projectId, Model model, HttpSession session){
        Ware ware = new Ware();
        ware.setProject(projectService.get(projectId).getProjectName());
        model.addAttribute("ware",ware);
        model.addAttribute("attr","添加");
        session.setAttribute("wareId",0);
        return "/ware/wareBillForm";
    }
    @PostMapping("/project/add")
    public String addWare(Ware ware, HttpSession session) throws Exception {
        int wareId = (int) session.getAttribute("wareId");
        WareBill orign = null;
        if (wareId != 0){
            orign = wareBillService.get(wareId);
        }
        //orign == null，则直接输入null，convert中会自动创建新的wareBill
        WareBill wareBill = convert(ware,orign);
        System.out.println("添加订单的含税金额为1：" + wareBill.getUnTaxMoney());
        User user;
        if (wareId != 0){//相当于直接删除原油的wareBill
            user = orign.getUser();
            wareBill.setId(wareId);
            wareBill.setUser(user);
        }else{
            //是添加新的，所以重新设置user
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            wareBill.setUser(user);
        }
        System.out.println("添加订单的含税金额为2：" + wareBill.getUnTaxMoney());
        wareBill = wareBillService.saveWare(wareBill);

        //model.addAttribute("wareBill",wareBill);
        if (wareBill.getProjectItem() == null){
            handleProjectMaterialItemIndex(wareBill.getEntryList(),wareBill.getProject());
        }else{
            handleProjectItemIMaterialItemIndex(wareBill.getEntryList(),wareBill.getProject(),wareBill.getProjectItem());
        }
        return "redirect:/ware/bill/" + wareBill.getId();
    }
    @GetMapping("/project/update/{wareId}")
    public String updateWare(@PathVariable int wareId,Model model,HttpSession session) throws Exception{
        WareBill wb = wareBillService.get(wareId);
        Ware ware = WareUtil.get(wb);
        model.addAttribute("ware",ware);
        model.addAttribute("attr","修改");
        session.setAttribute("wareId",wb.getId());
        return "/ware/wareBillForm";
    }
    @GetMapping("/bill/{wareBillId}")
    public String wareBill(@PathVariable int wareBillId,Model model){
        WareBill wareBill = wareBillService.get(wareBillId);
        Project project = wareBill.getProject();
        ProjectItem projectItem = wareBill.getProjectItem();
        if (project != null){
            model.addAttribute("project",project);
        }
        if (projectItem != null){
            model.addAttribute("projectItem",projectItem);
        }

        model.addAttribute("ware",wareBill);
        List<MaterialItem> materialItems = wareBill.getEntryList();
        model.addAttribute("materialItems",materialItems);
        model.addAttribute("attr",wareBill.getBillType().getName());
        return "/ware/wareBill";
    }
    @GetMapping("/project/all/{projectId}/{pageNum}")
    public String projectWareList(@PathVariable int projectId,@PathVariable int pageNum,Model model,HttpSession session){
        int pageSize = session.getAttribute("warePageSize") == null? 10: (int) session.getAttribute("warePageSize");
        session.setAttribute("warePageSize",pageSize);
        PageRequest pr = PageRequest.of(pageNum-1,pageSize);
        session.setAttribute("warePageNum",pageNum);
        Project project = projectService.get(projectId);
        model.addAttribute("projectId",projectId);
        model.addAttribute("projectName",project.getProjectName());
        Page<WareBill> page = wareBillService.getWareBillsByProject(project,pr);
        model.addAttribute("page",page);
        return "/ware/wareList";
    }
    private WareBill convert(Ware ware,WareBill orign) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        WareUtil utils;
        if (orign == null){
            utils = new WareUtil(companyService,projectService,materialService,employeeService);
        }else{
            utils = new WareUtil(companyService,projectService,materialService,materialItemService,employeeService);
        }
        return utils.wareBill(ware,orign);
    }
    @GetMapping("/delete/{projectId}/{wareId}")
    public String deleteWare(@PathVariable int projectId,@PathVariable int wareId){
        WareBill wb = wareBillService.get(wareId);
        wareBillService.deleteWare(wb);
        return "redirect:/ware/project/all/" + projectId + "/1";
    }
    private void handleProjectItemIMaterialItemIndex(List<MaterialItem> entryList, Project project,ProjectItem projectItem) {
        handleProjectMaterialItemIndex(entryList,project);
        int begin = getBegin(entryList);
        int end = getEnd(entryList);
        if (projectItem.getMaterialItemIdBegin() == 0){
            projectService.updateProjectItemMaterialBegin(projectItem.getId(),begin);
        }
        if (projectItem.getMaterialItemIdEnd() < end){
            projectService.updateProjectItemMaterialEnd(projectItem.getId(),end);
        }
    }

    private void handleProjectMaterialItemIndex(List<MaterialItem> entryList, Project project) {
        int begin = getBegin(entryList);
        int end = getEnd(entryList);
        if (project.getMaterialItemIdBegin() <1){
            projectService.updateProjectMaterialBegin(project.getId(),begin);
        }
        if (project.getMaterialItemIdEnd() < end){
            projectService.updateProjectMaterialEnd(project.getId(), end);
        }
    }
    private int getBegin(List<MaterialItem> items){
        int begin = 0;
        for (MaterialItem item : items){
            if (item.getId() < begin){
                begin = item.getId();
            }
        }
        return begin;
    }
    private int getEnd(List<MaterialItem> items){
        int end = 0;
        for (MaterialItem item : items){
            if (item.getId() > end){
                end = item.getId();
            }
        }
        return end;
    }
}
