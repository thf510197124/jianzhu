package com.feijian.config;

import com.feijian.domain.Company;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyToString implements Converter<Company,String> {
    @Override
    public String convert(Company source) {
        return source.getCompanyName();
    }
}
