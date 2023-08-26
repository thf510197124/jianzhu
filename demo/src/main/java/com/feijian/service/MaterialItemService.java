package com.feijian.service;

import com.feijian.domain.*;
import com.feijian.exceptions.DifferentUnitException;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;

import java.util.List;

/**
 * 主要用于材料统计
 */
public interface MaterialItemService {
    //添加都是从WareBill中添加的，所有应该不会单独用到
    /*public MaterialItem saveMaterialItem(MaterialItem item);*/

    /**
     * 可以单独更新item选项，
     * @param item
     * @return
     */
    public MaterialItem updateMaterialItem(MaterialItem item);

    /**
     * 可以单独删除item
     * @param item
     * @return
     */
    public boolean deleteMaterialItem(MaterialItem item);

    public MaterialItem get(int itemId);
    public List<MaterialItem> findMaterialItemByWareBill(WareBill bill);
    public List<MaterialItem> findMaterialItemByMaterial(Material material);
}
