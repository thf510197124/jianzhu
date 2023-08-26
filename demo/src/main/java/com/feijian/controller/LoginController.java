package com.feijian.controller;

import com.feijian.domain.User;
import com.feijian.service.UserService;
import com.feijian.utils.CreateVoMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    UserService userService;
    PasswordEncoder passwordEncoder;
    @Autowired
    public LoginController(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        User user = new User();
        model.addAttribute(user);
        return "/login";
    }
    @GetMapping(value = "/index")
    public String loginSuccess(Model model) {
        User ud = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user",ud);
        return "/index";
    }
    @GetMapping(value = "/register")
    public String register(Model model){
        User user = new User();
        model.addAttribute(user);
        return "/register";
    }

    @PostMapping(value = "/register")
    public String register(User user,Model model){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.addRole(CreateVoMethod.createRole("user","普通用户"));
        user = userService.register(user);
        model.addAttribute("user",user);
        return "/regSuccess";
    }
    @RequestMapping("/singout")
    public String logout(){
        return "/login";
    }
}
