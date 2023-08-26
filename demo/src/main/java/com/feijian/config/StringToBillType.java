package com.feijian.config;

import com.feijian.item.BillType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBillType implements Converter<String,BillType> {
    @Override
    public BillType convert(String source) {
        return BillType.get(source);
    }
}
