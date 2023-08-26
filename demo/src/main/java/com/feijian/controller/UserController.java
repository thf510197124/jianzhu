package com.feijian.controller;

import com.feijian.constant.RoleConstant;
import com.feijian.domain.Employee;
import com.feijian.domain.Role;
import com.feijian.domain.User;
import com.feijian.service.EmployeeService;
import com.feijian.service.UserService;
import com.feijian.utils.CreateVoMethod;
import com.feijian.utils.UserUtils;
import com.feijian.vo.UserEmp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;
    PasswordEncoder passwordEncoder;
    EmployeeService employeeService;
    @Autowired
    public UserController(UserService userService,PasswordEncoder passwordEncoder,EmployeeService employeeService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;
    }
    @GetMapping("/allUsers")
    public String allUser(Model model){
        List<User> users = userService.allUsers();
        model.addAttribute("users",users);
        model.addAttribute("title","所有用户");
        return "/user/userList";
    }
    @GetMapping("/{id}")
    public String userDetail(@PathVariable int id, Model model){
        User someUser = userService.get(id);
        setModel(model,someUser);
        UserEmp userEmp = UserEmp.getUserEmp(someUser);
        model.addAttribute("userEmp",userEmp);
        return "/user/user";
    }
    @GetMapping("/myDetail")
    public String myDetail(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetail(user.getId(),model);
    }
    @PostMapping("/update/{userId}")
    public String updateUser(@PathVariable int userId,UserEmp userEmp){
        User user = userService.get(userId);
        user = userEmp.setUserAndEmp(user,user.getEmployee());
        userService.updateUser(user);
        return "redirect:/user/" + user.getId();
    }
    @GetMapping("/update/employee/{userId}")
    public String updateEmployee(@PathVariable int userId,Model model){
        User user;
        if (userId == 0){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }else{
            user = userService.get(userId);
        }
        Employee employee = user.getEmployee();
        if (employee == null){
            employee = new Employee();
        }
        model.addAttribute("emp",employee);
        model.addAttribute("action","update");
        model.addAttribute("userId",userId);
        model.addAttribute("title",employee.getId() == 0 ? "添加我的员工信息":"修改我的员工信息");
        return "/user/employeeForm";
    }
    @PostMapping("/update/employee/{userId}")
    public String updateEmployee(@PathVariable int userId,Employee employee, Model model){
        User user;
        if (userId == 0){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }else{
            user = userService.get(userId);
        }
        user.setEmployee(employee);
        userService.updateUser(user);
        setModel(model,user);
        return "/user/user";
    }
    @GetMapping("/add/employee/{userId}")
    public String addEmployee(@PathVariable int userId, Model model){
        Employee employee = new Employee();
        if (userId != 0){
            model.addAttribute("userId",userId);
        }
        model.addAttribute("emp",employee);
        model.addAttribute("action","add");
        model.addAttribute("title","添加其他员工信息");
        return "/user/employeeForm";
    }
    @PostMapping("/add/employee/{userId}")
    public String addEmployee(@PathVariable int userId, Employee employee, Model model){
        if (userId != 0){
            User user = userService.get(userId);
            user.setEmployee(employee);
            userService.updateUser(user);
        }else{
            employeeService.saveOrUpdate(employee);
        }
        List<Employee> employees = employeeService.all();
        model.addAttribute("employees",employees);
        model.addAttribute("needBind",false);
        model.addAttribute("title","所有用户");
        return "/user/employeeList";
    }
    private void setModel(Model model, User user) {
        model.addAttribute("someUser",user);
        Employee employee = user.getEmployee();
        if (employee == null){
            employee = CreateVoMethod.nullEmployee();
        }
        model.addAttribute("emp", employee);
        List<String> roles = RoleConstant.getSubRole(user.getRoles());
        if (roles != null){
            model.addAttribute("roles",roles);
        }
    }

    @GetMapping("/needEnabled")
    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_root') or hasRole('ROLE_manager')")
    public String needEnabledUsers(Model model){
        List<User> users = userService.needEnabled(false);
        model.addAttribute("users",users);
        return "/user/needEnabledList";
    }

    /**
     * 使用局部更新，很重要的一个controller
     * @param userId 要更新的user
     * @param model
     * @return
     */
    @GetMapping("/enable/{userId}")
    public String enableUser(@PathVariable int userId, Model model){
        User user = userService.get(userId);
        user.setEnabled(true);
        userService.updateUser(user);
        return "redirect:/user/allUsers";
    }


    @PostMapping("/addRole/{userId}")
    public String addRole(@PathVariable int userId, @RequestParam String role, Model model){
        User user = userService.get(userId);
        Role role1 = RoleConstant.getRole(role);
        if (role1 != null){
            user.addRole(role1);
            userService.updateUser(user);
        }else{
            String errorMessage = "输入角色权限无法解析，请重新输入";
            model.addAttribute("errorMessage",errorMessage);
        }
        return userDetail(userId,model);
    }

    @GetMapping("/updatePass")
    public String updatePass(){
        return "/user/pass";
    }
    @PostMapping("/updatePass")
    public String updatePass(String oldPass,String newPass,Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newpass = passwordEncoder.encode(newPass);
        System.out.println(user.getPassword());
        if (passwordEncoder.matches(oldPass,user.getPassword())){
            user.setPassword(newpass);
            userService.updateUser(user);
            return "redirect:/login";
        }else{
            model.addAttribute("errorMess","原始密码输入错误，请重新输入");
            return "/user/pass";
        }
    }
    @GetMapping("/bind")
    public String bindEmp(Model model){
        model.addAttribute("needBind",true);
        List<Employee> employees = employeeService.getEmployeesWithoutUser();
        model.addAttribute("employees",employees);
        model.addAttribute("title","绑定员工");
        model.addAttribute("needBind",true);
        return "/user/employeeList";
    }
    @GetMapping("/bind/{employeeId}")
    public String bindEmp(@PathVariable int employeeId,Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee employee = employeeService.get(employeeId);
        if (user.getEmployee() == null){
            user.setEmployee(employee);
            userService.updateUser(user);
        }
        return myDetail(model);
    }
}
