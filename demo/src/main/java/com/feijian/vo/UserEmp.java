package com.feijian.vo;

import com.feijian.constant.RoleConstant;
import com.feijian.domain.Employee;
import com.feijian.domain.Role;
import com.feijian.domain.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class UserEmp {
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String name;
    private String phone1;
    private String phone2;
    private String duties;

    public User setUserAndEmp(User user,Employee emp){
        if (emp == null){
            emp = new Employee();
        }
        user.setAccountNonExpired(accountNonExpired);
        user.setAccountNonLocked(accountNonLocked);
        user.setCredentialsNonExpired(credentialsNonExpired);
        user.setEnabled(enabled);
        emp.setName(name);
        emp.setPhone1(phone1);
        emp.setPhone2(phone2);
        emp.setDuties(duties);
        user.setEmployee(emp);
        return user;
    }
    public static UserEmp getUserEmp(User user,Employee emp){
        UserEmp ue = new UserEmp();
        ue.setAccountNonExpired(user.isAccountNonExpired());
        ue.setAccountNonLocked(user.isAccountNonLocked());
        ue.setCredentialsNonExpired(user.isCredentialsNonExpired());
        ue.setEnabled(user.isEnabled());
        if (emp != null) {
            ue.setName(emp.getName());
            ue.setPhone1(emp.getPhone1());
            ue.setPhone2(emp.getPhone2());
            ue.setDuties(emp.getDuties());
        }
        return ue;
    }
    public static UserEmp getUserEmp(User user){
        Employee emp = user.getEmployee();
        return getUserEmp(user,emp);
    }
}
