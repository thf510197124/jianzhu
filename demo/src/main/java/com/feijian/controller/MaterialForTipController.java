package com.feijian.controller;

import com.feijian.domain.Material;
import com.feijian.item.MaterialType;
import com.feijian.service.MaterialService;
import com.feijian.utils.Utils;
import com.feijian.vo.MaterialDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/material/tip")
public class MaterialForTipController {
    MaterialService materialService;

    @Autowired
    public MaterialForTipController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @RequestMapping(value = "/nameList",method = RequestMethod.POST)
    public ResponseEntity<List<String>> getNames(@RequestBody String materialType) throws Exception {
        MaterialType materialType1 = MaterialType.get(materialType);
        List<String> names = materialService.getMaterialNames(materialType1);
        return new ResponseEntity<>(names, HttpStatus.OK);
    }
    @RequestMapping(value="/textures",method = RequestMethod.POST)
    public ResponseEntity<List<String>> getTexture(@RequestBody MaterialDto materialDto) throws Exception {
        MaterialType type = MaterialType.get(materialDto.getType());
        String name = materialDto.getName();
        List<String> texture;
        if (type == null && !Utils.isNullString(name)){
            texture = materialService.getTextureByName(name);
        }else if (type != null && Utils.isNullString(name)){
            texture = materialService.getTextureByType(type);
        }else if (type != null && !Utils.isNullString(name)){
            texture = materialService.getTextureBy(type,name);
        }else{
            texture = materialService.getTextureAll();
        }
        return new ResponseEntity<>(texture,HttpStatus.OK);
    }
    @RequestMapping(value = "/specs",method = RequestMethod.POST)
    public ResponseEntity<List<String>> getSpec(@RequestBody MaterialDto materialDto) throws Exception {
        MaterialType type = MaterialType.get(materialDto.getType());
        String name = materialDto.getName();
        String texture = materialDto.getTexture();
        List<String> specs;
        if (type != null && !Utils.isNullString(name) && !Utils.isNullString(texture)){
            specs = materialService.getSpecBy(type,name,texture);
        }else if (type == null && !Utils.isNullString(name) && !Utils.isNullString(texture)){
            specs = materialService.getSpecBy(name,texture);
        }else if (type != null && Utils.isNullString(name) && !Utils.isNullString(texture)){
            specs = materialService.getSpecByMaterialTypeAndTexture(type,texture);
        }else if (type != null && !Utils.isNullString(name) && Utils.isNullString(texture)){
            specs = materialService.getSpecByMaterialTypeAndName(type,name);
        }else if (type == null && Utils.isNullString(name) && !Utils.isNullString(texture)){
            specs = materialService.getSpecByTexture(texture);
        }else if (type == null && !Utils.isNullString(name) && Utils.isNullString(texture)){
            specs = materialService.getSpecByName(name);
        }else if (type != null && Utils.isNullString(name) && Utils.isNullString(texture)){
            specs = materialService.getSpecByMaterialType(type);
        }else{
            specs = materialService.getSpecAll();
        }
        return new ResponseEntity<>(specs,HttpStatus.OK);
    }

    @RequestMapping(value = "/material",method = RequestMethod.POST)
    public ResponseEntity<MaterialDto> getMaterial(@RequestBody String code){
        Material material = materialService.getMaterialByCode(code);
        if (material == null){
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
        MaterialDto dto = new MaterialDto();
        dto.setName(material.getMaterialName());
        dto.setSpec(material.getSpec());
        dto.setUnit(material.getUnitType().toString());
        dto.setType(material.getMaterialType().getName());
        dto.setTexture(material.getTexture());
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
