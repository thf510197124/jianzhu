package com.feijian.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.feijian.domain.*;
import com.feijian.item.BillType;
import com.feijian.item.MaterialType;
import com.feijian.service.CompanyService;
import com.feijian.service.EmployeeService;
import com.feijian.service.ProjectService;
import com.feijian.service.WareBillService;
import com.feijian.vo.CountVo;
import com.feijian.vo.ProductItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.feijian.vo.Utils.isNull;

@Controller
@RequestMapping("/count")
public class CountController {
    ProjectService projectService;
    WareBillService wareBillService;
    CompanyService companyService;
    EmployeeService employeeService;
    @Autowired
    public CountController(ProjectService projectService, WareBillService wareBillService,
                           CompanyService companyService,EmployeeService employeeService) {
        this.projectService = projectService;
        this.wareBillService = wareBillService;
        this.companyService = companyService;
        this.employeeService = employeeService;
    }

    @RequestMapping("/project/{projectId}")
    public String projectCount(@PathVariable int projectId,  Model model, HttpSession session){
        warpModelForForm(projectId,model);
        session.setAttribute("projectId",projectId);
        CountVo countVo = new CountVo();
        model.addAttribute("count",countVo);
        return "/project/projectCount";
    }
    @PostMapping("/project")
    public String projectCount(CountVo count,Model model,HttpSession session){
        int projectId = (int) session.getAttribute("projectId");
        Project project = projectService.get(projectId);
        List<WareBill> wareBills = wareBillService.getWareBillsByProject(project);
        List<WareBill> userful = new ArrayList<>();
        //订单类型过滤
        if (count.getBillType() != null && count.getBillType() != BillType.STORE){
            for (WareBill wareBill : wareBills){
                if (wareBill.getBillType() == count.getBillType()){
                    userful.add(wareBill);
                }
            }
            wareBills = userful;
            userful = new ArrayList<>();
        }
        //分项过滤
        if (!isNull(count.getProjectItem())){
            ProjectItem item1 = projectService.getByItemName(project,count.getProjectItem());
            if (item1 != null) {
                for (WareBill wareBill : wareBills) {
                    if (wareBill.getProjectItem() == item1) {
                        userful.add(wareBill);
                    }
                }
                wareBills = userful;
                userful = new ArrayList<>();
            }
        }
        //供应商过滤
        if (!isNull(count.getCompany())){
            Company company1 =companyService.getByCompanyName(count.getCompany());
            if (company1 != null){
                for (WareBill wareBill : wareBills){
                    if (wareBill.getBuyFrom() == company1){
                        userful.add(wareBill);
                    }
                }
                wareBills = userful;
                userful = new ArrayList<>();
            }
        }
        //领用人过滤
        if (!isNull(count.getEmployee())){
            Employee employee1 =employeeService.getEmployeeByName(count.getEmployee());
            if (employee1 != null){
                for (WareBill wareBill : wareBills){
                    if (wareBill.getEmployee() == employee1){
                        userful.add(wareBill);
                    }
                }
            }
        }

        //如果前面几个都是空的，那么就相当于查询所有
        if (count.getBillType() == null && isNull(count.getCompany()) && isNull(count.getEmployee()) && isNull(count.getProjectItem())){
            userful = wareBills;
        }
        if (userful.size() == 0){
            userful = wareBills;
        }
        List<ProductItem> productItems = breakWareBillToProjectItem(userful);
        List<ProductItem> usefulMaterial = new ArrayList<>();
        //进行材料类型过滤
        if(count.getMaterialType() != null){
            if (productItems.size() != 0){
                for (ProductItem productItem : productItems){
                    if (productItem.getMaterialType().equals(count.getMaterialType().getName())){
                        usefulMaterial.add(productItem);
                    }
                }
                productItems = usefulMaterial;
                usefulMaterial = new ArrayList<>();
            }
        }
        //进行材料名称过滤
        if (!isNull(count.getName())){
            if (productItems.size() != 0){
                for (ProductItem productItem : productItems){
                    if (productItem.getName().contains(count.getName())){
                        usefulMaterial.add(productItem);
                    }
                }
                productItems = usefulMaterial;
                usefulMaterial = new ArrayList<>();
            }
        }
        //进行材料规格过滤
        if (!isNull(count.getSpec())){
            if (productItems.size() != 0){
                for (ProductItem productItem : productItems){
                    if (productItem.getSpec().contains(count.getSpec())){
                        usefulMaterial.add(productItem);
                    }
                }
            }
        }else{
            usefulMaterial = productItems;
        }
        if (count.getBillType() == BillType.STORE){
            usefulMaterial = makeStore(usefulMaterial);
        }
        model.addAttribute("productItems",usefulMaterial);
        model.addAttribute("count",count);
        warpModelForForm(projectId,model);
        session.setAttribute("data",productItems);
        return "/project/projectCount";
    }
    @GetMapping("/download")
    @ResponseBody
    public void download(HttpSession session, HttpServletResponse response) throws IOException {
        List<ProductItem> productItems = (List<ProductItem>) session.getAttribute("data");
        String title = "导出的数据.xlsx";
        XSSFWorkbook workbook = (XSSFWorkbook) ExcelExportUtil.exportExcel(new ExportParams(title,"sheet1"),ProductItem.class,productItems);
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(title.getBytes("utf-8"), "ISO-8859-1"));
        response.addHeader("Access-Control-Allow-Origin", "*");
        OutputStream out = null;
        try {
            //写出
            out = response.getOutputStream();
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
            out.close();
        }
    }
    private void warpModelForForm(int projectId,Model model){
        Project project = projectService.get(projectId);
        List<ProjectItem> items = project.getProjectItems();
        List<String> companies  = wareBillService.getCompaniesByProject(project);
        List<String> employees = wareBillService.getEmployeesByProject(project);
        model.addAttribute("items",items);
        model.addAttribute("companys",companies);
        model.addAttribute("employees",employees);
    }
    private List<MaterialItem> breakWareBill(List<WareBill> wareBills){
        if (wareBills.size() == 0){
            return new ArrayList<>();
        }
        List<MaterialItem> materialItems = new ArrayList<>();
        for (WareBill wareBill : wareBills){
            materialItems.addAll(wareBill.getEntryList());
        }
        return materialItems;
    }
    private List<ProductItem> breakWareBillToProjectItem(List<WareBill> wareBills){
        if (wareBills.size() == 0){
            return new ArrayList<>();
        }
        List<ProductItem> projectItems = new ArrayList<>();
        for (WareBill bill : wareBills){
            if (bill.getEntryList().size() != 0){
                for (MaterialItem item : bill.getEntryList()){
                    projectItems.add(warpWare(bill,item));
                }
            }
        }
        return projectItems;
    }
    private ProductItem warpWare(WareBill wareBill,MaterialItem item){
        ProductItem projectItem = new ProductItem();
        projectItem.setProject(wareBill.getProject().getProjectName());
        projectItem.setProjectItem(wareBill.getProjectItem() == null ? null : wareBill.getProjectItem().getItemName());
        projectItem.setOrderCode(wareBill.getOrderCode());
        projectItem.setBillType(wareBill.getBillType().getName());
        projectItem.setBuyFrom(wareBill.getBuyFrom() != null ? wareBill.getBuyFrom().getCompanyName():null);
        projectItem.setEmployee(wareBill.getEmployee()!= null ? wareBill.getEmployee().getName():null);
        projectItem.setOrderTime(wareBill.getOrderTime());

        projectItem.setMaterialType(item.getMaterial().getMaterialType().getName());
        projectItem.setSpec(item.getMaterial().getSpec());
        projectItem.setTexture(item.getMaterial().getTexture());
        projectItem.setUnit(item.getMaterial().getUnitType().name());
        projectItem.setName(item.getMaterial().getMaterialName());
        projectItem.setCode(item.getMaterial().getCode());

        projectItem.setAmount(item.getAmount());
        projectItem.setPrice(item.getPrice());
        projectItem.setSummary(item.getSummary());
        return projectItem;
    }
    private List<ProductItem> makeStore(List<ProductItem> items){
        Map<String,ProductItem> map = new HashMap<>();
        for (ProductItem item : items){
            if (item.getBillType().trim().equals(BillType.PLAN.getName())){
                continue;
            }
            if (Objects.equals(item.getBillType().trim(), BillType.USE.getName())){
                item.setAmount(item.getAmount() * (-1));
                item.setSummary(item.getAmount() * item.getPrice());
            }
            String code = item.getCode();
            if (map.get(code) == null){
                map.put(code,item);
            }else{
                ProductItem org = map.get(code);
                org.setAmount(org.getAmount() + item.getAmount());
                org.setSummary(org.getAmount() + item.getSummary());
            }
        }
        return new ArrayList<>(map.values());
    }
}
