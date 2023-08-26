package com.feijian.utils;

import com.feijian.domain.Material;
import com.feijian.item.MaterialType;
import com.feijian.item.UnitType;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Random;

/**
 * 原则上文字相同，则输出的code相同，主要不同可能来自spec，所以可能需要四位
 */
public class MaterialCodeGenerator {
    public static String getCode(MaterialType type, String name,String texture, String spec,  @Nullable UnitType unit) {
        StringBuilder sb = new StringBuilder();
        if (type == null){
            sb.append(code(null,2));
        }else{
            int tempCode = type.ordinal() + 1;
            sb.append(get(tempCode,2));//2位
        }
        sb.append(code(name,3));//2位
        sb.append(code(texture,2));//2位
        sb.append(code(spec,4));//4位
        if (unit == null){
            sb.append(code(null,2));
        }else{
            int tempUnit = unit.ordinal() + 1;//2位
            sb.append(get(tempUnit,2));
        }
        return sb.toString();
    }
    public static String getCode(Material material){
        return getCode(material.getMaterialType(),material.getMaterialName(),material.getTexture(),material.getSpec(),material.getUnitType());
    }
    private static String code(@Nullable String str, int length){
        if (str == null || str.equals("")){
            return getForNull(length);
        }
        int code = Objects.hash(str);
        return get(code,length);
    }
    private static String getForNull(int length){//如果该项为空，设置为0；
        return "0".repeat(Math.max(0, length));
    }
    private static String get(int i,int length){
        i = Math.abs(i);
        if (i < 10){
            return "0"+i;
        }else{
            if (i < 100){
                return Integer.toString(i);
            }else{
                String x = Integer.toString(Math.abs(i));
                StringBuilder sb1 = new StringBuilder();
                for (int j = x.length() - 1;j >= x.length() - length;j--){
                    sb1.append(x.charAt(j));
                }
                return sb1.toString();
            }

        }
    }

    public static void main(String[] args) {
        String str = getCode(MaterialType.A,"碳钢",null,"3.0*1219*3048",UnitType.公斤);
        String str2 = getCode(MaterialType.A,"碳钢","","3.0*1219*2438",UnitType.公斤);
        String str3 = getCode(MaterialType.A,"碳钢","316L0","3.0*1219*2438",UnitType.公斤);
        String str4 = getCode(MaterialType.A,"碳钢","316L","3.0*12192438*2356",UnitType.公斤);
        System.out.println(str);
        System.out.println(str2);
        System.out.println(str3);
        System.out.println(str4);
    }
}
