package com.feijian.service.impl;

import com.feijian.dao.EmployeeRepository;
import com.feijian.domain.Employee;
import com.feijian.exceptions.ExistException;
import com.feijian.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeRepository employeeRepository;
    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveOrUpdate(Employee employee) {
        if (employee.getId() == 0){
            Employee e1 = getEmployeeByName(employee.getName());
            if (e1 != null){
                throw new ExistException("员工姓名已经存在，不能重复添加");
            }
        }
        employeeRepository.save(employee);
        return employeeRepository.getOne(employee.getId());
    }

    @Override
    public void deleteById(int employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Employee get(int id) {
        return employeeRepository.getOne(id);
    }

    @Override
    public List<Employee> all() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeByName(String name) {
        List<Employee> employees = employeeRepository.getEmployeesByName(name);
        if (employees.size() == 0){
            return null;
        }
        return employeeRepository.getEmployeesByName(name).get(0);
    }

    @Override
    public List<Employee> getEmployeesWithoutUser() {
        return employeeRepository.getEmployeesWithoutUser();
    }

}
