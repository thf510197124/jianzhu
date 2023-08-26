package com.feijian.dao;

import com.feijian.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer>, JpaSpecificationExecutor<Employee> {
    public List<Employee> findAll();
    public List<Employee> getEmployeesByName(String name);
    @Query(value = "select * from employee emp where not exists (select employee from user where emp.id = user.employee)",nativeQuery = true)
    public List<Employee> getEmployeesWithoutUser();
}
