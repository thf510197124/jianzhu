package com.feijian.utils;

import com.feijian.domain.Employee;
import com.feijian.domain.Role;
import com.feijian.domain.User;

public class CreateVoMethod {
    public static Role createRole(String name, String nameZN){
        Role role = new Role();
        role.setName(name);
        role.setNameZN(nameZN);
        return  role;
    }
    public static User createUser(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
    public static Employee nullEmployee(){
        Employee employee = new Employee();
        employee.setDuties("");
        employee.setName("");
        employee.setPhone1("");
        employee.setPhone2("");
        return employee;
    }
}
