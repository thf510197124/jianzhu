package com.feijian.controller;

import com.feijian.domain.*;
import com.feijian.domain.projectDetail.Status;
import com.feijian.exceptions.UnsuitableTableException;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import com.feijian.service.*;
import com.feijian.vo.Ware;
import com.feijian.vo.WareUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/ware")
public class MaterialController {
    MaterialService materialService;
    MaterialItemService materialItemService;
    @Autowired
    public MaterialController(MaterialService materialService,MaterialItemService materialItemService) {
        this.materialService = materialService;
        this.materialItemService = materialItemService;
    }

    @GetMapping("/material/all/{pageNum}")
    public String allMaterial(@PathVariable int pageNum, Model model, HttpSession session){
        int pageSize = session.getAttribute("materialPageSize") == null? 10: (int) session.getAttribute("materialPageSize");
        session.setAttribute("materialPageSize",pageSize);
        PageRequest pr = PageRequest.of(pageNum-1,pageSize);
        session.setAttribute("materialPageNum",pageNum);
        Page<Material> page = materialService.allMaterial(pr);
        model.addAttribute("page",page);
        return "/ware/material/materialList";
    }

    /**
     * 复制material，为了某些material比较相似，只有很小的差别的情况，复制后进行个别修改就可以
     * @param materialId material
     * @return 页面
     */
    @GetMapping("/material/delete/{materialId}")
    public String delete(@PathVariable int materialId){
        materialService.deleteMaterial(materialService.get(materialId));
        return "redirect:/ware/material/all/1";
    }

    /**
     * 修改提交的页面也是add，通过materialId来判断是添加还是修改
     * @param materialId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/material/update/{materialId}")
    public String material(@PathVariable int materialId,Model model,HttpSession session){
        Material material = materialService.get(materialId);
        model.addAttribute("material",material);
        session.setAttribute("materialId",materialId);
        model.addAttribute("attr","修改材料资料");
        List<MaterialItem> item = materialItemService.findMaterialItemByMaterial(material);
        boolean canDelete = item.size() == 0;
        model.addAttribute("canDelete",canDelete);
        return "/ware/material/materialForm";
    }
    @GetMapping("/material/add")
    public String addMaterial(Model model,HttpSession session){
        Material material = new Material();
        model.addAttribute("material",material);
        model.addAttribute("attr","添加材料资料");
        session.setAttribute("materialId",0);
        return "/ware/material/materialForm";
    }
    @PostMapping("/material/add")
    public String addMaterial(Material material,HttpSession session){
        int materialId = (int) session.getAttribute("materialId");
        if (materialId != 0){
            material.setId(materialId);
            session.removeAttribute("materialId");
        }
        materialService.saveOrUpdate(material);
        return "redirect:/ware/material/all/1";
    }

    @PostMapping("/isExistedCode")
    public ResponseEntity<Boolean> isMaterialCodeExist(@RequestBody String code){
        code = code.substring(1,code.length()-1);
        boolean isExisted = materialService.isExistCode(code);
        return new ResponseEntity<>(isExisted, HttpStatus.OK);
    }
    @GetMapping("/batchAdd")
    public String batchAdd(){
        return "/ware/material/batchAdd";
    }
    /**
     * 上传状态文件
     * @param file 上传的文件
     * @param session session
     * @return
     * @throws IOException
     */
    @PostMapping("/batchAddMaterial")
    public String batchAddMaterial(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        List<Material> materials = parseExcelForMaterial(file);
        assert materials != null;

        for (Material material:materials){
            if (material.getMaterialType() != null || material.getUnitType() != null){
                //可以直接保存
                materialService.saveOrUpdate(material);
            }else{
                System.out.println(material);
            }
        }
        return "";
    }
    @GetMapping("/download/sample/material")
    public ResponseEntity<InputStreamResource> download() throws IOException{
        //File file = ResourceUtils.getFile("classpath:/doc/添加材料示例文件.xlsx");
        ClassPathResource resource = new ClassPathResource("/doc/添加材料示例文件.xlsx");
        File file1 = resource.getFile();
        System.out.println(file1);
        InputStreamResource isr = new InputStreamResource(new FileInputStream(file1));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("content-type", "application/octet-stream;charset=utf-8")
                .header("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("添加材料示例文件.xlsx", StandardCharsets.UTF_8))
                .body(isr);
    }
    private List<Material> parseExcelForMaterial(MultipartFile file) throws UnsuitableTableException {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            XSSFWorkbook book = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = book.getSheetAt(0);
            List<Material> materials = new ArrayList<>();
            XSSFRow first = sheet.getRow(0);
            if (!Objects.equals(first.getCell(0).getStringCellValue(), "材料编码") ||
                    !Objects.equals(first.getCell(1).getStringCellValue(), "名称") ||
                    !Objects.equals(first.getCell(2).getStringCellValue(), "材质") ||
                    !Objects.equals(first.getCell(3).getStringCellValue(), "规格") ||
                    !Objects.equals(first.getCell(4).getStringCellValue(), "种类") ||
                    !Objects.equals(first.getCell(5).getStringCellValue(), "计量单位")){
                throw new UnsuitableTableException("文件的格式不符合要求");
            }
            /**
             * 除去标题与表头
             */
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row = sheet.getRow(i);
                row.getCell(0).setCellType(CellType.STRING);//因为在excel会有数据是int类型所以在这儿需要转换下不然会报错 getCell(0)这个意思是第几列需要转换
                Material material = new Material();
                material.setCode(row.getCell(0).getStringCellValue().trim());
                material.setMaterialName(row.getCell(1).getStringCellValue().trim());
                material.setTexture(row.getCell(2).getStringCellValue().trim());
                material.setSpec(row.getCell(3).getStringCellValue().trim());
                material.setMaterialType(MaterialType.get(row.getCell(4).getStringCellValue().trim()));
                material.setUnitType(UnitType.get(row.getCell(5).getStringCellValue().trim()));
                materials.add(material);
            }
            return materials;

        } catch (IOException ignored) {
        }
        return null;
    }


}
