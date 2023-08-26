package com.feijian.config;

import com.feijian.domain.Employee;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class EmployeeToString implements Converter<Employee,String> {
    @Override
    public String convert(Employee source) {
        return source.getName();
    }
}
