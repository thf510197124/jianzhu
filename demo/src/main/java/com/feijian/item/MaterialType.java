package com.feijian.item;

import com.feijian.domain.MaterialItem;

import java.util.Objects;

public enum MaterialType {
    A("黑色金属"),
    B("木材及制品"),
    C("水泥及制品"),
    D("砖、瓦、砂、石、灰"),
    E("门、窗、玻璃类"),
    F("混凝土及构件"),
    G("五金"),
    H("油漆、装饰、防水类"),
    I("人工"),
    J("机器台班"),
    K("有色金属"),
    L("墙体及保温材料"),
    M("塑料及制品"),
    N("管材及配件");
    private final String name;

    public String getName() {
        return name;
    }

    MaterialType(String name) {
        this.name = name;
    }
    public static MaterialType get(String name){
        if (Objects.equals(name, "") || name== null){
            return null;
        }
        for (MaterialType type : MaterialType.values()){
            if (type.getName().equals(name)){
                return type;
            }
        }
        return null;
    }
    @Override
    public String toString(){
        return name;
    }
}
