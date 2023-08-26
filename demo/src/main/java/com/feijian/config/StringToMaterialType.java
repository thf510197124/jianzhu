package com.feijian.config;

import com.feijian.item.MaterialType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMaterialType implements Converter<String, MaterialType> {
    @Override
    public MaterialType convert(String source) {
        return MaterialType.get(source);
    }
}
