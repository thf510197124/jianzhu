package com.feijian;

import com.feijian.controller.UserController;
import com.feijian.domain.Employee;
import com.feijian.domain.Role;
import com.feijian.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    UserController service;
    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("my_root");
        user.setPassword("8023");
        Employee employee = new Employee();
        employee.setName("童海峰");
        employee.setDuties("客户经理");
        employee.setPhone1("18626344169");
        employee.setPhone2("13395106848");
        user.setEmployee(employee);
        user.setEnabled(true);
        Role role = new Role();
        role.setName("root");
        role.setNameZN("系统权限");
        user.addRole(role);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        service.register(user,null);

    }

}
