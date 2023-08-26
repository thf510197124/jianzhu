package com.feijian.service.impl;

import com.feijian.dao.*;
import com.feijian.domain.*;
import com.feijian.item.MaterialType;
import com.feijian.service.MaterialItemService;
import com.feijian.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialItemServiceImpl implements MaterialItemService {

    MaterialItemRepository marItemRepository;
    @Autowired
    public MaterialItemServiceImpl(MaterialItemRepository marItemRepository) {
        this.marItemRepository = marItemRepository;
    }

    @Override
    public MaterialItem updateMaterialItem(MaterialItem item) {
        marItemRepository.save(item);
        return item;
    }

    @Override
    public boolean deleteMaterialItem( MaterialItem item) {
        marItemRepository.deleteMaterialItemById(item.getId());
        return true;
    }

    @Override
    public MaterialItem get(int itemId) {
        return marItemRepository.getOne(itemId);
    }

    @Override
    public List<MaterialItem> findMaterialItemByWareBill(WareBill bill) {
        return marItemRepository.getMaterialItemsByWareBill(bill);
    }

    @Override
    public List<MaterialItem> findMaterialItemByMaterial(Material material) {
        return marItemRepository.findMaterialItemsByMaterial(material);
    }


}
