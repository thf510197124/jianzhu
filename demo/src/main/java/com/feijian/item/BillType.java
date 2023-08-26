package com.feijian.item;

public enum BillType {
    PLAN("计划单"),
    BUY("购入单"),
    USE("使用单"),
    STORE("库存-仅供查询");

    private final String name;
    public String getName(){
        return name;
    }
    BillType(String name) {
        this.name = name;
    }
    public static BillType get(String name){
        for (BillType type : BillType.values()){
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
