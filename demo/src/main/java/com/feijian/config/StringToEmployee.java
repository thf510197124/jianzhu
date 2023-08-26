package com.feijian.config;

import com.feijian.domain.Employee;
import com.feijian.service.EmployeeService;
import com.feijian.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEmployee implements Converter<String, Employee> {
    private  EmployeeService employeeService;

    public void setService() {
        this.employeeService = SpringContextUtil.getBean(EmployeeService.class);
    }

    @Override
    public Employee convert(String s) {
        setService();
        if (s.trim().length() == 0){
            return null;
        }else{
            if (employeeService.getEmployeeByName(s) == null){
                Employee employee = new Employee();
                employee.setName(s);
                employeeService.saveOrUpdate(employee);
            }
            return employeeService.getEmployeeByName(s);
        }
    }
}
