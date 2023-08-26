package com.feijian.service;

import com.feijian.domain.Employee;

import java.util.List;

public interface EmployeeService {

    public Employee saveOrUpdate(Employee employee);
    public void deleteById(int employeeId);
    public Employee get(int id);
    public List<Employee> all();
    public Employee getEmployeeByName(String name);
    public List<Employee> getEmployeesWithoutUser();
}
