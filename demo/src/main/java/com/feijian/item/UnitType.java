package com.feijian.item;

public enum UnitType {
    吨{
        @Override
        public String toString() {
            return "吨";
        }
    },
    平方{
        @Override
        public String toString() {
            return "平方";
        }
    },
    立方{
        @Override
        public String toString() {
            return "立方";
        }
    },
    张{
        @Override
        public String toString() {
            return "张";
        }
    },
    米{
        @Override
        public String toString() {
            return "米";
        }
    },
    只{
        @Override
        public String toString() {
            return "只";
        }
    },
    套{
        @Override
        public String toString() {
            return "套";
        }
    },
    节{
        @Override
        public String toString() {
            return "节";
        }
    },
    公斤{
        @Override
        public String toString() {
            return "公斤";
        }
    },
    百个{
        @Override
        public String toString() {
            return "百个";
        }
    },
    卷{
        @Override
        public String toString() {
            return "卷";
        }
    },
    工{
        @Override
        public String toString() {
            return "工";
        }
    },
    千瓦时{
        @Override
        public String toString() {
            return "千瓦时";
        }
    },
    班{
        @Override
        public String toString() {
            return "班";
        }
    };
    public static UnitType get(String name){
        for (UnitType type : UnitType.values()){
            if (type.toString().equals(name)){
                return type;
            }
        }
        return null;
    }
}
